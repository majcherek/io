package pl.edu.agh.cs.kraksimcitydesigner.traffic;

import java.util.List;

import pl.edu.agh.cs.kraksimcitydesigner.element.Intersection;
import pl.edu.agh.cs.kraksimcitydesigner.element.Node;

/**
 * Klasa sluzaca do analizy przeplywu ruchu miedzy skrzyzowaniami na mapie 
 * @author Pawel Pierzchala
 *
 */
public class TrafficAnalyser {
	/**
	 * Metoda generujaca raport przeplywow dla calej mapy na podstawie danych ZDKiA
	 * @param intersections lista skrzyzowan dla ktorych ma zostac przeprowadzona analiza
	 * @return tekstowy raport
	 */
	public static String AnalyzeTrafficFlow(List<Intersection> intersections) {
		TrafficAnalyser analyser = new TrafficAnalyser();
		String result = "";
		for(Intersection i : intersections) {			
			if (i.hasTrafficInfo()) {
				for(Node n : i.getReachableNodes()) {
					if (n instanceof Intersection) {
						Intersection i2 = (Intersection)n;
						if (i2.hasTrafficInfo()) {
							result += analyser.CompareIntersectionsTraffic(i, i2);
						}
					}
					
				}
			}
		}
		
		result += "\n Total Error: " + analyser.getTotalError();
		return result;
	}
		
	private double total;
	private int comparisons;
	
	private double getTotalError() {
		return total / comparisons;
	}
	
	private String CompareIntersectionsTraffic(Intersection a, Intersection b) {
		String result = "";
		result += a.getId() + " " + a.getTrafficFile() + "\n";
		result += b.getId() + " " + b.getTrafficFile() + "\n";
		
		result += a.getId() + " Out -> " + b.getId() + " : " + 
				a.getOutTraffic(b.getId()) + "\n";
		result += a.getId() + " -> In " + b.getId() + " : " + 
				b.getInTraffic(a.getId()) + "\n";
		double localA = Math.abs(b.getInTraffic(a.getId()) - a.getOutTraffic(b.getId())) / (double)(a.getOutTraffic(b.getId()) + b.getInTraffic(a.getId()));
		comparisons++;
		result += a.getId() + " -> Diff -> " + b.getId() + " : " + 
				Math.abs(b.getInTraffic(a.getId()) - a.getOutTraffic(b.getId())) + " Error: " + localA + "\n";
		result += "\n";
		result += a.getId() + " <- Out " + b.getId() + " : " + 
				b.getOutTraffic(a.getId()) + "\n";
		result += a.getId() + " In <- " + b.getId() + " : " + 
				a.getInTraffic(b.getId()) + "\n";
		double localB = (Math.abs(a.getInTraffic(b.getId()) - b.getOutTraffic(a.getId()))) / (double)(a.getOutTraffic(b.getId()) + b.getInTraffic(a.getId()));
		comparisons++;
		result += a.getId() + " <- Diff <- " + b.getId() + " : " + 
				Math.abs(a.getInTraffic(b.getId()) - b.getOutTraffic(a.getId())) + " Error: " + localB + "\n";
		result += "\n";
		
		total += localA + localB;
		
		return result;
	}

}
