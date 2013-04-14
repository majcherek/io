package map.util;

public class Coordinate {
	private static final int X_INITIAL = 10;
	private static final int X_PACE = 210;
	private static final int Y_INITIAL = 50;
	private static final int Y_PACE = 160;
	
	
	public int getCooridnateX(int number) {
		return X_INITIAL + X_PACE * number;
	}
	
	public int getCooridnateY(int number) {
		return Y_INITIAL + Y_PACE * number;
	}
}
