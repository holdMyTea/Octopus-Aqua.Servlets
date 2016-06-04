package ua.com.octopus_aqua.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ImageHandler {

	private static final String DEFAULT_NAME = "img";
	private static final String SUFFIX = "txt";
	private static File imgDir = new File("/home/rise42/BUFF/java/imgDir/");

	public static String saveImage(String imgInBase64) {

		Log.info("Saving image to file");

		File file;
		try {
			file = createImageFile();
		} catch (IOException e) {
			Log.info("Error while file creation: " + e.toString());
			return null;
		}

		try {
			Log.info("File saving: " + file.getAbsolutePath());
			return saveImageToFile(imgInBase64, file);
		} catch (IOException ioex) {
			Log.info("Error while file wrting: " + ioex.getMessage());
			return null;
		}

	}

	public static File createImageFile() throws IOException {
		if (!imgDir.exists()) {
			imgDir.mkdirs();
		}
		File file = new File(imgDir, DEFAULT_NAME + "0." + SUFFIX);
		for (int i = 1; file.exists(); i++) {
			file = new File(imgDir, DEFAULT_NAME + i + "." + SUFFIX);
		}

		file.createNewFile(); // no need to check return value, as existence of
								// such file is checked above
		Log.info("File created, Kappa");
		return file;
	}

	public static String saveImageToFile(String imgStr, File file) throws IOException {
		try(PrintWriter out = new PrintWriter(new FileOutputStream(file))){
			out.write(imgStr);
		}
		return file.getAbsolutePath();
	}
	
	public static String readImageToBase64(String filePath) throws NullPointerException{
		File file = new File(filePath);
		if(! file.exists()){
			throw new NullPointerException("File not found");
		}
		try(BufferedReader bf = new BufferedReader(new FileReader(new File(filePath)))){
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = bf.readLine())!=null){
				builder.append(line);
			}
			return builder.toString();
		} catch (IOException e) {
			Log.info("Error while file reading: "+e.toString());
			return null;
		}
				
	}
}
