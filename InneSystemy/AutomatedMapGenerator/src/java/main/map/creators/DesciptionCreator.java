package map.creators;

import map.util.NameTable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DesciptionCreator {
	private Document document;
	private NameTable nameTable;
	private int xSize;
	private int ySize;
	
	
	public DesciptionCreator(Document document, NameTable nameTable, int xSize, int ySize) {
		super();
		this.document = document;
		this.nameTable = nameTable;
		this.xSize = xSize;
		this.ySize = ySize;
	}


	public void createDescription(Element root) {
		Element descriptions = document.createElement("intersectionDescriptions");
		root.appendChild(descriptions);
		for (int i = 1; i < xSize - 1; i++) {
			for (int j = 1; j < ySize - 1; j++) {
				Element intersection = createIntersectionDescription(i, j);
				descriptions.appendChild(intersection);
			}
		}	
	}


	private Element createIntersectionDescription(int i, int j) {
		Element intersection = document.createElement("intersection");
		intersection.setAttribute("id", nameTable.getName(i, j));
		createArmActionsGroup(intersection, i, j);
		TrafficScheduleCreator tsc = new GreenTrafficScheduleCreator(document, nameTable);
		tsc.createLightsSchedule(intersection, i, j);
		return intersection;
	}
	
	private void createArmActionsGroup(Element intersection, int i, int j) {
		createArmActions("NS", intersection, i, j, 0, -1);
		createArmActions("WE", intersection, i, j, 1, 0);
		createArmActions("NS", intersection, i, j, 0, 1);
		createArmActions("WE", intersection, i, j, -1, 0);	
	}


	private void createArmActions(String dir, Element intersection, int i, int j, int a, int b) {
		String armName = nameTable.getName(i + a, j + b);
		Element armAction = document.createElement("armActions");
		armAction.setAttribute("arm", armName);
		armAction.setAttribute("dir", dir);
		intersection.appendChild(armAction);
		
		createActionLeft(i, j, a, b, armAction);
		createActionToword(i, j, a, b, armAction);
		createActionRight(i, j, a, b, armAction);
	}


	private void createActionLeft(int i, int j, int a, int b, Element armAction) {
		Element actionLeft = document.createElement("action");
		actionLeft.setAttribute("lane", "-1");
		actionLeft.setAttribute("exit", nameTable.getName(i - b, j + a));
		armAction.appendChild(actionLeft);
		
		
		Element ruleRight = document.createElement("rule");
		ruleRight.setAttribute("entrance",nameTable.getName(i + b, j - a));
		ruleRight.setAttribute("lane", "0");
		actionLeft.appendChild(ruleRight);
		
		Element ruleToward = document.createElement("rule");
		ruleToward.setAttribute("entrance",nameTable.getName(i - a, j - b));
		ruleToward.setAttribute("lane", "0");
		actionLeft.appendChild(ruleToward);
		
		Element ruleLeft = document.createElement("rule");
		ruleLeft.setAttribute("entrance",nameTable.getName(i - b, j + a));
		ruleLeft.setAttribute("lane", "0");
		actionLeft.appendChild(ruleLeft);
	}


	private void createActionToword(int i, int j, int a, int b, Element armAction) {
		Element actionToward = document.createElement("action");
		actionToward.setAttribute("lane", "0");
		actionToward.setAttribute("exit", nameTable.getName(i - a, j - b));
		armAction.appendChild(actionToward);
		
		Element ruleRight = document.createElement("rule");
		ruleRight.setAttribute("entrance",nameTable.getName(i + b, j - a));
		ruleRight.setAttribute("lane", "0");
		actionToward.appendChild(ruleRight);
	}


	private void createActionRight(int i, int j, int a, int b, Element armAction) {
		Element actionRight = document.createElement("action");
		actionRight.setAttribute("lane", "0");
		actionRight.setAttribute("exit", nameTable.getName(i + b, j - a));
		armAction.appendChild(actionRight);
	}



	
}
