package de.hhu.bigdata.twitter;

import de.hhu.bigdata.processing.batch.reduce.AvgFollower;
import de.hhu.bigdata.processing.batch.reduce.CountFriends;
import de.hhu.bigdata.processing.batch.reduce.CountTweets;
import de.hhu.bigdata.processing.batch.reduce.HashtagCountryCounter;
import de.hhu.bigdata.processing.batch.reduce.MostFollowedVerified;
import de.hhu.bigdata.processing.stream.map.LangMapper;
import de.hhu.bigdata.processing.stream.map.TweetMapper;
import de.hhu.bigdata.twitter.model.Tweet;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;

public class TwitterBatch {
	public static void batch(String file) throws Exception {
		// set up execution environment
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// read the tweets from the file
		// the file should contain of an continuous datastream
		DataSource<String> twitter = env.readTextFile(file);

		// get the tweets from the twitter inputStream
		DataSet<Tweet> tweets = twitter
				//map the inputStream to tweet objects
				.flatMap(new TweetMapper())
				//filter for the hashtags
				.filter(tweet -> tweet.containsHashtags("cat,dog,bird,fish,snake"));

		// compute the 5 metrics
		// we compute the same metrics as in @link
		// 1. calculate the avg sum of follower for each hashtag
		DataSet<Tuple3<String, Float, Long>> follower = tweets
				// partition by found_hashtag
				.groupBy(tweet -> tweet.found_hashtag)
				// calculate the avg follower count
				.reduceGroup(new AvgFollower());
		// Update the BatchResult
		BatchResult.updateFollower(follower.collect());

		// 2. calculate the sum of tweets in 10 minutes
		DataSet<Tuple3<String, Float, Long>> count = tweets
				// partition by found_hashtag
				.groupBy(tweet -> tweet.found_hashtag)
				// count the tweets
				.reduceGroup(new CountTweets(10));
		// Update the BatchResult
		BatchResult.updateCount(count.collect());

		// 3. count the tweet sum for the found_hashtag and the used lang
		DataSet<Tuple6<String, String, Float, Long, Float, Float>> countries = tweets
				// partition by lang and found_hashtag
				.groupBy(tweet -> tweet.getTweetOrUserLang() + tweet.found_hashtag)
				// sliding window with windowsize 15min and slide 5min
				// count the number of tweets
				.reduceGroup(new HashtagCountryCounter(15))
				// map lang to country coordinates
				.map(new LangMapper());
		// Update the BatchResult
		BatchResult.updateCountries(countries.collect());

		// 4. most followed verified account for each hashtag
		DataSet<Tuple4<String, String, Float, Long>> verified = tweets
				// filter for verified users
				.filter(tweet -> tweet.user.verified)
				// partition by found_hashtag
				.groupBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				// compute the most followed user from the tweets
				.reduceGroup(new MostFollowedVerified(10));
		// Update the BatchResult
		BatchResult.updateVerified(verified.collect());

		// 5. count sum of their follows/friends
		DataSet<Tuple3<String, Float, Long>> friends = tweets
				// partition by found_hashtag and user to prevent from counting one user multiple times
				.groupBy(tweet -> tweet.found_hashtag)
				// tumbling window of size 10 minutes
				// compute sum of friends
				.reduceGroup(new CountFriends(10));
		// Update the BatchResult
		BatchResult.updateFriends(friends.collect());
	}
}
