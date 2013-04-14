package map.util;

public class NameTable {
	private int xSize;
	private int ySize;
	private String[][] nameTable;
	private int intersectionCounter;
	
	public NameTable(int xNumer, int yNumer) {
		super();
		this.xSize = xNumer;
		this.ySize = yNumer;
		intersectionCounter = 0;
		createNameTable();
	}
	
	public String getName(int i, int j) {
		return nameTable[i][j];
	}

	private void createNameTable() {
		nameTable = new String[xSize][ySize];
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				if(isCorner(i,j)) {
					createCorner(i,j);
				} else if(isEdgeNode(i,j)) {
					createEdgeNode(i,j);
				} else {
					createIntersection(i,j);
				}
			}
		}
	}

	private void createIntersection(int i, int j) {
		intersectionCounter++;
		setName(i,j, "X" + intersectionCounter);
	}

	private void createEdgeNode(int i, int j) {
		if (i == 0) setName(i, j, "W" + j);
		if (i == xSize - 1) setName(i, j, "E" + j);
		if (j == 0) setName(i, j, "N" + i);
		if (j == ySize - 1) setName(i, j, "S" + i);
	}

	private boolean isEdgeNode(int i, int j) {
		if (i == 0 && (j != 0 || j != ySize - 1)) return true;
		if (i == xSize - 1 && (j != 0 || j != ySize - 1)) return true;
		if ((i != 0 || i != xSize - 1) && j == 0) return true;
		if ((i != 0 || i != xSize - 1) && j == ySize - 1) return true;
		return false;
	}

	private void createCorner(int i, int j) {
		setName(i, j, "");
	}

	private boolean isCorner(int i, int j) {
		if(i == 0 && j == 0) return true;
		if(i == 0 && j == ySize -1) return true;
		if(i == xSize - 1 && j == 0) return true;
		if(i == xSize - 1 && j == ySize - 1) return true;
		return false;
	}
	
	private void setName(int i, int j, String name){
		nameTable[i][j] = name;
	}	
}
