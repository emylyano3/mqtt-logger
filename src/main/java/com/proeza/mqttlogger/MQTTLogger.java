package com.proeza.mqttlogger;

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
	private CoreConf coreConf;

	public MQTTLogger (String configFilePath) {
		super();
		this.coreConf = CoreConf.getInstance(configFilePath);
	}

	private IMqttClient client;

	public void connect () throws MqttException {
		this.client = new MqttClient(this.coreConf.getBrokerURL(), this.coreConf.getClientID());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(this.coreConf.isAutoReconnect());
		options.setCleanSession(this.coreConf.isCleanSession());
		options.setConnectionTimeout(this.coreConf.getConnectionTimeout());
		this.client.connect(options);
	}

	public void subscribe () throws MqttSecurityException, MqttException, InterruptedException {
		for (String topic : this.coreConf.getLogTopics()) {
			this.client.subscribe(topic, new MessageListener());
		}
	}

	public void disconnect () throws MqttException {
		this.client.disconnect();
	}

	class MessageListener implements IMqttMessageListener {
		private StringLogger			persistence		= new StringLogger();
		private IMqttPayloadParser<?>	payloadParser	= (IMqttPayloadParser<?>) MQTTLogger.this.coreConf.getPayloadParser();

		@Override
		public void messageArrived (String topic, MqttMessage message) throws Exception {
			this.persistence.persist(this.payloadParser.parse(message.getPayload()));
		}
	}
}
