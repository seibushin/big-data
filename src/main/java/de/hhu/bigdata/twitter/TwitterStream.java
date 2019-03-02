package de.hhu.bigdata.twitter;

import de.hhu.bigdata.config.Auth;
import de.hhu.bigdata.elasticsearch.TweetSink;
import de.hhu.bigdata.prediction.CountLangPrediction;
import de.hhu.bigdata.prediction.HashtagsPrediction;
import de.hhu.bigdata.prediction.VerifiedPrediction;
import de.hhu.bigdata.processing.CustomEndPoint;
import de.hhu.bigdata.processing.stream.apply.AvgFollower;
import de.hhu.bigdata.processing.stream.apply.CountFriends;
import de.hhu.bigdata.processing.stream.apply.CountTweets;
import de.hhu.bigdata.processing.stream.apply.HashtagCountryCounter;
import de.hhu.bigdata.processing.stream.apply.MostFollowedVerified;
import de.hhu.bigdata.processing.stream.map.LangMapper;
import de.hhu.bigdata.processing.stream.map.TweetMapper;
import de.hhu.bigdata.processing.stream.source.TweetSource;
import de.hhu.bigdata.twitter.model.Tweet;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

public class TwitterStream {
	public static void stream(String stream, boolean compare, boolean ml) throws Exception {
		// set up streaming execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// choose wether to use the twitter stream directly or to use the given file as stream
		DataStream<String> twitter;
		if (stream.equals("")) {
			// directly use the twitter stream
			// Setup twitter source
			TwitterSource twitterSource = new TwitterSource(Auth.getAuth());
			twitterSource.setCustomEndpointInitializer(new CustomEndPoint());

			// add twitter source to the environment and get the resulting datastream
			twitter = env.addSource(twitterSource);
		} else {
			// use the file as stream
			final int servingSpeedFactor = 600; // events of 10 minutes are served in 1 second
			env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

			// add twitter source to the environment and get the resulting datastream
			twitter = env.addSource(new TweetSource(stream, servingSpeedFactor));
		}

		// get the tweets from the twitter inputStream
		DataStream<Tweet> tweets = twitter
				// map the inputStream to tweet objects
				.flatMap(new TweetMapper())
				// filter for the hashtags
				.filter(tweet -> tweet.containsHashtags("cat,dog,bird,fish,snake"));

		// compute the 5 metrics
		// 1. calculate the avg sum of follower for each hashtag
		DataStream<Tuple3<String, Float, Long>> follower = tweets
				// partition by found_hashtag
				.keyBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				.timeWindow(Time.minutes(10))
				// calculate the avg follower count
				.apply(new AvgFollower(BatchResult.getFollower(), compare));
		// emit the data to elasticSearch
		follower.addSink(TweetSink.getFollowerSink());

		// 2. calculate the sum of tweets in 10 minutes
		DataStream<Tuple3<String, Float, Long>> count = tweets
				// partition by found_hashtag
				.keyBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				.timeWindow(Time.minutes(10))
				// count the tweets
				.apply(new CountTweets(BatchResult.getCount(), compare));
		// emit the data to elasticSearch
		count.addSink(TweetSink.getCountSink());

		// 3. count the tweet sum for the found_hashtag and the used lang
		DataStream<Tuple6<String, String, Float, Long, Float, Float>> countries = tweets
				// partition by lang and found_hashtag
				.keyBy(tweet -> tweet.getTweetOrUserLang() + tweet.found_hashtag)
				// sliding window with windowsize 15min and slide 5min
				.timeWindow(Time.minutes(15), Time.minutes(5))
				// count the number of tweets
				.apply(new HashtagCountryCounter(BatchResult.getCountries(), compare))
				// map lang to country coordinates
				.map(new LangMapper());
		// emit the data to elasticSearch
		countries.addSink(TweetSink.getCountrySink());

		// 4. most followed verified account for each hashtag
		DataStream<Tuple4<String, String, Float, Long>> verified = tweets
				// filter for verified users
				.filter(tweet -> tweet.user.verified)
				// partition by found_hashtag
				.keyBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				.timeWindow(Time.minutes(10))
				// compute the most followed user from the tweets
				.apply(new MostFollowedVerified(BatchResult.getVerified(), compare));
		// emit the data to elasticSearch
		verified.addSink(TweetSink.getVerifiedSink());

		// 5. count sum of their follows/friends
		DataStream<Tuple3<String, Float, Long>> friends = tweets
				// partition by found_hashtag and user to prevent from counting one user multiple times
				.keyBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				.timeWindow(Time.minutes(10))
				// compute sum of friends
				.apply(new CountFriends(BatchResult.getFriends(), compare));
		// emit the data to elasticSearch
		friends.addSink(TweetSink.getFriendsSink());

		if (ml) {
			// make 3 predictions
			// 1. predict verified
			tweets.keyBy(tweet -> tweet.found_hashtag)
					// refine the model and predict
					.flatMap(new VerifiedPrediction.PredictionModel())
					// add sink for elasticSearch
					.addSink(TweetSink.getVerifiedPredSink());

			// 2. predict number of hashtags to follower_count
			tweets.keyBy(tweet -> tweet.found_hashtag)
					.flatMap(new HashtagsPrediction.PredictionModel())
					// add sink for elasticSearch
					.addSink(TweetSink.getFollowerPredSink());

			// 3. predict number of tweets based on lang every 10min
			tweets.keyBy(tweet -> tweet.getTweetOrUserLang())
					.timeWindow(Time.minutes(10))
					// refine the model and predict
					.apply(new CountLangPrediction.PredictionModel())
					// add sink for elasticSearch
					.addSink(TweetSink.getCountLangPredSink());
		}

		env.execute("Twitter");
	}
}
