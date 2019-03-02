package de.hhu.bigdata.elasticsearch;

import de.hhu.bigdata.config.ElasticHost;
import de.hhu.bigdata.elasticsearch.sink.CountLangPredInserter;
import de.hhu.bigdata.elasticsearch.sink.FollowerInserter;
import de.hhu.bigdata.elasticsearch.sink.CountInserter;
import de.hhu.bigdata.elasticsearch.sink.FollowerPredInserter;
import de.hhu.bigdata.elasticsearch.sink.FriendsInserter;
import de.hhu.bigdata.elasticsearch.sink.LocationInserter;
import de.hhu.bigdata.elasticsearch.sink.VerifiedInserter;
import de.hhu.bigdata.elasticsearch.sink.VerifiedPredInserter;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Provies acces to different SinkFunctions
 */
public class TweetSink {
	/**
	 * Gives a SinkFunction which can be used to sink the Tuple6 into the twitter/countries index
	 *
	 * @return SinkFunction for country
	 */
	public static SinkFunction<Tuple6<String, String, Float, Long, Float, Float>> getCountrySink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple6<String, String, Float, Long, Float, Float>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new LocationInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the Tuple3 into the twitter/follower index
	 *
	 * @return
	 */
	public static SinkFunction<Tuple3<String, Float, Long>> getFollowerSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple3<String, Float, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new FollowerInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}


	/**
	 * Gives a SinkFunction which can be used to sink the Tuple3 into the twitter/count index
	 *
	 * @return
	 */
	public static SinkFunction<Tuple3<String, Float, Long>> getCountSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple3<String, Float, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new CountInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the Tuple4 into the twitter/verified index
	 *
	 * @return
	 */
	public static SinkFunction<Tuple4<String, String, Float, Long>> getVerifiedSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple4<String, String, Float, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new VerifiedInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the Tuple4 into the twitter/verified index
	 *
	 * @return
	 */
	public static SinkFunction<Tuple3<String, Float, Long>> getFriendsSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple3<String, Float, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new FriendsInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the prediction
	 *
	 * @return
	 */
	public static SinkFunction<Tuple5<String, Integer, Float, Float, Long>> getVerifiedPredSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple5<String, Integer, Float, Float, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new VerifiedPredInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the prediction
	 *
	 * @return
	 */
	public static SinkFunction<Tuple5<String, Integer, Integer, Integer, Long>> getFollowerPredSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple5<String, Integer, Integer, Integer, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new FollowerPredInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}

	/**
	 * Gives a SinkFunction which can be used to sink the prediction
	 *
	 * @return
	 */
	public static SinkFunction<Tuple4<String, Integer, Integer, Long>> getCountLangPredSink() {
		List<HttpHost> httpHosts = new ArrayList<>();
		httpHosts.add(new HttpHost(ElasticHost.host, 9200, "http"));
		// use a ElasticsearchSink.Builder to create an ElasticsearchSink
		ElasticsearchSink.Builder<Tuple4<String, Integer, Integer, Long>> esSinkBuilder = new ElasticsearchSink.Builder<>(
				httpHosts,
				new CountLangPredInserter()
		);
		// configuration for the bulk requests; this instructs the sink to emit after every element, otherwise they would be buffered
		esSinkBuilder.setBulkFlushMaxActions(1);

		return esSinkBuilder.build();
	}
}
