package map;

import map.util.NameTable;

import map.creators.DesciptionCreator;
import map.creators.NodesCreator;
import map.creators.RoadsCreator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapContentGenerator {
	private int xSize = 8;
	private int ySize = 8;
	private NameTable nameTable = new NameTable(xSize, ySize);


	void createDocument(Document document) {
		Element root = createRoadNet(document);
		NodesCreator nodesCreator = new NodesCreator(document, nameTable, xSize, ySize);
		nodesCreator.createNodes(root);
		RoadsCreator rodesCreator = new RoadsCreator(document, nameTable, xSize, ySize);
		rodesCreator.createRoads(root);
		DesciptionCreator descriptionCreator = new DesciptionCreator(document, nameTable, xSize, ySize);
		descriptionCreator.createDescription(root);
	}

	

	private Element createRoadNet(Document document) {
		Element root = document.createElementNS(null, "RoadNet");
		document.appendChild(root);
		return root;
	}
}
