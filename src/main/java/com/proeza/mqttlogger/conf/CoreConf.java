package com.proeza.mqttlogger.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.proeza.common.config.annotation.Property;
import com.proeza.common.config.core.AbstractPropertiesConfiguration;
import com.proeza.common.config.factory.ConfigFactory;
import com.proeza.common.config.factory.IConfigFactory;

public class CoreConf extends AbstractPropertiesConfiguration {

	public static final String		CLIENT_ID			= "conf.client.id";
	public static final String		LOG_TOPICS			= "conf.log.topics";
	public static final String		LOG_OUTPUT			= "conf.log.output";
	public static final String		LOG_FORMAT			= "conf.log.format";
	public static final String		LOG_SPLITTER		= "conf.log.splitter";
	public static final String		CONNECTION_PROTOCOL	= "conf.connection.protocol";
	public static final String		CONNECTION_TO		= "conf.connection.timeout";
	public static final String		CLEAN_SESSION		= "conf.connection.cleansession";
	public static final String		AUTO_RECONNECT		= "conf.connection.autoreconnect";
	public static final String		BROKER_HOST			= "conf.broker.host";
	public static final String		BROKER_PORT			= "conf.broker.port";

	private static Logger			log					= Logger.getLogger(CoreConf.class);

	private static IConfigFactory	configFactory		= new ConfigFactory();

	private static CoreConf			instance;

	@Property(name = LOG_TOPICS)
	private String					logTopics;

	@Property(name = LOG_SPLITTER)
	private String					logSplitter;

	@Property(name = LOG_FORMAT)
	private String					logFormat;

	@Property(name = BROKER_HOST)
	private String					brokerHost;

	@Property(name = BROKER_PORT)
	private String					brokerPort;

	@Property(name = CONNECTION_PROTOCOL)
	private String					connectionProtocol;

	@Property(name = CLIENT_ID)
	private String					clientID;

	@Property(name = CLEAN_SESSION)
	private Boolean					cleanSession		= true;

	@Property(name = AUTO_RECONNECT)
	private Boolean					automaticReconnect	= true;

	public static CoreConf getInstance (String configFilePath) {
		if (instance == null) {
			if (configFilePath == null) {
				throw new IllegalArgumentException("The configuration file path cannot be null");
			}
			File confFile = new File(configFilePath);
			if (confFile.isDirectory()) {
				throw new IllegalArgumentException("The specified destination is not a file: " + confFile.getAbsolutePath());
			}
			if (!confFile.exists()) {
				throw new IllegalArgumentException("The configuration file does not exist or cannot be reached: " + confFile.getAbsolutePath());
			}
			try {
				instance = configFactory.createPropertiesConfig(new FileInputStream(confFile), new CoreConf());
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("The configuration file does not exist or cannot be reached: " + confFile.getAbsolutePath(), e);
			}
		}
		return instance;
	}

	private CoreConf () {

	}

	@Override
	protected void postInject (Properties loaded) {
		log.info("Config loaded");
		loaded.list(System.out);
	}

	public List<String> getLogTopics () {
		return getList(LOG_TOPICS);
	}

	public boolean isCleanSession () {
		return this.cleanSession;
	}

	public boolean isAutoReconnect () {
		return this.automaticReconnect;
	}

	public String getLogSplitter () {
		return this.logSplitter;
	}

	public String getLogFormat () {
		return this.logFormat;
	}

	public String getClientID () {
		return this.clientID;
	}

	public int getConnectionTimeout () {
		return getInt(CONNECTION_TO, 10);
	}

	/**
	 * protocol://host:port
	 */
	public String getBrokerURL () {
		return new StringBuilder()
			.append(getProperty(CONNECTION_PROTOCOL))
			.append("://")
			.append(getProperty(BROKER_HOST))
			.append(":")
			.append(getProperty(BROKER_PORT))
			.toString();
	}
}
