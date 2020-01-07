package parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.proeza.mqttlogger.conf.CoreConf;
import com.proeza.mqttlogger.parser.Payload2SimpleLogParser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleLogParserTest {

	@Mock
	private CoreConf				conf;

	@InjectMocks
	private Payload2SimpleLogParser	parser;

	@Before
	public void defineMocksBehaviour () {
		when(this.conf.getPayloadSplitter()).thenReturn("\\|");
		when(this.conf.getPayloadFormat()).thenReturn("10|20|30|*");
	}

	@Test
	public void parse () {
		String parsed = this.parser.parse("234345|192.168.0.84|Riego invernadero|Riego on".getBytes());
		assertEquals("234345    192.168.0.84        Riego invernadero             Riego on", parsed);
	}
}
