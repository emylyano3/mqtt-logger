package com.proeza.mqttlogger.persistence;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.proeza.mqttlogger.conf.CoreConf;

public class StringLogger implements IMqttPayloadPersistence<Object> {
	private static Logger logger = Logger.getLogger(StringLogger.class);

	static {
		CoreConf conf = CoreConf.getInstance();
		FileAppender fa = new FileAppender();
		fa.setName(StringLogger.class.getName());
		fa.setFile(conf.getLogFile());
		fa.setLayout(new PatternLayout(conf.getLogPatternLayout()));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();
		Logger.getLogger(StringLogger.class).addAppender(fa);
	}

	@Override
	public void persist (Object payload) {
		logger.info(payload);
	}
}
