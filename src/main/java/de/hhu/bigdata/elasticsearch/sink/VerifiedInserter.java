package de.hhu.bigdata.elasticsearch.sink;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows to sink a @{@link de.hhu.bigdata.processing.stream.apply.MostFollowedVerified} into the elasticSearch index
 */
public class VerifiedInserter implements ElasticsearchSinkFunction<Tuple4<String, String, Float, Long>> {
	@Override
	public void process(Tuple4<String, String, Float, Long> record, RuntimeContext ctx, RequestIndexer indexer) {
		//		// construct JSON document to index
		Map<String, String> json = new HashMap<>();
		json.put("hashtag", record.f0);
		json.put("user", record.f1);
		json.put("follower", record.f2.toString());
		json.put("time", record.f3.toString());

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_verified")
				.type("verified")
				.source(json);

		indexer.add(rqst);
	}
}