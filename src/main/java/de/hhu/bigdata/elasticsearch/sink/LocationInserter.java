package de.hhu.bigdata.elasticsearch.sink;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows to sink a @{@link de.hhu.bigdata.processing.stream.map.LangMapper} into the elasticSearch index
 */
public class LocationInserter implements ElasticsearchSinkFunction<Tuple6<String, String, Float, Long, Float, Float>> {
	@Override
	public void process(Tuple6<String, String, Float, Long, Float, Float> record, RuntimeContext ctx, RequestIndexer indexer) {
		// construct JSON document to index
		Map<String, String> json = new HashMap<>();
		json.put("hashtag", record.f0);
		json.put("lang", record.f1);
		json.put("count", record.f2.toString());
		json.put("time", record.f3.toString());
		// lat,lon
		json.put("location", record.f4 + "," + record.f5);

		// as configured in elasticSearch
		IndexRequest rqst = Requests.indexRequest()
				.index("tw_countries")
				.type("countries")
				.source(json);

		indexer.add(rqst);
	}
}