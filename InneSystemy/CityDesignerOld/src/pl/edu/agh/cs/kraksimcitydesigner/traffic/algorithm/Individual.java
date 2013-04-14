/*Author: Tomasz*/
package pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm;

import java.util.Random;
import java.util.Vector;

/**
 * Klasa reprezentujaca pojedynczego osobnika w populacji
 * @author Tomasz Adamski
 *
 */
public class Individual {
	private Vector<Double> parameters;
	private Random rand;
	
	private double fitness;
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * @param n - ilosc par bram
	 */
	public Individual(int n) {
		//System.out.println("POWSTAJE KOLEJNY INDIVIDUAL");
		parameters=new Vector<Double>(n);
		rand=new Random();
		for(int i=0;i<n;i++)parameters.add(rand.nextDouble());//no tu oczywiscie potrzeba bedzie jakas kalibracja
		//System.out.println("wygenerowalem mu takie parametry: "+parameters.toString());
	}
	
	public void setParameter(int i,double value){
		parameters.set(i, value);
	}
	
	public double getParameter(int i){
		return parameters.get(i);
	}
	
	
	/**
	 * Funkcja wykonujaca krzyzowanie osobnika z innym osobnikiem
	 * @param ind - drugi osobnik
	 * @return potomek
	 */
	public Individual crossWith(Individual ind){
		Individual child=new Individual(parameters.size());
		for(int i=0;i<parameters.size();i++){
			double absDif=Math.abs(this.parameters.get(i)-ind.parameters.get(i));
			double avg=(this.parameters.get(i)+ind.parameters.get(i))/2;
			double lowerThreshold=avg-absDif;
			if(lowerThreshold<0.0)lowerThreshold=0.0;
			double higherThreshold=avg+absDif;
			double newParameter=rand.nextDouble()*(higherThreshold-lowerThreshold)+lowerThreshold;
			child.setParameter(i, newParameter);
		}
		return child;
	}
	
	public void removeToHigh(){ 
		for(int i=0;i<parameters.size();i++){
			if(parameters.get(i)>10000.0)parameters.set(i, 0.0);
		}
	}
	
	public String toString(){
		return parameters.toString();
	}
	
	int size(){
		return parameters.size();
	}
}
