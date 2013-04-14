/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.graph;

import java.util.LinkedList;
import java.util.List;

/**
 * Klasa reprezentujaca wierzcholek grafu
 * @author Tomasz Adamski
 *
 */
public class Vertex {
	
	private List<Edge> edges=new LinkedList<Edge>();
	private int number;
	private boolean gate;
	
	private int shortestDistance;
	private Vertex predecessor;
	
	public int getShortestDistance() {
		return shortestDistance;
	}

	public void setShortestDistance(int shortestDistance) {
		this.shortestDistance = shortestDistance;
	}

	public Vertex getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Vertex predecessor) {
		this.predecessor = predecessor;
	}

	public Vertex(int number,boolean gate){
		this.number=number;
		this.gate=gate;
	}
	
	public void addEdge(Edge edge){
		edges.add(edge);
	}
	
	public int getNumber() {
		return number;
	}

	public boolean isGate(){
		return gate;
	}
	
	/**
	 * Resetuje najkrótszy dystans i poprzednika wierzchołka 
	 */
	public void reset(){
		shortestDistance=-1;
		predecessor=null;
		for(Edge e: edges)e.reset();
	}
	
	public List<Edge> getEdges(){
		return edges;
	}
	
	
	/**
	 * Zwraca krawedz do zadanego wierzcholka
	 * @param v - wierzcholek
	 * @return krawedz
	 */
	public Edge getEdgeTo(Vertex v){
		for(Edge e:edges)if(e.getDestination()==v)return e; 
		return null;
	}
	
	public String toString(){
		return new Integer(number).toString();
	}
}
