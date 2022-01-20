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
				if (connected.getTime().isAfter(entry.getTime())) {
					continue;
				}
				//Period period = Period.fieldDifference(connected.getTime(), entry.getTime());

				Duration duration = calcDuration(connected, entry);
				
				if (bestTime == null || duration.isShorterThan(bestTime)) {
					best = entry;
					bestTime = duration;
				}
			}
		}
		if (best != null) {
			// TODO: more optimization possible: .remove() iterates over the entire list (again)!
			lList.remove(best);
		}
		return best;
	}

	public static Duration calcDuration(LogEntry start, LogEntry end) {
		return new Duration(
				start.getTime().toDateTime(DateTimeZone.UTC),
				end.getTime().toDateTime(DateTimeZone.UTC));
	}

	private final LogEntry connected;
	private final LogEntry disconnected;
	private final Duration duration;

	public LogSession(LogEntry connected, LogEntry disconnected) {
		super();
		// TODO: sanity check those input values
		this.connected = connected;
		this.disconnected = disconnected;
		// TODO: more optimization possible: duration gets calculated twice
		this.duration = calcDuration(connected, disconnected);
	}

	public LogEntry getConnected() {
		return connected;
	}
	public LogEntry getDisconnected() {
		return disconnected;
	}
	public Duration getDuration() {
		return duration;
	}
}
