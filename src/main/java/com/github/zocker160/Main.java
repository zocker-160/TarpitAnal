package com.github.zocker160;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.zocker160.model.Connection;
import com.github.zocker160.model.LogEntry;
import com.github.zocker160.model.LogSession;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class Main {

	public static void main(String[] args) {
		Arguments arguments = new Arguments();
		CommandLine cli = new CommandLine(arguments);
		
		try {
			cli.parseArgs(args);
		} catch (ParameterException e) {
            // writer
            PrintWriter writer = cli.getErr();
            
            // error
            writer.write(cli.getColorScheme().errorText(e.getMessage()).toString() + "\n");
            writer.flush();
            
            // help
            cli.usage(writer);
            writer.flush();

			System.exit(0);
        }
		
		System.out.println("InputFile: " + arguments.getFile());
		
		
		// file parser
		Map<Connection, List<LogEntry>> logMap = new HashMap<>();
		
		long duration = System.currentTimeMillis();
		
		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(arguments.getFile()))) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int b; 
			
			while( (b = input.read()) != -1 ) {
				if (b == '\n') {
					String line = buffer.toString();
					buffer.reset();
					try {
						//LogEntry tlog = LogEntry.parse(line);
						LogEntry tlog = LogEntry.parse_raw(line);
						List<LogEntry> entries = logMap.computeIfAbsent(tlog.getCon(), (key) -> new ArrayList<>());
						entries.add(tlog);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						//System.out.println("ignoring entry: "+line);
					}
					continue;
				}
				buffer.write(b);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		duration -= System.currentTimeMillis();
		
		// end file parser
		
		// analyse data
		List<LogSession> sessions = new ArrayList<>();
		
		for (List<LogEntry> entries : logMap.values()) {
			//System.out.println(entries);
			sessions.addAll(LogSession.calcSessions(entries));
		}
		
		for (LogSession logSession : sessions) {
			System.out.println(logSession.getConnected() + " -> " + logSession.getDisconnected());
		}
		
		System.out.println("size "+logMap.size());
		System.out.println("duration "+duration);
		
		// generate csv
	}
}
