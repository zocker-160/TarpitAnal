package com.github.zocker160.model;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LogEntry {
	
	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	public static LogEntry parse(String line) throws UnknownHostException  {
		String[] parts = line.split(",");
		
		if (parts.length != 4) {
			throw new IllegalArgumentException("logfile does not have 4 parts");
		}
		
		LocalDateTime localDateTime = formatter.parseLocalDateTime(parts[0]);
		Inet4Address ip = (Inet4Address)Inet4Address.getByName(parts[1]);
		int port = Integer.parseInt(parts[2]);
		Connection con = new Connection(ip, port);
		State state = State.valueOf(parts[3].toUpperCase());
		
		return new LogEntry(localDateTime, con, state);
	}
	
	public static LogEntry parse_raw(String line) throws UnknownHostException, ParseException {
		if (!line.contains("Client")) {
			throw new ParseException(line, 0);
		}
		
		String[] parts = line.split("INFO");
		LocalDateTime localDateTime = formatter.parseLocalDateTime(parts[0].strip());
		
		parts = parts[1].split("'");
		Inet4Address ip = (Inet4Address)Inet4Address.getByName(parts[1]);
		
		parts = parts[2].split(" ");
		State state = State.valueOf(parts[2].toUpperCase());
		int port = Integer.parseInt(parts[1].split("\\)")[0]);
		Connection con = new Connection(ip, port);
		
		return new LogEntry(localDateTime, con, state);
	}
	
	private final LocalDateTime time;
	private final Connection con;
	private final State state;

	public LogEntry(LocalDateTime time, Connection con, State state) {
		super();
		this.time = time;
		this.con = con;
		this.state = state;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	public Connection getCon() {
		return con;
	}
	public State getState() {
		return state;
	}

	@Override
	public String toString() {
		return "LogEntry [time=" + time + ", con=" + con + ", state=" + state + "]";
	}
}