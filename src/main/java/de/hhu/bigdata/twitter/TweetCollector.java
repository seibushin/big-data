package de.hhu.bigdata.twitter;

import de.hhu.bigdata.config.Auth;
import de.hhu.bigdata.processing.CustomEndPoint;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

public class TweetCollector {
	public static void main(String[] args) throws Exception {
		collect("tweets_" + System.currentTimeMillis());
	}

	public static void collect(String file) throws Exception {
		// set up streaming execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// Setup twitter source
		TwitterSource twitterSource = new TwitterSource(Auth.getAuth());
		twitterSource.setCustomEndpointInitializer(new CustomEndPoint());

		// add twitter source to the environment and get the resulting datastream
		DataStream<String> twitter = env.addSource(twitterSource);

		DataStream<String> tweets = twitter
				.filter(s -> !s.equals(""));

		// write to single file
		tweets.writeAsText(file).setParallelism(1);

		// execute the transformation pipeline
		env.execute("TweetCollector");
	}
}
