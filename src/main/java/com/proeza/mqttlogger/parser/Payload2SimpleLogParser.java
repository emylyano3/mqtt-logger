package com.proeza.mqttlogger.parser;

import com.proeza.mqttlogger.conf.CoreConf;

public class Payload2SimpleLogParser implements IMqttPayloadParser<String> {

	private static final String	PAD				= " ";
	private static final String	SIZE_WILDCARD	= "*";

	private CoreConf			conf			= CoreConf.getInstance();

	@Override
	public String parse (byte[] payload) {
		String sPayload = new String(payload);
		String[] chunks = sPayload.split(this.conf.getPayloadSplitter());
		String[] chunksSize = this.conf.getPayloadFormat().split(this.conf.getPayloadSplitter());
		if (chunksSize.length != chunks.length) {
			return sPayload;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < chunksSize.length; ++i) {
			int chunkSize = getChunkSize(chunksSize[i]);
			for (int j = 0; j < chunkSize; ++j) {
				if (j < chunks[i].length()) {
					builder.append(chunks[i].charAt(j));
				} else {
					builder.append(PAD);
				}
			}
		}
		return builder.toString().trim();
	}

	private short getChunkSize (String chunk) {
		if (SIZE_WILDCARD.equals(chunk)) {
			return Short.SIZE;
		} else {
			return Short.parseShort(chunk);
		}
	}
}
