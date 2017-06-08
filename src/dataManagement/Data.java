package dataManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	public static void writeToLog(String toLog){
		File dir = new File("log/");
		dir.mkdirs();
		
		SimpleDateFormat s = new SimpleDateFormat("dd_MM_yyyy");
		
		File logfile = new File(dir, s.format(new Date()) + ".log");
		
		s = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss");
		
		Writer writer = null;
		
		try {
			writer = new FileWriter(logfile, true);
			writer.write(s.format(new Date()) + "::" + toLog);
			writer.write(System.lineSeparator());
		} catch (IOException e) {
			System.err.println(e);
		} finally{
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
