package edu.upenn.capsproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class Logger {
	private static Logger log = null;

	private final CSVWriter writer;
	private Logger (File file) throws IOException{
		writer = new CSVWriter(new FileWriter(file));
	}

	public static void createLogger(File file) throws IOException{
		log = new Logger(file);
	}

	public static Logger getLogger() throws Exception{
		if (log == null){
			throw new NullPointerException("Log has not been created");
		}
		return log;
	}

	public void write(List<String[]> values){
		writer.writeAll(values);
	}

	public void close() throws IOException{
		writer.close();
	}
}
