package map.creators;

import map.util.Coordinate;
import map.util.NameTable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NodesCreator {
	private Coordinate coordinate = new Coordinate();
	private Document document;
	private NameTable nameTable;
	private int xSize;
	private int ySize;
	
	public NodesCreator(Document document, NameTable nameTable, int xSize, int ySize) {
		super();
		this.document = document;
		this.nameTable = nameTable;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public void createNodes(Element root) {
		Element nodes = document.createElementNS(null, "nodes");
		root.appendChild(nodes);
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if (isNotCornerNode(i, j)) {
					Element node = createNode(i, j);
					nodes.appendChild(node);
				}
			}
		}
	}

	private boolean isNotCornerNode(int i, int j) {
		if (i == 0 && j == 0)
			return false;
		if (i == 0 && j == ySize - 1)
			return false;
		if (i == xSize - 1 && j == 0)
			return false;
		if (i == xSize - 1 && j == ySize - 1)
			return false;
		return true;
	}

	private Element createNode(int i, int j) {
		String nodeName = nameTable.getName(i, j);
		Element node = null;
		if (nodeName.startsWith("X")) {
			node =  document.createElement("intersection");
		} else {
			node =  document.createElement("gateway"); 
		}
		node.setAttribute("id", nodeName);
		node.setAttribute("x", "" + coordinate.getCooridnateX(i));
		node.setAttribute("y", "" + coordinate.getCooridnateY(j));
		return node;
	}
}
