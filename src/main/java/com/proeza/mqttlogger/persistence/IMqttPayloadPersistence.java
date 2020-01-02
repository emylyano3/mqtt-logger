package com.proeza.mqttlogger.persistence;

public interface IMqttPayloadPersistence<T> {
	void persist (T payload);
}
