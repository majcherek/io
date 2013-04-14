package test.pl.edu.agh.cs.kraksimcitydesinger.traffic;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileLine;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.TrafficFileParser;


public class TestTrafficFileParser {
	public static String rootDirectoryPath = "testy/Dane-ZDiK/";
	@Test public void TestParseAllZdikFiles() {
	    for(String fileName : new File(rootDirectoryPath).list())
	    	new TrafficFileParser(rootDirectoryPath + "/" + fileName);
	}
	
	@Test public void TestNumberOfTrafficLines() {
	    TrafficFileParser parser = new TrafficFileParser(rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    Assert.assertEquals(144, parser.getTrafficLines().length);
	}
	
	@Test public void TestNumberOfTrafficLineDirection() {
	    TrafficFileParser parser = new TrafficFileParser(rootDirectoryPath + "/29 Listopada - Wilenska Junction.txt");
	    Assert.assertEquals(TrafficFileLine.DIRECTION_RIGHT, parser.getTrafficLines()[0].getDirection());
	}
}
