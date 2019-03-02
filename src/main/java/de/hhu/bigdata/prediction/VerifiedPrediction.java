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


public class VerifiedPrediction {
	private SimpleRegression model;

	public VerifiedPrediction() {
		model = new SimpleRegression();
	}

	/**
	 * Predicts if a user is verified based on the follower count
	 *
	 * @param follower
	 * @return
	 */
	public float predict(int follower) {
		double prediction = model.predict(follower);

		if (Double.isNaN(prediction)) {
			return -1;
		} else {
			return (float) prediction;
		}
	}

	/**
	 * Refines the model by adding a data point
	 *
	 * @param follower
	 * @param verified
	 */
	public void refineModel(int follower, int verified) {
		model.addData(follower, verified);
	}

	/**
	 * FlatMapFunction
	 */
	public static class PredictionModel extends RichFlatMapFunction<Tweet, Tuple5<String, Integer, Float, Float, Long>> {
		private transient ValueState<VerifiedPrediction> modelState;

		@Override
		public void flatMap(Tweet val, Collector<Tuple5<String, Integer, Float, Float, Long>> out) throws Exception {
			// fetch operator state
			VerifiedPrediction model = modelState.value();
			if (model == null) {
				model = new VerifiedPrediction();
			}

			// emit prediction
			float verified = model.predict(val.user.followers_count);
			out.collect(new Tuple5<>(val.user.name, val.user.followers_count, val.user.verified ? 1F : 0F, verified, val.created_at.getTime()));

			model.refineModel(val.user.followers_count, val.user.verified ? 1 : 0);

			modelState.update(model);
		}

		@Override
		public void open(Configuration config) {
			// obtain key-value state for prediction model
			ValueStateDescriptor<VerifiedPrediction> descriptor =
					new ValueStateDescriptor<>(
							// state name
							"verifiedRegressionModel",
							// type information of state
							TypeInformation.of(VerifiedPrediction.class));
			modelState = getRuntimeContext().getState(descriptor);
		}
	}
}
