package de.hhu.bigdata.prediction;

import de.hhu.bigdata.twitter.model.Tweet;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class CountLangPrediction {
	private Map<String, SimpleRegression> models;

	public CountLangPrediction() {
		models = new HashMap<>();
	}

	/**
	 * Predicts the number of tweets by using the langs
	 *
	 * @param lang
	 * @return
	 */
	public int predict(String lang, long time) {
		if (models.containsKey(lang)) {
			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(time);
			double prediction = models.get(lang).predict(c.get(Calendar.MINUTE));

			if (Double.isNaN(prediction)) {
				return -1;
			} else {
				return (int) prediction;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Refines the model by adding a data point
	 *
	 * @param count
	 * @param lang
	 */
	public void refineModel(String lang, long time, int count) {
		if (!models.containsKey(lang)) {
			models.put(lang, new SimpleRegression(false));
		}

		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);

		models.get(lang).addData(c.get(Calendar.MINUTE), count);
	}

	/**
	 * FlatMapFunction
	 */
	public static class PredictionModel extends RichWindowFunction<Tweet, Tuple4<String, Integer, Integer, Long>, String, TimeWindow> {
		private transient ValueState<CountLangPrediction> modelState;

		@Override
		public void apply(String lang, TimeWindow timeWindow, Iterable<Tweet> iterable, Collector<Tuple4<String, Integer, Integer, Long>> collector) throws Exception {
			// fetch operator state
			CountLangPrediction model = modelState.value();
			if (model == null) {
				model = new CountLangPrediction();
			}


			int count = 0;
			for (Tweet tweet : iterable) {
				count++;
			}

			int prediction = model.predict(lang, timeWindow.getEnd());
			// emit prediction
			collector.collect(new Tuple4<>(lang, count, prediction, timeWindow.getEnd()));

			// refine model
			model.refineModel(lang, timeWindow.getEnd(), count);

			modelState.update(model);
		}

		@Override
		public void open(Configuration config) {
			// obtain key-value state for prediction model
			ValueStateDescriptor<CountLangPrediction> descriptor =
					new ValueStateDescriptor<>(
							// state name
							"countLangRegressionModel",
							// type information of state
							TypeInformation.of(CountLangPrediction.class));
			modelState = getRuntimeContext().getState(descriptor);
		}
	}
}
