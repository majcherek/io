package traffic.reader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TrafficReader {
	public Set<String> read(String name) {
		Document document = parseXmlFile(name);
		return findGateways(document);	
	}

	private Set<String> findGateways(Document document) {
		Element roadNet = document.getDocumentElement();
		NodeList nodesList = roadNet.getElementsByTagName("nodes");
		Element nodes = (Element) nodesList.item(0);
		NodeList gatewayList = nodes.getElementsByTagName("gateway");
		
		Set<String> gatewayNames = new HashSet<String>();
		for(int i = 0; i < gatewayList.getLength(); i++) {
			Element gateway = (Element) gatewayList.item(i);
			gatewayNames.add(gateway.getAttribute("id"));
		}

		return gatewayNames;
	}

	private Document parseXmlFile(String name) {
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			return db.parse(name);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}
