/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.swing.JProgressBar;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.DijkstraAlgorithm;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Edge;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Graph;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Path;
import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Vertex;

/**
 * Klasa reprezentujaca algorytm genetyczny generujacy 
 * obciazenie na podstawie zadanych danych statystycznych
 * @author Tomasz Adamski
 *
 */
public class Algorithm {
	private Graph graph;
	private Vector<GatePair> gatePairs = new Vector<GatePair>();
	private Vector<Individual> population;
	private int populationSize;
	private LinkedList<Individual> maternal;
	
	private double crossProbability = 0.8;
	private double mutationProbability = 0.1;

	private Random rand = new Random();
	
	private Individual best;

	/**
	 * @param graph - graf reprezentujacy siec drog
	 * @param populationSize - rozmiar populacji w algorytmie genetycznym
	 */
	public Algorithm(Graph graph, int populationSize) {
		this.graph = graph;
		this.populationSize=populationSize;
		population = new Vector<Individual>(populationSize);
	}

	private void initializePairs() {
		System.out.println("Rozpoczyna sie initializePairs");
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		for (int i = 0; i < graph.size(); i++) {
			if (!graph.getVerticle(i).isGate())
				continue;
			List<GatePair> gp = dijkstra.calculatePathsFromVertex(i);
			gatePairs.addAll(gp);
		}
		System.out.println("Wyszlo mi, ze sa takie pary wierzcholkow: ");
		for(GatePair pair : gatePairs){
			System.out.println(pair.getInputVertex()+" -> "+pair.getOutputVertex()+" "+pair.getPath());
		}
		System.out.println("Konczy sie initialize pairs");
	}

	private void initializePopulation() {
		System.out.println("w populacji kazdy czlonek ma "+gatePairs.size()+" chromosomów");
		for (int i = 0; i < populationSize; i++)
			population.add(i, new Individual(gatePairs.size()));
	}

	private void calculateFitnessFunction(Individual indv) {
		graph.reset();
		for (int i = 0; i < indv.size(); i++) {
			Path path = gatePairs.get(i).getPath();
			for (int j = 0; j < path.size(); j++)
				path.getEdgeAt(j).increaseTraffic(indv.getParameter(i));
		}
		double totalError = 0;
		for (int i = 0; i < graph.size(); i++) {
			Vertex vertex = graph.getVerticle(i);
			for (Edge e : vertex.getEdges()) {
				double avg = (e.getInputCars() + e.getOutputCars()) / 2.0;
				double traffic = e.getTraffic();
				double error = Math.abs(avg - traffic);
				if(avg<=0.0)error=0.0;
				totalError += error;
			}
		}
		double fitness = 1.0 / totalError;
		indv.setFitness(fitness);

	}

	private void calculateFitnessFunctions() {
		for (Individual indv : population)
			calculateFitnessFunction(indv);
	}

	private Individual pickOneIndividual() {
		Individual a = population.get(rand.nextInt(population.size()));
		Individual b = population.get(rand.nextInt(population.size()));
		if (a.getFitness() > b.getFitness())
			return a;
		return b;
	}

	private void createMaternal() {
		maternal = new LinkedList<Individual>();
		for (int i = 0; i < population.size(); i++) {
			Individual a = pickOneIndividual();
			Individual b = pickOneIndividual();
			if (a.getFitness() > b.getFitness()) {
				maternal.add(a);
			} else {
				maternal.add(b);
			}
		}
	}

	private void cross() {
		Vector<Individual> newPopulation = new Vector<Individual>();
		while (maternal.size() > 0) {
			Individual parentOne = maternal.remove(rand
					.nextInt(maternal.size()));
			Individual parentTwo = maternal.remove(rand
					.nextInt(maternal.size()));
			if (rand.nextDouble() < crossProbability) {
				Individual childOne = parentOne.crossWith(parentTwo);
				Individual childTwo = parentOne.crossWith(parentTwo);
				newPopulation.add(childOne);
				newPopulation.add(childTwo);
			} else {
				newPopulation.add(parentOne);
				newPopulation.add(parentTwo);
			}
		}
		population = newPopulation;
	}

	private void mutate() {
		// do napisania
	}

	private Individual selectWinner() {
		for (Individual i : population) {
			if (best == null || best.getFitness() < i.getFitness())
				best = i;
		}
		return best;
	}
	
	/**
	 * Zwraca osobnika reprezentujacego najlepsze rozwiazanie
	 * @return najlepszy osobnik
	 */
	public Individual getBest(){
		return best;
	}
	
	public List<GatePair> getGatePairs(){
		return gatePairs;
	}

	
	/**
	 * Funkcja wykonujqca algorytm dla zadanej ilosci krokow
	 * @param steps ilosc krokow
	 */
	public void perform(int steps) {
		initializePairs();
		System.out.println("Zainicjowalem pary");
		initializePopulation();
		System.out.println("Zainicjowalem populacje w ktorej teraz jest "+population.size());
		while ((steps--) > 0) {
			if(steps%1000==0){
				System.out.println("Pozostalo jeszcze "+steps+" kroków algorytmu");
			}
			calculateFitnessFunctions();
			createMaternal();
			cross();
		}
		Individual best=selectWinner();
		//best.removeToHigh();
		System.out.println("\n\n NAJLEPSZY JEST TAKI GOSC:");
		System.out.println(best.toString());
		System.out.println("A JEGO FITNESS TO:");
		calculateFitnessFunction(best);
		graph.printTraffic();
		System.out.println("ŚREDNI ERROR WYSZEDŁ:");
		System.out.println(graph.calculateError());
	}

	/*static public void main(String args[]) {
		try {
			Graph graph = Graph
					.parseGraph("/home/tomek/workspace/KraksimTrafficGraph/src/kazimierz.in");
			Algorithm algorithm = new Algorithm(graph, 1000);
			algorithm.perform(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
