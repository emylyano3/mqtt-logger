package com.proeza.mqttlogger;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.proeza.mqttlogger.conf.CoreConf;
import com.proeza.mqttlogger.parser.IMqttPayloadParser;
import com.proeza.mqttlogger.persistence.StringLogger;

public class MQTTLogger {

	private static final int	CONNECT_RETRY_TIME	= 5000;

	private static Logger		log					= Logger.getLogger(MQTTLogger.class);

	private CoreConf			coreConf;

	public MQTTLogger (String configFilePath) {
		super();
		this.coreConf = CoreConf.getInstance(configFilePath);
	}

	private IMqttClient client;

	public void connect () throws MqttException {
		log.info(String.format("Connecting to MQTT broker @%s:%s", this.coreConf.getBrokerHost(), this.coreConf.getBrokerPort()));
		this.client = new MqttClient(this.coreConf.getBrokerURL(), this.coreConf.getClientID());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(this.coreConf.isAutoReconnect());
		options.setCleanSession(this.coreConf.isCleanSession());
		options.setConnectionTimeout(this.coreConf.getConnectionTimeout());
		int retries = 0;
		boolean retry = false;
		do {
			retry = ++retries <= this.coreConf.getConnectionRetries();
			try {
				this.client.connect(options);
				Thread.sleep(CONNECT_RETRY_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				log.error(String.format("Cannot connect to MQTT server. URL: ", this.coreConf.getBrokerURL()), e);
				if (retry) {
					log.info(String.format("Retrying %s of %s in %s seconds", retries, this.coreConf.getConnectionRetries(), CONNECT_RETRY_TIME / 1000));
				} else {
					log.info("No retrying");
				}
			}
		} while (retry && !this.client.isConnected());
		log.info(String.format("Connected to MQTT broker @%s:%s", this.coreConf.getBrokerHost(), this.coreConf.getBrokerPort()));
	}

	public void subscribe () throws MqttSecurityException, MqttException, InterruptedException {
		log.info("Subscribing topics");
		for (String topic : this.coreConf.getLogTopics()) {
			log.info(String.format("Topic subscribed: %s", topic));
			this.client.subscribe(topic, new MessageListener());
		}
	}

	public void disconnect () throws MqttException {
		log.info(String.format("Disconnecting from MQTT broker @%s:%s", this.coreConf.getBrokerHost(), this.coreConf.getBrokerPort()));
		this.client.disconnect();
		log.info(String.format("Disconnected from MQTT broker @%s:%s", this.coreConf.getBrokerHost(), this.coreConf.getBrokerPort()));
	}

	class MessageListener implements IMqttMessageListener {
		private StringLogger			persistence		= new StringLogger();
		private IMqttPayloadParser<?>	payloadParser	= (IMqttPayloadParser<?>) MQTTLogger.this.coreConf.getPayloadParser();

		@Override
		public void messageArrived (String topic, MqttMessage message) throws Exception {
			log.info(String.format("Message received from topic %s", topic));
			this.persistence.persist(this.payloadParser.parse(message.getPayload()));
		}
	}
}
