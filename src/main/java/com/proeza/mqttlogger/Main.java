package com.proeza.mqttlogger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main (String... args) {
		Options options = new Options();
		Option input = new Option("c", "confFile", true, "Defines the configuration file path");
		input.setRequired(true);
		options.addOption(input);
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			MQTTLogger logger = new MQTTLogger(cmd.getOptionValue("c"));
			try {
				logger.connect();
				logger.subscribe();
				while (true) {}
			} catch (Exception e) {
				System.err.println(e);
				System.exit(1);
			}
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			System.err.println(e.getMessage());
			formatter.printHelp("MQTT Logger", options);
			System.exit(1);
		}
	}
}
