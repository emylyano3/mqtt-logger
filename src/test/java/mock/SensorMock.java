package mock;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorMock {

	private IMqttClient client;

	public static void main (String... args) throws Exception {
		SensorMock mock = new SensorMock();
		while (true) {
			mock.call();
			Thread.sleep(2000);
		}
	}

	public SensorMock () throws MqttException {
		this.client = new MqttClient(getBrokerURL(), UUID.randomUUID().toString());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		this.client.connect(options);
	}

	private String getBrokerURL () {
		return "tcp://192.168.0.101:1883";
	}

	public void call () throws Exception {
		if (this.client.isConnected()) {
			MqttMessage msg = readEngineTemp();
			msg.setQos(0);
			msg.setRetained(true);
			this.client.publish("/domotic/log/", msg);
		}
	}

	private MqttMessage readEngineTemp () {
		double temp = 80 + new Random().nextDouble() * 20.0;
		byte[] payload = String
			.format("T:%04.2f", temp)
			.getBytes();
		return new MqttMessage(payload);
	}
}
