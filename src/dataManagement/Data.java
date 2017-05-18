package dataManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Data {
	
	public static HashMap<Integer, HashMap<String, String>> readData(String path){
		
		File file = new File(path);
		FileReader fin = null;
		BufferedReader in = null;
		
		String line;
		Integer id;
		HashMap<String, String> params;
		HashMap<Integer, HashMap<String, String>> result = new HashMap<>();
		
		try {
			
			fin = new FileReader(file);
			in = new BufferedReader(fin);
			
			while((line = in.readLine()) != null){
				params = parseParameters(line, ",");
				id = Integer.parseInt(params.get("id"));
				
				result.put(id, params);
			}
			
			fin.close();
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static HashMap<String, String> parseParameters(String toParse, String regex){
		HashMap<String, String> parameters = new HashMap<String, String>();
		String[] split = toParse.split(regex);
		for(int i = 0; i < split.length; i++){
			String[] param = split[i].split("=");
			parameters.put(param[0], param[1]);
		}
		return parameters;
	}
	
	public static double parseDouble(String arg) {
		if (arg.contains("/")) {
			String[] rat = arg.split("/");
			return (Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]));
		} else {
			return Double.parseDouble(arg);
		}
	}
}
