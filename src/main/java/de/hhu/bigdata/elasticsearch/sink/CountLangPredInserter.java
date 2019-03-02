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
 * Allows to sink a Prediction into the elasticSearch index
 */
public class CountLangPredInserter implements ElasticsearchSinkFunction<Tuple4<String, Integer, Integer, Long>> {
	@Override
	public void process(Tuple4<String, Integer, Integer, Long> record, RuntimeContext ctx, RequestIndexer indexer) {
		//		// construct JSON document to index
		Map<String, String> json = new HashMap<>();
		json.put("type", record.f0 + "_real");
		json.put("count", record.f1.toString());
		json.put("time", record.f3.toString());

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_pred_lang")
				.type("lang")
				.source(json);

		indexer.add(rqst);

		json = new HashMap<>();
		json.put("type", record.f0 + "_pred");
		json.put("count", record.f2.toString());
		json.put("time", record.f3.toString());

		// as configured in elasticSearch
		rqst = Requests.indexRequest()
				.index("tw_pred_lang")
				.type("lang")
				.source(json);

		indexer.add(rqst);
	}
}