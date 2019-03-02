package de.hhu.bigdata.twitter.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	// the user id
	public String id;

	// the name the user identifies as
	public String name;

	// the unique screen_name
	public String screen_name;

	// the location string, not necessarily a location
	public String location;

	// users account description
	public String description;

	// flag for verified users
	public boolean verified;

	// the follower count of the user
	public int followers_count;

	// friends count / their follows
	public int friends_count;

	// the number of tweets the user issued
	public int statuses_count;

	// the date the account was created
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy", locale = "en")
	public Date created_at;

	// the BCP47 code for the user's self declared interface language
	public String lang;

	// hexadecimal for the users background color
	public String profile_background_color;

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", screen_name='" + screen_name + '\'' +
				", location='" + location + '\'' +
				", description='" + (description != null ?description.replaceAll("[\\r\\n]+", " "): "") + '\'' +
				", verified=" + verified +
				", followers_count=" + followers_count +
				", friends_count=" + friends_count +
				", statuses_count=" + statuses_count +
				", created_at=" + created_at +
				", lang='" + lang + '\'' +
				", profile_background_color='" + profile_background_color + '\'' +
				'}';
	}
}