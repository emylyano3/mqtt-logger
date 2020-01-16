package com.proeza.mqttlogger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	public static void main (String... args) {
		log.info("MQTT Logger App started");
		Options options = new Options();
		Option input = new Option("c", "confFile", true, "Defines the configuration file path");
		input.setRequired(true);
		options.addOption(input);
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			log.info("Arguments received");
			for (Option opt : cmd.getOptions()) {
				log.info(String.format("[OPT:%s] [LONG_OPT:%s] [VALUE:%s]", opt.getOpt(), opt.getLongOpt(), cmd.getOptionValue(opt.getOpt())));
			}
			MQTTLogger logger = new MQTTLogger(cmd.getOptionValue("c"));
			try {
				logger.connect();
				logger.subscribe();
				while (true) {
					Thread.sleep(100);
				}
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
