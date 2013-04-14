package pl.edu.agh.cs.kraksimcitydesigner.traffic;

import java.util.Map;

/**
 * Jest to reprezentacja jednej linii z pliku danych ZDKiA, zawiera metody ulatwiajace automatyczne mapowanie drog
 * @author Pawel Pierzchala
 *
 */
public class TrafficFileLine {
	/**
	 * stala oznaczajaca droge skrecajaca w prawo 
	 */
	public static final int DIRECTION_RIGHT = 1;
	/**
	 * stala oznaczajaca droge wprost
	 */
	public static final int DIRECTION_STRAIGHT = 2;
	/**
	 * stala oznaczajaca droge skrecajaca w lewo
	 */
	public static final int DIRECTION_LEFT = 3;
	
	private String from;
	private int direction;
	private int traffic;
	private int time;
	
	/**
	 * Jedyny konstruktor klasy, sluzy do tworzenia reprezentacji jednej linii.
	 * @param from nazwa wezla z ktorego wychodzi ruch
	 * @param direction kierunek drogi ze stalych DIRECTION
	 * @param traffic liczba pojazdow
	 * @param time czas przejazdu
	 */
	public TrafficFileLine(String from, int direction, int traffic, int time) {
		this.from = from;
		this.direction = direction;
		this.traffic = traffic;
		this.setTime(time);
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFrom() {
		return from;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getDirection() {
		return direction;
	}
	public void setTraffic(int traffic) {
		this.traffic = traffic;
	}
	public int getTraffic() {
		return traffic;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}
	
	/**
	 * Metoda sluzaca do tlumaczenia nazw skrzyzowania (identyfikator -> nazwa lub nazwa-> identyfikator)
	 * @param lines linie do przetlumaczenia
	 * @param names translacje nazw
	 * @return linie ze zmienionymi nazwami (zmiana zmiennej lines w miejscu)
	 */
	public static TrafficFileLine[] mapNames(TrafficFileLine[] lines, Map<String, String> names) {
		for(TrafficFileLine line : lines)
			line.setFrom(names.get(line.getFrom()));
		return lines;
	}
}
