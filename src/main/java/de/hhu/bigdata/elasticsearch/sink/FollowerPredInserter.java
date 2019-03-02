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
public class FollowerPredInserter implements ElasticsearchSinkFunction<Tuple5<String, Integer, Integer, Integer, Long>> {
	@Override
	public void process(Tuple5<String, Integer, Integer, Integer, Long> record, RuntimeContext ctx, RequestIndexer indexer) {
		Map<String, String> json = new HashMap<>();
		json.put("type", "real");
		json.put("hashtags", record.f1.toString());
		json.put("follower", record.f2.toString());
		json.put("time", record.f4.toString());

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_pred_follower")
				.type("follower")
				.source(json);

		indexer.add(rqst);

		json = new HashMap<>();
		json.put("type", "pred");
		json.put("hashtags", record.f1.toString());
		json.put("follower", record.f3.toString());
		json.put("time", record.f4.toString());

		// as configured in elasticSearch
		rqst = Requests.indexRequest()
				.index("tw_pred_follower")
				.type("follower")
				.source(json);

		indexer.add(rqst);
	}
}