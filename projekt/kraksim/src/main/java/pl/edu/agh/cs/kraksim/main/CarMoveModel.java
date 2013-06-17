package pl.edu.agh.cs.kraksim.main;

import java.util.HashMap;
import java.util.Map;

public class CarMoveModel {
	
	public static final String MODEL_VDR = "vdr";
	public static final String MODEL_VDR_0_PROB = "zeroProb";
	public static final String MODEL_VDR_MOVE_PROB = "movingProb";
	
	public static final String MODEL_NAGLE = "nagle";
	public static final String MODEL_NAGLE_MOVE_PROB = "decProb";
	
	public static final String MODEL_BRAKELIGHT = "bl";
	public static final String MODEL_BRAKELIGHT_0_PROB = "zeroProb";
	public static final String MODEL_BRAKELIGHT_MOVE_PROB = "movingProb";
	public static final String MODEL_BRAKELIGHT_BRAKE_PROB = "brakeProb";
	public static final String MODEL_BRAKELIGHT_DISTANCE_THRESHOLD = "threshold";
	
	public static final String MODEL_MULTINAGLE = "multiNagle";
	public static final String MODEL_MULTINAGLE_MOVE_PROB = "decProb";
	
	private String name;
	private Map<String, String> parametrs;
	
	public CarMoveModel(String data){
		
		System.out.println(data);
		
		int index = data.indexOf(":");
		if(index>0){
			this.name=data.substring(0, index);
			data = data.substring(index+1);
			
			String[] params = data.split(",");
			parametrs = new HashMap<String, String>();
			
			for(String par : params){
				String[] p = par.split("=");
				if(p.length == 2){
					parametrs.put(p[0], p[1]);
				}
			}

		}else{
			this.name = data;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getParametrs() {
		return parametrs;
	}

}
