package traffic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TrafficContentGenerator {
	private Document document;

	public TrafficContentGenerator(Document document) {
		super();
		this.document = document;
	}

	public void createDocument(Set<String> gateways) {
		Element traffic = createTraffic();
		createAllToAllSchemes(traffic, gateways);
	}

	private void createAllToAllSchemes(Element traffic, Set<String> gateways) {
		for (String sourceGateway : gateways) {
			for (String destinationGateway : gateways) {
				createScheme(traffic, sourceGateway, destinationGateway);
			}
		}
	}

	private void createScheme(Element traffic, String sourceGateway,
			String destinationGateway) {
		if (!sourceGateway.equals(destinationGateway)) {
			String startTurn = "0";
			String endTurn = "240000";

//			String trafficValue = "1000";
//			String trafficValue = trafficForKazimierz(sourceGateway,
//					destinationGateway);
//			String trafficValue = trafficForKrakow(sourceGateway,
//					destinationGateway);
			String trafficValue = trafficFor6x6(sourceGateway,
					destinationGateway);
			createSchemeElement(traffic, sourceGateway, destinationGateway,
					trafficValue, startTurn, endTurn);
		}
	}
	
	
	private String trafficFor6x6(String sourceGateway, String destinationGateway) {
		Set<String> a = new HashSet<String>(Arrays.asList(new String[] {
				"N3", "S4"}));
		Set<String> b = new HashSet<String>(Arrays.asList(new String[] {
				"W4", "E3"}));
		
		
		
		if (a.contains(sourceGateway) && a.contains(destinationGateway)) {
			return "25000";
		} else if (b.contains(sourceGateway) && b.contains(destinationGateway)) {
			return "25000";
		} else {
			return "600";
		}
	}

	private String trafficForKrakow(String sourceGateway,
			String destinationGateway) {
		Set<String> a = new HashSet<String>(Arrays.asList(new String[] {
				"G7", "G10", "G11", "G12" }));
		Set<String> b = new HashSet<String>(Arrays.asList(new String[] {
				"G10", "G11", "G12"}));
		Set<String> c = new HashSet<String>(Arrays.asList(new String[] {
				"G0", "G7", "G8", "G9"}));
		
		
		
		if (c.contains(sourceGateway) && c.contains(destinationGateway)) {
			return "3000";
		} else if ("G12".equals(sourceGateway) && "G13".equals(destinationGateway) || "G13".equals(sourceGateway) && "G12".equals(destinationGateway)) {
			return "12000";
		} else if ("G9".equals(sourceGateway) || "G9".equals(destinationGateway)) {
			return "1000";
		} else if ("G4".equals(sourceGateway) && a.contains(destinationGateway) || a.contains(sourceGateway) && "G4".equals(destinationGateway)) {
			return "350";
		} else if ("G1".equals(sourceGateway) && b.contains(destinationGateway) || b.contains(sourceGateway) && "G1".equals(destinationGateway)) {
			return "1200";
		}  else {
			return "3600";
		}
	}

	private String trafficForKazimierz(String sourceGateway,
			String destinationGateway) {
		Set<String> big = new HashSet<String>(Arrays.asList(new String[] {
				"G0", "G5", "G12", "G13" }));
		Set<String> small = new HashSet<String>(Arrays.asList(new String[] {
				"G2", "G3", "G4", "G5", "G6", "G10" }));

		if (big.contains(sourceGateway) && big.contains(destinationGateway)) {
			return "10000";
		} else if (small.contains(sourceGateway)
				&& small.contains(destinationGateway)) {
			return "1000";
		} else {
			return "3000";
		}
	}

	private void createSchemeElement(Element traffic, String source,
			String destination, String count, String startTurn, String endTurn) {
		Element scheme = document.createElement("scheme");
		scheme.setAttribute("count", count);
		traffic.appendChild(scheme);

		Element sourceGateway = document.createElement("gateway");
		sourceGateway.setAttribute("id", source);
		scheme.appendChild(sourceGateway);

		Element uniform = document.createElement("uniform");
		uniform.setAttribute("a", startTurn);
		uniform.setAttribute("b", endTurn);
		sourceGateway.appendChild(uniform);

		Element destinationGateway = document.createElement("gateway");
		destinationGateway.setAttribute("id", destination);
		scheme.appendChild(destinationGateway);
	}

	private Element createTraffic() {
		Element traffic = document.createElement("traffic");
		document.appendChild(traffic);
		return traffic;
	}

}
