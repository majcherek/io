package map.creators;

import map.util.NameTable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RoadsCreator {
	private Document document;
	private NameTable nameTable;
	private int xSize;
	private int ySize;

	public RoadsCreator(Document document, NameTable nameTable, int xSize, int ySize) {
		super();
		this.document = document;
		this.nameTable = nameTable;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public void createRoads(Element root) {
		Element roads = document.createElementNS(null, "roads");
		roads.setAttribute("defaultSpeedLimit", "2");
		root.appendChild(roads);
		for (int i = 1; i < xSize - 1; i++) {
			for (int j = 1; j < ySize - 1; j++) {
				createRoadsForIntersection(roads, i, j);
			}
		}
	}

	private void createRoadsForIntersection(Element roads, int i, int j) {
		String fromNorth = nameTable.getName(i, j - 1);
		String toNorth = nameTable.getName(i, j);
		Element road = createRoad(fromNorth, toNorth);
		roads.appendChild(road);

		String fromWest = nameTable.getName(i - 1, j);
		String toWest = nameTable.getName(i, j);
		road = createRoad(fromWest, toWest);
		roads.appendChild(road);

		if (i == xSize - 2) {
			String fromEast = nameTable.getName(i + 1, j);
			String toEast = nameTable.getName(i, j);
			road = createRoad(fromEast, toEast);
			roads.appendChild(road);
		}

		if (j == ySize - 2) {
			String fromSouth = nameTable.getName(i, j + 1);
			String toSouth = nameTable.getName(i, j);
			road = createRoad(fromSouth, toSouth);
			roads.appendChild(road);
		}

	}

	private Element createRoad(String from, String to) {
		Element road = document.createElement("road");
		road.setAttribute("id", from + to);
		road.setAttribute("from", from);
		road.setAttribute("to", to);

		Element uplink = createUplink(from,to);
		road.appendChild(uplink);

		Element downlink = createDownlink(to, from);
		road.appendChild(downlink);

		return road;
	}

	private Element createUplink(String from, String to) {
		Element uplink = document.createElement("uplink");
		Element main = document.createElement("main");
		main.setAttribute("length", "100");
		main.setAttribute("numberOfLanes", "2");
		uplink.appendChild(main);
		if (to.startsWith("X")) {
			Element left = document.createElement("left");
			left.setAttribute("length", "20");
			uplink.appendChild(left);
		}
		return uplink;
	}

	private Element createDownlink(String to, String from) {
		Element downlink = document.createElement("downlink");
		Element main = document.createElement("main");
		main.setAttribute("length", "100");
		main.setAttribute("numberOfLanes", "2");
		downlink.appendChild(main);
		if (from.startsWith("X")) {
			Element left = document.createElement("left");
			left.setAttribute("length", "20");
			downlink.appendChild(left);
		}
		return downlink;
	}

}
