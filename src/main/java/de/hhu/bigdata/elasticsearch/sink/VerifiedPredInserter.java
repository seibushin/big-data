package de.hhu.bigdata.elasticsearch.sink;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows to sink a prediction into the elasticSearch index
 */
public class VerifiedPredInserter implements ElasticsearchSinkFunction<Tuple5<String, Integer, Float, Float, Long>> {
	@Override
	public void process(Tuple5<String, Integer, Float, Float, Long> record, RuntimeContext ctx, RequestIndexer indexer) {
		//		// construct JSON document to index
		Map<String, String> json = new HashMap<>();
		json.put("type", "real");
		json.put("follower", record.f1.toString());
		json.put("verified", record.f2.toString());
		json.put("time", record.f4.toString());

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_pred_verified")
				.type("verified")
				.source(json);

		indexer.add(rqst);

		json = new HashMap<>();
		json.put("type", "pred");
		json.put("follower", record.f1.toString());
		json.put("verified", record.f3.toString());
		json.put("time", record.f4.toString());

		// as configured in elasticSearch
		rqst = Requests.indexRequest()
				.index("tw_pred_verified")
				.type("verified")
				.source(json);

		indexer.add(rqst);
	}
}