package pl.edu.agh.cs.kraksimcitydesigner.traffic.algorithm;

import pl.edu.agh.cs.kraksimcitydesigner.traffic.graph.Path;


/**
 * Klasa reprezentujaca pare bram (przechowuje rowniez najkrotsza sciezke miedzy tymi bramami)
 * @author Tomasz Adamski
 *
 */
public class GatePair {
	private int number;
	private int inputVertex;
	private int outputVertex;
	private Path path;

	public int getInputVertex() {
		return inputVertex;
	}

	public int getNumber() {
		return number;
	}

	public int getOutputVertex() {
		return outputVertex;
	}

	public Path getPath() {
		return path;
	}

	public void setInputVertex(int inputVertex) {
		this.inputVertex = inputVertex;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setOutputVertex(int outputVertex) {
		this.outputVertex = outputVertex;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
