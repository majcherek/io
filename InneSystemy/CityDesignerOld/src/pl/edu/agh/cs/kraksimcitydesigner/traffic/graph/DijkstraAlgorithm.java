/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm.GatePair;

/**
 * Klasa reprezentujaca algorytm Dijkstry
 * @author Tomasz Adamski
 * 
 */
public class DijkstraAlgorithm {
	private Graph graph;
	private PriorityQueue<RelaxationPair> pqueue;

	/**
	 * @param graph graf na którym algorytm zostanie wykonany
	 */
	public DijkstraAlgorithm(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Funkcja obliczajaca najkrotsze odleglosci od wierzcholka o zadanym numerze
	 * @param v - numer wierzcholka, od ktorego liczone beda odleglosci
	 * @return - lista takich par bram, w których pierwsza brama jest zadanym wierzcholkiem poczatkowym 
	 * (kazda taka para przechowuje najkrotsza sciezke miedzy tymi bramami)
	 */
	public List<GatePair> calculatePathsFromVertex(int v) {
		// System.out.println("Rozpoczyna sie obliczanie drog z wierzcholka "+v);
		graph.reset();
		// System.out.println("Zresetowalem graf");
		List<GatePair> gatePairs = new ArrayList<GatePair>();
		pqueue = new PriorityQueue<RelaxationPair>();
		RelaxationPair firstPair = new RelaxationPair(v, 0, -1);
		pqueue.add(firstPair);
		while (pqueue.size() > 0) {
			// System.out.println("Wchodze do łajla a w kolejce jest "+pqueue.size());
			RelaxationPair pair = pqueue.poll();
			Vertex vertex = graph.getVerticle(pair.getVertex());
			if (vertex.getShortestDistance() >= 0)
				continue;
			int distance = pair.getDistance();
			vertex.setShortestDistance(distance);
			vertex.setPredecessor(graph.getVerticle(pair.getPredecessor()));
			for (Edge e : graph.getVerticle(pair.getVertex()).getEdges()) {
				RelaxationPair newPair = new RelaxationPair(e.getDestination().getNumber(), distance + e.getDistance(),
						vertex.getNumber());
				pqueue.add(newPair);
			}
		}
		// System.out.println("Wyszedlem z łajla");
		for (int i = 0; i < graph.size(); i++) {
			Vertex vertex = graph.getVerticle(i);
			if (i == v || !vertex.isGate())
				continue;
			GatePair gatePair = new GatePair();
			gatePair.setInputVertex(v);
			gatePair.setOutputVertex(i);
			Path path = calculatePath(vertex);
			gatePair.setPath(path);
			gatePairs.add(gatePair);
		}
		// System.out.println("konczy sie obliczanie drogi z wierzcholka "+v);
		return gatePairs;
	}

	private Path calculatePath(Vertex vertex) {
		// System.out.println("Rozpoczyna sie obliczanie sciezki");
		Stack<Edge> stack = new Stack<Edge>();
		while (vertex.getPredecessor() != null) {
			stack.add(vertex.getPredecessor().getEdgeTo(vertex));
			vertex = vertex.getPredecessor();
		}
		Path path = new Path();
		while (!stack.empty()) {
			// System.out.println("Wchodze do drugiego łajla a na staku jest "+stack.size());
			path.addEdge(stack.pop());
		}
		// System.out.println("konczy sie obliczanie ścieżki");
		return path;
	}
}

/**
 * Klasa pomocnicza reprezentujaca pare wierzcholkow wraz z tymczasowa odlegloscia.
 * Jest uzywana w algorytmie Dijkstry.
 * @author Tomasz Adamski
 *
 */
class RelaxationPair implements Comparable<RelaxationPair> {

	private int vertex;
	private int distance;
	private int predecessor;

	public RelaxationPair(int vertex, int distance, int predecessor) {
		this.vertex = vertex;
		this.distance = distance;
		this.predecessor = predecessor;
	}

	public int getPredecessor() {
		return predecessor;
	}

	@Override
	public int compareTo(RelaxationPair rp) {
		if (this.distance < rp.distance)
			return -1;
		if (this.distance == rp.distance)
			return 0;
		return 1;
	}

	public int getDistance() {
		return distance;
	}

	public int getVertex() {
		return vertex;
	}

}
