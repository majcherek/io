package map.creators;

import map.util.NameTable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GreenTrafficScheduleCreator extends TrafficScheduleCreator {

	public GreenTrafficScheduleCreator(Document document, NameTable nameTable) {
		super(document, nameTable);
	}

	@Override
	protected void createPhases(Element lightsSchedule, int i, int j) {
		Element phase1 = createPhase("1",i,j);
		lightsSchedule.appendChild(phase1);
		Element phase2 = createPhase("2",i,j);
		lightsSchedule.appendChild(phase2);
		Element phase3 = createPhase("3",i,j);
		lightsSchedule.appendChild(phase3);
		Element phase4 = createPhase("4",i,j);
		lightsSchedule.appendChild(phase4);
		
	//	Element plan = createPlan();
	//	lightsSchedule.appendChild(plan);

	}

	private Element createPlan() {
		Element plan = document.createElement("plan");
		plan.setAttribute("name", "plan");
		
		Element phase1 = createPhaseElement("1");
		plan.appendChild(phase1);
		Element phase2 = createPhaseElement("2");
		plan.appendChild(phase2);
		Element phase3 = createPhaseElement("3");
		plan.appendChild(phase3);
		Element phase4 = createPhaseElement("4");
		plan.appendChild(phase4);
		
		return plan;
	}

	protected Element createPhase(String num, int i, int j) {
		Element phase = createPhaseElement(num);

		if (num.equals("1")) {
			createInlaneGroup(phase, i, j, "red", "red", "red", "green");
		} else if (num.equals("2")) {
			createInlaneGroup(phase, i, j, "red", "red", "green", "red");
		} else if (num.equals("3")) {
			createInlaneGroup(phase, i, j, "red", "green", "red", "red");
		} else if (num.equals("4")) {
			createInlaneGroup(phase, i, j, "green", "red", "red", "red");
		}

		return phase;
	}

	private Element createPhaseElement(String num) {
		Element phase = document.createElement("phase");
		phase.setAttribute("num", num);
		phase.setAttribute("name", "phase_" + num);
		phase.setAttribute("duration", "50");
		return phase;
	}

	private void createInlaneGroup(Element phase, int i, int j, String state1, String state2, String state3,
			String state4) {
		createInlaneForLink(phase, i - 1, j, state1);
		createInlaneForLink(phase, i + 1, j, state2);
		createInlaneForLink(phase, i, j - 1, state3);
		createInlaneForLink(phase, i, j + 1, state4);

	}

	private void createInlaneForLink(Element phase, int i, int j, String state) {
		Element inlaneMain = createInline(i, j, "0", state);
		phase.appendChild(inlaneMain);
		Element inlaneLeft = createInline(i, j, "-1", state);
		phase.appendChild(inlaneLeft);
	}

	private Element createInline(int i, int j, String lane, String state) {
		Element inlane = document.createElement("inlane");
		inlane.setAttribute("arm", nameTable.getName(i, j));
		inlane.setAttribute("lane", lane);
		inlane.setAttribute("state", state);
		return inlane;
	}

}
