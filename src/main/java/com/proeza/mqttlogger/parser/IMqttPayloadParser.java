package com.proeza.mqttlogger.parser;

public interface IMqttPayloadParser<T> {
	T parse (byte[] payload);
}
