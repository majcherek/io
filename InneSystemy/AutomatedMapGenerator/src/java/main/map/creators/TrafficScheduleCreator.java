package map.creators;

import map.util.NameTable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class TrafficScheduleCreator {
	protected Document document;
	protected NameTable nameTable;
	
	public TrafficScheduleCreator(Document document, NameTable nameTable) {
		super();
		this.document = document;
		this.nameTable = nameTable;
	}

	public void createLightsSchedule(Element intersection, int i, int j) {
		Element lightsSchedule = document.createElement("trafficLightsSchedule");
		intersection.appendChild(lightsSchedule);
		createPhases(lightsSchedule, i, j);
	}
	
	protected abstract void createPhases(Element intersection, int i, int j);

}
