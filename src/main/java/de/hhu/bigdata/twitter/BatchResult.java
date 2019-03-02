/* Copyright 2018 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.hhu.bigdata.twitter;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the results of the Batch processing
 */
public class BatchResult {
	private static Logger log = LoggerFactory.getLogger(BatchResult.class);

	private static Map<String, Float> friends = new HashMap<>();
	private static Map<String, Tuple2<String, Float>> verified = new HashMap<>();
	private static Map<String, Float> countries = new HashMap<>();
	private static Map<String, Float> count = new HashMap<>();
	private static Map<String, Float> follower = new HashMap<>();

	static void updateFriends(List<Tuple3<String, Float, Long>> collect) {
		log.info("========== FRIENDS =============");
		for (Tuple3<String, Float, Long> el : collect) {
			log.info(el.toString());
			friends.put(el.f0, el.f1);
		}
	}

	static Map<String, Float> getFriends() {
		return friends;
	}

	static void updateVerified(List<Tuple4<String, String, Float, Long>> collect) {
		log.info("========== VERIFIED =============");
		for (Tuple4<String, String, Float, Long> el : collect) {
			log.info(el.toString());
			verified.put(el.f0, new Tuple2<>(el.f1, el.f2));
		}
	}

	public static Map<String, Tuple2<String, Float>> getVerified() {
		return verified;
	}

	static void updateCountries(List<Tuple6<String, String, Float, Long, Float, Float>> collect) {
		log.info("========== COUNTRIES =============");
		for (Tuple6<String, String, Float, Long, Float, Float> el : collect) {
			log.info(el.toString());
			countries.put(el.f0 + "_" + el.f1, el.f2);
		}
	}

	static Map<String, Float> getCountries() {
		return countries;
	}

	static void updateCount(List<Tuple3<String, Float, Long>> collect) {
		log.info("========== COUNT =============");
		for (Tuple3<String, Float, Long> el : collect) {
			log.info(el.toString());
			count.put(el.f0, el.f1);
		}
	}

	public static Map<String, Float> getCount() {
		return count;
	}

	static void updateFollower(List<Tuple3<String, Float, Long>> collect) {
		log.info("========== FOLLOWER =============");
		for (Tuple3<String, Float, Long> el : collect) {
			log.info(el.toString());
			follower.put(el.f0, el.f1);
		}
	}

	public static Map<String, Float> getFollower() {
		return follower;
	}
}
