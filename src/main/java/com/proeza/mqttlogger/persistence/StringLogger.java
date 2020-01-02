package com.proeza.mqttlogger.persistence;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class StringLogger implements IMqttPayloadPersistence<String> {
	private static Logger logger = Logger.getLogger(StringLogger.class);

	static {
		FileAppender fa = new FileAppender();
		fa.setName("mqtt-messages");
		fa.setFile("mqtt-messages.log");
		fa.setLayout(new PatternLayout("%m%n"));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();
		Logger.getLogger(StringLogger.class).addAppender(fa);
	}

	@Override
	public void persist (String payload) {
		logger.info(payload);
	}
}
