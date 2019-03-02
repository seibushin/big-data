package de.hhu.bigdata.twitter.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.util.set.Sets;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
	@JsonIgnore
	private static ObjectMapper jsonParser;

	// the Text of the tweet
	public String text;

	// Date when the tweet was created
	// deleted tweets dont have a date
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy", locale = "en")
	public Date created_at;

	// list of all used hashtags
	public Set<String> hashtags = new HashSet<String>();

	// the tweet id
	public long id;

	// source of the tweet might be web for Twitter
	// other possibilities are instagram and others
	public String source;

	// contains the tweet id if it was a reply
	public long in_reply_to_status_id;

	// if the tweet is a reply it contains the user id of the original tweet
	public long in_reply_to_user_id;

	// the user object
	public User user;

	// the geo location of the tweet
	public float lon;
	public float lat;

	// retweet count
	public int retweet_count;

	// favorite count
	public int favorite_count;

	// BCP 47 code - machine detected tweet language
	public String lang;

	public String found_hashtag;

	/**
	 * This method is used to unpack the hashtags from its nested element
	 * In a Pojo for json/jackson you are only able to get the top-level elements via JsonProperty
	 *
	 * @param entities
	 */
	@JsonProperty("entities")
	private void unpackHashtags(Map<String, JsonNode> entities) {
		Iterator<JsonNode> it = entities.get("hashtags").iterator();
		while (it.hasNext()) {
			this.hashtags.add(it.next().get("text").asText());
		}
	}

	/**
	 * This method is used to unpack the coordinates from the nested elements
	 *
	 * @param coordinates
	 */
	@JsonProperty("coordinates")
	private void unpackCoordinates(Map<String, JsonNode> coordinates) {
		if (coordinates != null) {
			Iterator<JsonNode> it = coordinates.get("coordinates").iterator();
			this.lat = it.next().floatValue();
			this.lon = it.next().floatValue();
		}
	}

	/**
	 * Parse the given JSON-String to a Tweet
	 *
	 * @param json
	 * @return
	 */
	public static Tweet parse(String json) {
		// lazy initialize the jsonParser/objectMapper
		if (jsonParser == null) {
			jsonParser = new ObjectMapper();
		}

		try {
			Tweet tweet = jsonParser.readValue(json, Tweet.class);
			// deleted tweets dont have a date
			if (tweet.created_at != null) {
				return tweet;
			}
		} catch (JsonMappingException e) {
			// most of the times this will occur if we get an empty element
			// No content due to end-of-input at [Source: ; line: 1, column: 0]
			// we ignore this error
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			// ignore the exception
			e.printStackTrace();
		}

		// not a valid tweet -> return null
		return null;
	}

	/**
	 * Check if the set of Hashtags contain any of the given hashtag
	 *
	 * @param hashtag_string for example "cat small,dog" where ',' is interpreted as OR and ' ' as AND
	 * @return
	 */
	public boolean containsHashtags(String hashtag_string) {
		String[] hashtag_groups = hashtag_string.split(",");
		for (String hashtags : hashtag_groups) {
			if (this.hashtags.containsAll(Sets.newHashSet(hashtags.split(" ")))) {
				// set the found hashtag for later keyBy
				this.found_hashtag = hashtags;
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the lang of the tweet or the user lang if the tweet lang is "und" (unknown)
	 *
	 * @return
	 */
	public String getTweetOrUserLang() {
		return lang.equals("und") ? user.lang : lang;
	}

	@Override
	public String toString() {
		return "Tweet{" +
				"text='" + text.replaceAll("[\\r\\n]+", " ") + '\'' +
				", created_at=" + created_at +
				", hashtags=" + hashtags +
				", id=" + id +
				", source='" + source + '\'' +
				", in_reply_to_status_id=" + in_reply_to_status_id +
				", in_reply_to_user_id=" + in_reply_to_user_id +
				", user=" + user +
				", lon=" + lon +
				", lat=" + lat +
				", retweet_count=" + retweet_count +
				", favorite_count=" + favorite_count +
				", lang='" + lang + '\'' +
				'}';
	}
}
