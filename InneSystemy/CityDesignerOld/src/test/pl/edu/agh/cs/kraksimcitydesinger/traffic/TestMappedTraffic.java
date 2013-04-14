package test.pl.edu.agh.cs.kraksimcitydesinger.traffic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.MappedTraffic;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileLine;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileParser;

public class TestMappedTraffic {
	@Test public void TestOneTimeSlot() {
	    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    TrafficFileLine[] lines = parser.getTrafficLines();
	    String[] orderNodes = parser.getStreets();
	    
	    MappedTraffic mapped = new MappedTraffic(lines, orderNodes, 55, 55);
	    
	    Assert.assertEquals(5 + 5 + 287 + 46 +12 +38, mapped.getInTraffic("0	29 Listopada Prandoty"));
	    Assert.assertEquals(8 + 4 + 3, mapped.getInTraffic("1	Wilenska"));
	    Assert.assertEquals(29 + 206 + 8, mapped.getInTraffic("2	29 Listopada Opolska"));
	    Assert.assertEquals(48 + 1 + 32, mapped.getInTraffic("3	Wilenska"));
	    
	    Assert.assertEquals(8 + 206 + 32, mapped.getOutTraffic("0	29 Listopada Prandoty"));
	    Assert.assertEquals(6 + 38 + 29 + 1, mapped.getOutTraffic("1	Wilenska"));
	    Assert.assertEquals(5 + 287 + 46 +6 +3 + 48, mapped.getOutTraffic("2	29 Listopada Opolska"));
	    Assert.assertEquals(5 + 4 + 8, mapped.getOutTraffic("3	Wilenska"));
	}
	
	@Test public void TestMappAllFiles() {
		for(String fileName : new File(TestTrafficFileParser.rootDirectoryPath).list()) {
		    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/" + fileName);
		    TrafficFileLine[] lines = parser.getTrafficLines();
		    String[] orderNodes = parser.getStreets();
		    
		    MappedTraffic mapped = new MappedTraffic(lines, orderNodes, 55, 55);
		} 
	}
	
	@Test public void TestTranslateStraight() {
	    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    TrafficFileLine[] lines = parser.getTrafficLines();
	    String[] orderNodes = new String[] {
		    "0	29 Listopada Prandoty",
		    "1	Wilenska",
		    "2	29 Listopada Opolska",
		    "3	Wilenska"
	    };
	    
	    
	    MappedTraffic mapped = new MappedTraffic(lines, orderNodes, 55, 55);
	    
	    Assert.assertEquals("0	29 Listopada Prandoty", mapped.translate("2	29 Listopada Opolska", TrafficFileLine.DIRECTION_STRAIGHT));
	}
	
	@Test public void TestTranslateLeft() {
	    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    TrafficFileLine[] lines = parser.getTrafficLines();
	    String[] orderNodes = new String[] {
		    "0	29 Listopada Prandoty",
		    "1	Wilenska",
		    "2	29 Listopada Opolska",
		    "3	Wilenska"
	    };
	    
	    
	    MappedTraffic mapped = new MappedTraffic(lines, orderNodes, 55, 55);
	    
	    Assert.assertEquals("0	29 Listopada Prandoty", mapped.translate("3	Wilenska", TrafficFileLine.DIRECTION_LEFT));
	}
	
	@Test public void TestTranslateRight() {
	    TrafficFileParser parser = new TrafficFileParser(TestTrafficFileParser.rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    TrafficFileLine[] lines = parser.getTrafficLines();
	    String[] orderNodes = new String[] {
		    "0	29 Listopada Prandoty",
		    "1	Wilenska",
		    "2	29 Listopada Opolska",
		    "3	Wilenska"
	    };
	    
	    
	    MappedTraffic mapped = new MappedTraffic(lines, orderNodes, 55, 55);
	    
	    Assert.assertEquals("2	29 Listopada Opolska", mapped.translate("3	Wilenska", TrafficFileLine.DIRECTION_RIGHT));
	}
}
