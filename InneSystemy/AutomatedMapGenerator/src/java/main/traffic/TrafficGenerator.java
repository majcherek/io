package traffic;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import traffic.reader.TrafficReader;

public class TrafficGenerator {
	private static final String mapName = "ala.xml";
	private static final String trafficFileName = "alatraffic.xml";
	private Document document;
	
	public static void main(String[] args) {
		TrafficGenerator tg = new TrafficGenerator();
		tg.createDocument();
	}
	

	private TrafficGenerator() {
		super();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			document = db.newDocument();

		} catch (ParserConfigurationException pce) {
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
	}

	private void createDocument() {
		TrafficReader tr = new TrafficReader();
		Set<String> gateways = tr.read(mapName);
		TrafficContentGenerator cg = new TrafficContentGenerator(document);
		cg.createDocument(gateways);
		writeDocumentToFile();
	}

	private void writeDocumentToFile() {
		try {
			DOMImplementation domImplementation = document.getImplementation();
			if (domImplementation.hasFeature("LS", "3.0") && domImplementation.hasFeature("Core", "2.0")) {
				DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation.getFeature("LS",
						"3.0");
				LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
				DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
				if (domConfiguration.canSetParameter("format-pretty-print", Boolean.TRUE)) {
					lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
					LSOutput lsOutput = domImplementationLS.createLSOutput();
					lsOutput.setEncoding("UTF-8");
					StringWriter stringWriter = new StringWriter();
					lsOutput.setCharacterStream(stringWriter);
					lsSerializer.write(document, lsOutput);
					System.out.println(stringWriter.toString());
					
					System.out.println("tutut");
					PrintWriter pw = new PrintWriter(new File(trafficFileName));
					pw.write(stringWriter.toString());
					pw.flush();
					pw.close();
				} else {
					throw new RuntimeException("DOMConfiguration 'format-pretty-print' parameter isn't settable.");
				}
			} else {
				throw new RuntimeException("DOM 3.0 LS and/or DOM 2.0 Core not supported.");
			}

		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
