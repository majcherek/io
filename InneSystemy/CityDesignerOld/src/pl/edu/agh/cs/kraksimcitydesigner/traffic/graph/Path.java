/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentujaca sciezke w grafie
 * @author Tomasz Adamski
 */
public class Path {
	private List<Edge> edges=new ArrayList<Edge>();
	
	
	/**
	 * Dodaj krawedz do sciezki
	 * @param edge - krawedz
	 */
	public void addEdge(Edge edge){
		edges.add(edge);
	}
	
	
	/**
	 * Pobierz i-ta krawedz sciezki
	 * @param i 
	 * @return krawedz
	 */
	public Edge getEdgeAt(int i){
		return edges.get(i);
	}
	
	public int size(){
		return edges.size();
	}
	
	public String toString(){
		return edges.toString();
	}
}
