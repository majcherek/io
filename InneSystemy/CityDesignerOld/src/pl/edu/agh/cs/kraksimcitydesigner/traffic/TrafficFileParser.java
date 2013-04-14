package pl.edu.agh.cs.kraksimcitydesigner.traffic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Klasa odpowiedzialna za parsing plikow z danymi 
 * @author Pawel Pierzchala
 *
 */
public class TrafficFileParser {
	private String[] streets;
	private TrafficFileLine[] trafficLines;
	
	
	/**
	 * Tworzy wewnetrzna reprezentacje podanego plikuj
	 * @param path sciezka do pliku z danymi ZDKiA
	 */
	public TrafficFileParser(String path) {
		List<String> streetsList = new ArrayList<String>();
		Map<Integer, String> streetsMap = new HashMap<Integer, String>();
		try {
			CSVReader reader = new CSVReader(new FileReader(path), '\t');
			
			String[][] lines = reader.readAll().toArray(new String[1][]);
			
			int i = 0;
			for(;i < lines.length; i++) {
				if (lines[i][0].equals("order"))
					break;
				
				if (!lines[i][0].equals("")) {
					String streetName = lines[i][0] + "\t" + lines[i][1].split(" - ")[0];
					streetsList.add(streetName);
					streetsMap.put(new Integer(lines[i][0]), streetName);
				}
			}
			
			List<TrafficFileLine> trafficLinesList = new ArrayList<TrafficFileLine>(); 
			streets = streetsList.toArray(new String[0]);
			
			i += 1;
			for(; i < lines.length; i++) {
				int traffic = 0;
				for(int j = 3; j < 11; j++)
					traffic += Integer.parseInt(lines[i][j]);
				
				trafficLinesList.add(new TrafficFileLine(
						streetsMap.get(new Integer(lines[i][0])),
						Integer.parseInt(lines[i][2]),
						traffic,
						Integer.parseInt(lines[i][1])));			
			}
			
			trafficLines = trafficLinesList.toArray(new TrafficFileLine[0]);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Zwraca liste nazw ulic przecinajajcych sie na tym skrzyzowaniu
	 * @return lista nazw ulic
	 */
	public String[] getStreets() {
		return streets;
	}
	
	/**
	 * Sluzy do pobrania wszytkich trafficLines z danego skrzyzowania
	 * @return lista trafficlines
	 */
	public TrafficFileLine[] getTrafficLines() {
		return trafficLines;
	}
}
