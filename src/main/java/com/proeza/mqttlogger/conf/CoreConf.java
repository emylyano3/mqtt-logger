package com.proeza.mqttlogger.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.proeza.common.config.annotation.Property;
import com.proeza.common.config.core.AbstractPropertiesConfiguration;
import com.proeza.common.config.factory.ConfigFactory;
import com.proeza.common.config.factory.IConfigFactory;
import com.proeza.mqttlogger.parser.Payload2StringParser;

public class CoreConf extends AbstractPropertiesConfiguration {

	public static final String		CLIENT_ID				= "conf.client.id";
	public static final String		LOG_TOPICS				= "conf.log.topics";
	public static final String		LOG_OUTPUT				= "conf.log.output";
	public static final String		LOG_FILE				= "conf.log.file";
	public static final String		LOG_PATTERN_LAYOUT		= "conf.log.patternlayout";
	public static final String		LOG_PAYLOAD_SPLITTER	= "conf.log.payload.splitter";
	public static final String		LOG_PAYLOAD_FORMAT		= "conf.log.payload.format";
	public static final String		LOG_PAYLOAD_PARSER		= "conf.log.payload.parser";
	public static final String		CONNECTION_PROTOCOL		= "conf.connection.protocol";
	public static final String		CONNECTION_TO			= "conf.connection.timeout";
	public static final String		CLEAN_SESSION			= "conf.connection.cleansession";
	public static final String		AUTO_RECONNECT			= "conf.connection.autoreconnect";
	public static final String		BROKER_HOST				= "conf.broker.host";
	public static final String		BROKER_PORT				= "conf.broker.port";

	private static Logger			log						= Logger.getLogger(CoreConf.class);

	private static IConfigFactory	configFactory			= new ConfigFactory();

	private static CoreConf			instance;

	@Property(name = LOG_TOPICS)
	private String					logTopics;

	@Property(name = LOG_PAYLOAD_FORMAT)
	private String					payloadFormat;

	@Property(name = LOG_PAYLOAD_SPLITTER)
	private String					payloadSplitter;

	@Property(name = LOG_FILE)
	private String					logFile;

	@Property(name = LOG_PATTERN_LAYOUT)
	private String					logPatternLayout;

	@Property(name = LOG_PAYLOAD_PARSER)
	private String					logPayloadParser;

	@Property(name = BROKER_HOST)
	private String					brokerHost;

	@Property(name = BROKER_PORT)
	private String					brokerPort;

	@Property(name = CONNECTION_PROTOCOL)
	private String					connectionProtocol;

	@Property(name = CLIENT_ID)
	private String					clientID;

	@Property(name = CLEAN_SESSION)
	private Boolean					cleanSession			= true;

	@Property(name = AUTO_RECONNECT)
	private Boolean					automaticReconnect		= true;

	public static CoreConf getInstance () {
		return instance;
	}

	public static CoreConf getInstance (String configFilePath) {
		if (instance == null) {
			if (configFilePath == null) {
				throw new IllegalArgumentException("The configuration file path cannot be null");
			}
			InputStream is = CoreConf.class.getResourceAsStream(configFilePath);
			if (is == null) {
				File confFile = new File(configFilePath);
				if (confFile.isDirectory()) {
					throw new IllegalArgumentException("The specified destination is not a file: " + confFile.getAbsolutePath());
				}
				if (!confFile.exists()) {
					throw new IllegalArgumentException("The configuration file does not exist or cannot be reached: " + confFile.getAbsolutePath());
				}
				try {
					is = new FileInputStream(confFile);
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException("The configuration file does not exist or cannot be reached: " + configFilePath, e);
				}
			}
			instance = configFactory.createPropertiesConfig(is, new CoreConf());
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

	public String getPayloadFormat () {
		return this.payloadFormat;
	}

	public String getPayloadSplitter () {
		return this.payloadSplitter;
	}

	public String getLogFile () {
		return this.logFile;
	}

	public String getLogPatternLayout () {
		return this.logPatternLayout;
	}

	public Object getPayloadParser () {
		try {
			return Class.forName(this.logPayloadParser).newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			return new Payload2StringParser();
		}
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
