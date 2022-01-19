package com.github.zocker160.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;

public class LogSession {

	public static List<LogSession> calcSessions(List<LogEntry> lList) {
		List<LogSession> sessions = new ArrayList<>();
		LogEntry connected;

		while ((connected = getAnyConnected(lList)) != null) {
			LogEntry disconnected = getDisconnected(lList, connected);
			if (disconnected != null) {
				sessions.add(new LogSession(connected, disconnected));
			}
		}

		return sessions;
	}

	private static LogEntry getAnyConnected(List<LogEntry> lList) {
		Iterator<LogEntry> iterator = lList.iterator();

		while (iterator.hasNext()) {
			LogEntry entry = iterator.next();
			if (entry.getState() == State.CONNECTED) {
				iterator.remove();
				return entry;
			}
		}
		return null;
	}

	private static LogEntry getDisconnected(List<LogEntry> lList, LogEntry connected) {
		LogEntry best = null;
		Duration bestTime = null;

		for (LogEntry entry : lList) {
			if (entry.getState() == State.DISCONNECTED) {
				if (connected.getTime().isBefore(entry.getTime())) {
					continue;
				}
				Period period = Period.fieldDifference(connected.getTime(), entry.getTime());
				// TODO: use proper duration method

				Duration duration = new Duration(
						connected.getTime().toDateTime(DateTimeZone.UTC),
						entry.getTime().toDateTime(DateTimeZone.UTC)
				);
				
				if (bestTime == null || duration.isShorterThan(bestTime)) {
					best = entry;
					bestTime = duration;
				}
			}
		}
		if (best != null) {
			lList.remove(best);
		}
		return best;
	}

	private final LogEntry connected;
	private final LogEntry disconnected;

	public LogSession(LogEntry connected, LogEntry disconnected) {
		super();
		// TODO: sanity check those input values
		this.connected = connected;
		this.disconnected = disconnected;
	}

	public LogEntry getConnected() {
		return connected;
	}

	public LogEntry getDisconnected() {
		return disconnected;
	}
}
