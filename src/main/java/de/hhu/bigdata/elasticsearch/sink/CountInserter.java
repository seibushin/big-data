package de.hhu.bigdata.elasticsearch.sink;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows to sink a @{@link de.hhu.bigdata.processing.stream.apply.CountTweets} into the elasticSearch index
 */
public class CountInserter implements ElasticsearchSinkFunction<Tuple3<String, Float, Long>> {
	@Override
	public void process(Tuple3<String, Float, Long> record, RuntimeContext ctx, RequestIndexer indexer) {
		// construct JSON document to index
		Map<String, String> json = new HashMap<>();
		json.put("hashtag", record.f0);
		json.put("count", record.f1.toString());
		json.put("time", record.f2.toString());

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_count")
				.type("count")
				.source(json);

		indexer.add(rqst);
	}
}