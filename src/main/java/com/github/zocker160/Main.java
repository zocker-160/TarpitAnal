package com.github.zocker160;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.*;

import com.github.zocker160.model.Connection;
import com.github.zocker160.model.LogEntry;
import com.github.zocker160.model.LogSession;

import com.github.zocker160.model.State;
import org.joda.time.Duration;
import picocli.CommandLine;
import picocli.CommandLine.ParameterException;

public class Main {

	public static void main(String[] args) {
		Arguments arguments = new Arguments();
		CommandLine cli = new CommandLine(arguments);

		int totalNumEntries = 0;

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

		if (arguments.getFile() == null) {
			System.out.println(cli.getUsageMessage());
			System.exit(0);
		}

		System.out.println("InputFile: " + arguments.getFile());
		
		// file parser
		Map<Connection, List<LogEntry>> logMap = new HashMap<>();
		
		long starttime = System.currentTimeMillis();
		
		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(arguments.getFile()))) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int b; 
			
			while( (b = input.read()) != -1 ) {
				if (b == '\n') {
					byte[] line =  buffer.toByteArray();
					buffer.reset();

					try {
						//LogEntry tlog = LogEntry.parse(line);
						//LogEntry tlog = LogEntry.parse_raw(new String(line));
						LogEntry tlog = LogEntry.parseNew(line);
						totalNumEntries++;

						//System.out.println(tlog);

						List<LogEntry> entries = logMap.computeIfAbsent(tlog.getCon(), (key) -> new ArrayList<>());
						entries.add(tlog);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						//System.out.println("ignoring entry: "+e.getMessage());
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

		long parsingTime = System.currentTimeMillis() - starttime;

		// analyse data

		List<LogSession> sessions = new ArrayList<>();
		int numConnected = 0;
		int numDisconnected = 0;
		Duration longestSession = null;
		Duration shortestSession = null;
		Duration totalTime = new Duration(0);

		for (List<LogEntry> entries : logMap.values()) {
			for (LogEntry entry : entries) {
				if (entry.getState() == State.CONNECTED)
					numConnected++;
				else
					numDisconnected++;
			}
			sessions.addAll(LogSession.calcSessions(entries));
		}

		for (LogSession logSession : sessions) {
			Duration tDur = logSession.getDuration();

			if (longestSession == null || longestSession.isShorterThan(tDur))
				longestSession = tDur;

			if (shortestSession == null || shortestSession.isLongerThan(tDur))
				shortestSession = tDur;

			totalTime = totalTime.plus(tDur);

			//System.out.println(logSession.getConnected() + " -> " + logSession.getDisconnected()
			//		+ ": " + logSession.getDuration().toStandardSeconds() + " seconds");
		}

		long calculationTime = System.currentTimeMillis() - starttime;

		/*
		Connected: 56
		Disconnected: 36
		Time in total: 01:07:05
		Average: 00:01:11
		Longest in Tarpit: 00:07:34
		Shortest in Tarpit: 00:00:00
		 */

		System.out.println("---");
		System.out.println("Total parsed entries: "+totalNumEntries);
		System.out.println("Connected: "+numConnected);
		System.out.println("Disconnected: "+numDisconnected);
		System.out.println("Time in total: "+TimeHelper.getTimeString(totalTime));
		System.out.println("Average: "+TimeHelper.getTimeString( new Duration( totalTime.getMillis() / sessions.size() ) ));
		System.out.println("Longest: "+TimeHelper.getTimeString(longestSession));
		System.out.println("Shortest: "+TimeHelper.getTimeString(shortestSession));

		long printTime = System.currentTimeMillis() - starttime;

		System.out.println("----");
		System.out.println("Parsing time: "+parsingTime+"ms");
		System.out.println("Calculation time: "+(calculationTime-parsingTime)+"ms");
		System.out.println("Print time: "+(printTime-calculationTime)+"ms");
		System.out.println("TOTAL time: "+(System.currentTimeMillis()-starttime)+"ms");
	}
}
