package test.pl.edu.agh.cs.kraksimcitydesinger.traffic;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileLine;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileParser;

public class TestTrafficFileLine {
	@Test public void TestNumberOfTrafficLineDirection() {
	    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    TrafficFileLine[] lines = parser.getTrafficLines();
	    Map<String, String> mapping = new HashMap<String, String>();
	    mapping.put("0	29 Listopada Prandoty", "A");
	    mapping.put("1	Wilenska", "B");
	    mapping.put("2	29 Listopada Opolska", "C");
	    mapping.put("3	Wilenska", "D");
	    
	    lines = TrafficFileLine.mapNames(lines, mapping);
	    Assert.assertEquals("A", lines[0].getFrom());
	}
}
