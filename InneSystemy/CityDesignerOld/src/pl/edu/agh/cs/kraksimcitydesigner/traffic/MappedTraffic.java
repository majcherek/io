package pl.edu.agh.cs.kraksimcitydesigner.traffic;

import java.util.HashMap;
import java.util.Map;

/**
 * Jest to klasa zawierajaca informacje o ruchu drogowym w formacie do analiz. Zawiera mapowanie nazw ulic na nazwy wezlow.
 * @author Pawel Pierzchala
 *
 */
public class MappedTraffic {
	private TrafficFileLine[] lines;
	private String[] orderedNodes;
	private int minTime;
	private int maxTime;
	private Map<String, Integer> inTraffic;
	private Map<String, Integer> outTraffic;
	
	/**
	 * Dokonuje automatycznego mapowania nazw skrzyzowan na podstawie przekazanych arugmentow.
	 * @param lines tablica informacji o ruchu
	 * @param orderNodes uszeregowana lista id sasiadow, porzadek wg ruchu wskazowek zegara
	 */
	public MappedTraffic(TrafficFileLine[] lines, String[] orderNodes) {
		minTime = lines[0].getTime();
		maxTime = lines[lines.length - 1].getTime();
		
		init(lines, orderNodes, minTime, maxTime);
	}
	
	/**
	 * Dokonuje automatycznego mapowania nazw skrzyzowan na podstawie przekazanych arugmentow. Wersja z przedzialem czasowym.
	 * @param lines tablica informacji o ruchu
	 * @param orderNodes uszeregowana lista id sasiadow, porzadek wg ruchu wskazowek zegara
	 * @param minTime minimalny czas
	 * @param maxTime maksymalny czas
	 */
	public MappedTraffic(TrafficFileLine[] lines, String[] orderNodes, int minTime, int maxTime) {
		init(lines, orderNodes, minTime, maxTime);
	}

	private void init(TrafficFileLine[] lines, String[] orderNodes,
			int minTime, int maxTime) {
		this.lines = lines;
		this.orderedNodes = orderNodes;
		this.minTime = minTime;
		this.maxTime = maxTime;
		inTraffic = new HashMap<String, Integer>();
		outTraffic = new HashMap<String, Integer>();
		for(String node : orderedNodes) {
			inTraffic.put(node, 0);
			outTraffic.put(node, 0);
		}
			
		initTraffic();
	}
	
	private void initTraffic() {
		for(TrafficFileLine line : lines) {
			if (line.getTime() >= minTime && line.getTime() <= maxTime) {
				String from = line.getFrom();
				String to = translate(from, line.getDirection());
				inTraffic.put(from, inTraffic.get(from) + line.getTraffic());
				outTraffic.put(to, outTraffic.get(to) + line.getTraffic());
			}
		}
	}
	
	/**
	 * Na podstawie id ulicy oraz numeru kierunku z pliku ZDKiA poprawnie okresla kierunek skretu
	 * @param from id skrzyzowania uzywane w KrakSimie
	 * @param direction kierunek skretu z danych ZDKiA
	 * @return prawdziwy kierunek skretu
	 */
	public String translate(String from, int direction) {
		for(int i = 0; i < orderedNodes.length; i++) {
			if (from.equals(orderedNodes[i])) {
				if (direction == TrafficFileLine.DIRECTION_RIGHT)
					return nodeIdMod(i - 1);
				if (direction == TrafficFileLine.DIRECTION_LEFT)
					return nodeIdMod(i + 1);
				if (direction == TrafficFileLine.DIRECTION_STRAIGHT)
					return nodeIdMod(i + 2);
			}
		}
		
		return null;
	}
	
	private String nodeIdMod(int i) {
		if (i == -1)
			return orderedNodes[orderedNodes.length -1];
		else
			return orderedNodes[i % orderedNodes.length];
	}
	
	/**
	 * Zwraca natezenie wychodzacego ruchu w kierunku danego wezla
	 * @param nodeId id z kraksima
	 * @return liczba pojazdow wyjezdzajacych w kierunku nodeId
	 */
	public int getOutTraffic(String nodeId) {
		return outTraffic.get(nodeId).intValue();
	}
	
	/**
	 * Zwraca natezenie ruchu wchodzacego od danego wezla
	 * @param nodeId id z kraksima
	 * @return liczba pojazdow wjezdzajacych od nodeId
	 */
	public int getInTraffic(String nodeId) {
		return inTraffic.get(nodeId).intValue();
	}

	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public int getMinTime() {
		return minTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public int getMaxTime() {
		return maxTime;
	}
}
