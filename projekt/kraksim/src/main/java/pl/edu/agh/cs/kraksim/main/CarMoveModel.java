package pl.edu.agh.cs.kraksim.main;

import java.util.HashMap;
import java.util.Map;

public class CarMoveModel {
	
	private String name;
	private Map<String, String> parametrs;
	
	public CarMoveModel(String data){
		
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
