package ua.com.octopus_aqua.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	private static Log instance;

	private final String logPath = "/home/rise42/workspace/Servlets/log/";

	private BufferedWriter bw;

	Log() {
		File file = new File(logPath, getDate() + ".txt");

		try {
			file.getParentFile().mkdirs();
			this.bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException ioex) {
			
		}
	}

	public static void info(String message) {
		try {
			if(instance == null){
				instance = new Log();
			}
			instance.bw.write(getDate()+": "+message+"\n\r");
			instance.bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getDate(){
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(new Date());
	}
}
