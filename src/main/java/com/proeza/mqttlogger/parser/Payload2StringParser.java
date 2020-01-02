package com.proeza.mqttlogger.parser;

public class Payload2StringParser implements IMqttPayloadParser<String> {

	@Override
	public String parse (byte[] payload) {
		return new String(payload);
	}
}
