package de.hhu.bigdata.processing;

import com.twitter.hbc.core.endpoint.RawEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

import java.io.Serializable;

public class CustomEndPoint implements TwitterSource.EndpointInitializer, Serializable {
	@Override
	public StreamingEndpoint createEndpoint() {
		return new RawEndpoint("/1.1/statuses/filter.json?track=%23cat,%23dog,%23bird,%23fish,%23snake", "GET");
	}
}