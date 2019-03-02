package de.hhu.bigdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Auth {
	private static Logger log = LoggerFactory.getLogger(Auth.class);

	public static Properties getAuth() {
		Properties props = new Properties();
		try (InputStream twitterProps = Auth.class.getResourceAsStream("/twitter.properties")) {
			props.load(twitterProps);
		} catch (IOException e) {
			log.error("Cannot load twitter properties", e);
		}

		return props;
	}
}
