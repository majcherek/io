package pl.edu.agh.cs.kraksimcitydesigner.traffic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pl.edu.agh.cs.kraksimcitydesigner.element.Gateway;
import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection;
import pl.edu.agh.cs.kraksimcitydesigner.element.Node;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm.Algorithm;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm.GatePair;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Edge;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Graph;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Vertex;

/** 
 * Klasa jest odpowiedzialna za generowanie rozkladu ruchu
 * @author Tomek Adamski
 * 
 */
public class GenerateTraffic {
	/**
	 * rozmiar populacji
	 */
	public static int algorithmPopulationSize;
	/**
	 * liczba krokow algorytmu
	 */
	public static int algorithmSteps;
	/** To jest opis metody
	 * @param intersections lista analizowanych skrzyzowan
	 * @param nodes lista wezlow
	 * @param filename plik w ktorym zostanie zapisany rozklad ruchu
	 */
	public static void Generate(List<Intersection> intersections, List<Node> nodes,String filename) {
		System.out.println("ROZPOCZYNA SIE GENEROWANIE TEGO GRAFU: "+algorithmPopulationSize+" "+algorithmSteps);
		Graph graph = new Graph();
		HashMap<String, Integer> nodeToVertexMapping = new HashMap<String, Integer>();
		HashMap<Integer, String> vertexToNodeMapping = new HashMap<Integer, String>();
		int number = 0;
		for (Node n : nodes) {
			if (!nodeToVertexMapping.containsKey(n.getId())) {
				graph.addVertex(new Vertex(number, n instanceof Gateway));
				nodeToVertexMapping.put(n.getId(), new Integer(number));
				vertexToNodeMapping.put(new Integer(number), n.getId());
				number++;
			}
		}

		for (Node n : nodes) {
			for (Node neighbour : n.getReachableNodes()) {
				Edge e = new Edge();
				Vertex from = graph.getVerticle(nodeToVertexMapping.get(n.getId()));
				Vertex to = graph.getVerticle(nodeToVertexMapping.get(neighbour.getId()));
				e.setDestination(to);
				e.setDistance(n.calculateDistance(neighbour));
				from.addEdge(e);
			}
		}

		for (Intersection i : intersections) {
			if (i.hasTrafficInfo()) {
				for (Node n : i.getReachableNodes()) {
					Vertex from = graph.getVerticle(nodeToVertexMapping.get(i.getId()));
					Vertex to = graph.getVerticle(nodeToVertexMapping.get(n.getId()));
					int outputTraffic = i.getOutTraffic(n.getId());
					int inputTraffic = i.getInTraffic(n.getId());
					if (from.getEdgeTo(to) != null)
						from.getEdgeTo(to).setInputCars(outputTraffic);
					if (to.getEdgeTo(from) != null)
						to.getEdgeTo(from).setOutputCars(inputTraffic);
				}
			}
		}

		for (int i = 0; i < graph.size(); i++) {
			for (Edge e : graph.getVerticle(i).getEdges()) {
				if (e.getInputCars() == 0 && e.getOutputCars() > 0)
					e.setInputCars(e.getOutputCars());
				if (e.getOutputCars() == 0 && e.getInputCars() > 0)
					e.setOutputCars(e.getInputCars());
			}
		}

		System.out.println("Numery wierzcholkow:");
		System.out.println(nodeToVertexMapping);
		System.out.println("Tak wyglada mapa bram:");
		for (int i = 0; i < graph.size(); i++) {
			if (graph.getVerticle(i).isGate())
				System.out.print("1 ");
			else
				System.out.print("0 ");
			System.out.println();
		}
		System.out.println("Bede liczyl na takim grafie:");
		graph.printTraffic();

		Algorithm algorithm = new Algorithm(graph, algorithmPopulationSize);
		algorithm.perform(algorithmSteps);

		Document document = new Document();
		Element root = new Element("traffic");

		List<GatePair> gatePairs = algorithm.getGatePairs();
		for (int i = 0; i < gatePairs.size(); i++) {
			GatePair gatePair = gatePairs.get(i);
			int fromNumber = gatePair.getInputVertex();
			int toNumber = gatePair.getOutputVertex();
			String fromId = vertexToNodeMapping.get(new Integer(fromNumber));
			String toId = vertexToNodeMapping.get(new Integer(toNumber));
			Edge e = graph.getVerticle(fromNumber).getEdgeTo(graph.getVerticle(toNumber));

			Element scheme = new Element("scheme");
			scheme.setAttribute("count", new Long(Math.round(algorithm.getBest().getParameter(i))).toString());

			Element inputGateway = new Element("gateway");
			inputGateway.setAttribute("id", fromId);

			Element uniform = new Element("uniform");
			uniform.setAttribute("a", "0");
			uniform.setAttribute("b", "900");

			inputGateway.addContent(uniform);

			scheme.addContent(inputGateway);

			Element outputGateway = new Element("gateway");
			outputGateway.setAttribute("id", toId);

			scheme.addContent(outputGateway);

			root.addContent(scheme);
		}
		document.addContent(root);

		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());

		try {
			FileWriter fw = new FileWriter(filename);
			outp.output(document, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
