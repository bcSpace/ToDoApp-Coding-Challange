package response;

import java.util.ArrayList;

public class Events {
	
	String logs[][];
	
	public Events(ArrayList<Long> times, ArrayList<String> events) {
		logs = new String[times.size()][2]; 
		for(int i = 0; i < times.size(); i++) {
			logs[i][0] = times.get(i)+"";
			logs[i][1] = events.get(i); 
		}
	}

	public String[][] getLogs() {
		return logs;
	}

	public void setLogs(String[][] logs) {
		this.logs = logs;
	}

}
