/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.graph;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * Klasa reprezentująca graf
 * @author Tomasz Adamski
 *
 */
public class Graph {
	private Vector<Vertex> verticles = new Vector<Vertex>();

	/**
	 * Funkcja dodająca wierzcholek do grafu
	 * @param v dodawany wierzcholek
	 */
	public void addVertex(Vertex v) {
		verticles.add(v);
	}

	
	/**
	 * @return lista numerow wierzcholkow, ktore sa bramami
	 */
	public List<Integer> gates() {
		List<Integer> gates = new LinkedList<Integer>();
		for (Vertex v : verticles)
			if (v.isGate())
				gates.add(new Integer(v.getNumber()));
		return gates;
	}

	/**
	 * Funkcja resetujaca wszystkie wierzcholki 
	 * (wartosci najkrotszej trasy oraz poprzednika dla kazdego wierzcholka).
	 */
	public void reset() {
		for (Vertex v : verticles)
			v.reset();
	}

	
	/**
	 * Funkcja zwracajaca wierzcholek o zadanym numerze
	 * @param v numer wierzcholka
	 * @return wierzcholek
	 */
	public Vertex getVerticle(int v) {
		if (v == -1)
			return null;
		return verticles.get(v);
	}

	public int size() {
		return verticles.size();
	}

	
	/**
	 * Funkcja drukujaca ruch na wszystkich krawedziach grafu 
	 */
	public void printTraffic() {
		for (Vertex v : verticles) {
			System.out.println("Wierzcholek " + v);
			for (Edge e : v.getEdges()) {
				System.out.println("\t z " + v + " do " + e.getDestination() + " traffic " + e.getTraffic() + " input "
						+ e.getInputCars() + " output " + e.getOutputCars() + " avg "
						+ (e.getInputCars() + e.getOutputCars()) / 2 + " dst " + e.getDistance());
			}

		}
	}
	
	
	/**
	 * Funkcja obliczajaca calkowity blad - roznice miedzy danymi a otrzymanym ruchem
	 * @return blad
	 */
	public double calculateError(){
		double error=0.0;
		int edges=0;
		for(Vertex v: verticles){
			for(Edge e: v.getEdges()){
				double avg=(e.getInputCars()+e.getOutputCars())/2.0;
				if(avg==0.0)continue;
				error+=Math.abs(e.getTraffic()-avg)/avg;
				edges++;
			}
		}
		error/=edges;
		return error;
	}

	
	/**
	 * Funkcja pozwalajaca na wczytanie grafu z plika.
	 * @param filename - nazwa pliku
	 * @return wczytany graf
	 * @throws Exception
	 */
	public static Graph parseGraph(String filename) throws Exception {
		Graph graph = new Graph();

		Scanner input = new Scanner(new File(filename));
		int n = input.nextInt();
		System.out.println("graf sklada sie z " + n + " wierzcholkow");
		for (int i = 0; i < n; i++) {
			int gate = input.nextInt();
			boolean isGate;
			if (gate > 0)
				isGate = true;
			else
				isGate = false;
			System.out.println("Dodaje do grafu wierzcholek " + i + " z bramowatoscia: " + isGate);
			graph.addVertex(new Vertex(i, isGate));
		}
		int k = input.nextInt();
		for (int i = 0; i < k; i++) {
			int from = input.nextInt();
			int to = input.nextInt();
			System.out.println("Dodaje krawedz od " + from + " do " + to);
			int distance = input.nextInt();
			int inputCars = input.nextInt();
			int outputCars = input.nextInt();
			Edge e = new Edge();
			e.setDestination(graph.getVerticle(to));
			e.setDistance(distance);
			e.setInputCars(inputCars);
			e.setOutputCars(outputCars);
			graph.getVerticle(from).addEdge(e);
		}

		System.out.println("Wczytalem taki graf:");
		for (int i = 0; i < graph.size(); i++) {
			System.out.println("\n" + i + " ");
			for (Edge e : graph.getVerticle(i).getEdges())
				System.out.println("do: " + e.getDestination().getNumber() + " traffic: " + e.getInputCars());
		}

		return graph;
	}
}
