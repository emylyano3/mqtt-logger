package conf;

import org.junit.Test;

import com.proeza.mqttlogger.conf.CoreConf;
import com.proeza.mqttlogger.parser.Payload2SimpleLogParser;

import static org.junit.Assert.*;

public class CoreConfTest {

	private CoreConf conf = CoreConf.getInstance("/core-conf-win-test.properties");

	@Test
	public void coreConftest () {
		assertEquals("tcp://192.168.0.101:1883", this.conf.getBrokerURL());
		assertEquals(10, this.conf.getConnectionTimeout());
		assertEquals("/domotic/log/", this.conf.getLogTopics().get(0));
		assertEquals("/brickland92", this.conf.getLogTopics().get(1));
		assertEquals("mqtt-messages.log", this.conf.getLogFile());
		assertEquals("10|20|30|*", this.conf.getPayloadFormat());
		assertEquals(true, this.conf.isCleanSession());
		assertEquals(true, this.conf.isAutoReconnect());
		assertEquals(new Payload2SimpleLogParser().getClass().getName(), this.conf.getPayloadParser().getClass().getName());
	}
}
