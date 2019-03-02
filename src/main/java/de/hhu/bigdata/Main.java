package de.hhu.bigdata;

import de.hhu.bigdata.config.ElasticHost;
import de.hhu.bigdata.twitter.TweetCollector;
import de.hhu.bigdata.twitter.TwitterBatch;
import de.hhu.bigdata.twitter.TwitterStream;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {

	public static Logger log = LoggerFactory.getLogger(Main.class);

	/**
	 * -batch FILE for Batch processing
	 * -stream for Stream processing
	 * -ml for predictions
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ParameterTool params = ParameterTool.fromArgs(args);

		if (params.has("host")) {
			ElasticHost.host = params.get("host");
		}

		if (params.has("collect")) {
			String file = params.get("collect").equals("__NO_VALUE_KEY") ? "tweets_" + System.currentTimeMillis() : params.get("collect");
			log.info("Collecting data from Twitter into " + file);
			TweetCollector.collect(file);
		} else {
			boolean batch = false;

			// Batch processing
			if (params.has("batch")) {
				log.info("Performing batch processing...");

				String file = params.get("batch");
				if (new File(file).exists()) {
					log.info("Selected batch: " + file);
					batch = true;
					TwitterBatch.batch(file);
				} else {
					log.info("File '" + file + "' does not exist");
				}
			}

			// stream processing
			if (params.has("stream")) {
				log.info("Performing stream processing...");
				String file = params.get("stream").equals("__NO_VALUE_KEY") ? "" : params.get("stream");
				// we can only compare if we performed batch processing
				boolean compare = params.has("compare") && batch;
				// make predictions?
				boolean ml = params.has("ml");

				log.info("Using alternate file as stream: " + !file.equals(""));
				log.info("Comparing with batch: " + compare);
				log.info("Making predictions: " + ml);
				TwitterStream.stream(file, compare, ml);
			}
		}
	}
}
