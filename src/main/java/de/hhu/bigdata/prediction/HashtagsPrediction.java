package de.hhu.bigdata.prediction;

import de.hhu.bigdata.twitter.model.Tweet;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;


public class HashtagsPrediction {
	private SimpleRegression model;

	public HashtagsPrediction() {
		model = new SimpleRegression();
	}

	/**
	 * Predicts the number of follower by using the number of hashtags
	 *
	 * @param hashtags
	 * @return
	 */
	public int predict(int hashtags) {
		double prediction = model.predict(hashtags);

		if (Double.isNaN(prediction)) {
			return -1;
		} else {
			return (int) prediction;
		}
	}

	/**
	 * Refines the model by adding a data point
	 *
	 * @param follower
	 * @param hashtags
	 */
	public void refineModel(int hashtags, int follower) {
		model.addData(hashtags, follower);
	}

	/**
	 * FlatMapFunction
	 */
	public static class PredictionModel extends RichFlatMapFunction<Tweet, Tuple5<String, Integer, Integer, Integer, Long>> {
		private transient ValueState<HashtagsPrediction> modelState;

		@Override
		public void flatMap(Tweet val, Collector<Tuple5<String, Integer, Integer, Integer, Long>> out) throws Exception {
			// fetch operator state
			HashtagsPrediction model = modelState.value();
			if (model == null) {
				model = new HashtagsPrediction();
			}

			// emit prediction
			int prediction = model.predict(val.hashtags.size());
			out.collect(new Tuple5<>(val.user.name, val.hashtags.size(), val.user.followers_count, prediction, val.created_at.getTime()));

			model.refineModel(val.hashtags.size(), val.user.followers_count);

			modelState.update(model);
		}

		@Override
		public void open(Configuration config) {
			// obtain key-value state for prediction model
			ValueStateDescriptor<HashtagsPrediction> descriptor =
					new ValueStateDescriptor<>(
							// state name
							"hashtagRegressionModel",
							// type information of state
							TypeInformation.of(HashtagsPrediction.class));
			modelState = getRuntimeContext().getState(descriptor);
		}
	}
}
