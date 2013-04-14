package pl.edu.agh.cs.kraksimcitydesigner.traffic.graph;



/**
 * Klasa reprezentujaca krawedz w grafie.
 * @author Tomasz Adamski
 *
 */
public class Edge {
	private int inputCars=0;
	private int outputCars=0;

	private int distance;

	private double traffic;

	private Vertex Destination;

	public Vertex getDestination() {
		return Destination;
	}

	public int getDistance() {
		return distance;
	}

	public int getInputCars() {
		return inputCars;
	}

	public int getOutputCars() {
		return outputCars;
	}

	public double getTraffic() {
		return traffic;
	}

	public void reset() {
		traffic = 0;
	}

	public void setDestination(Vertex destination) {
		Destination = destination;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setInputCars(int inputCars) {
		this.inputCars = inputCars;
	}

	public void setOutputCars(int outputCars) {
		this.outputCars = outputCars;
	}

	public void setTraffic(double traffic) {
		this.traffic = traffic;
	}
	
	public void increaseTraffic(double increase){
		traffic+=increase;
	}
	
	public String toString(){
		return Destination.toString()+"("+new Integer(inputCars)+")";
	}
}
