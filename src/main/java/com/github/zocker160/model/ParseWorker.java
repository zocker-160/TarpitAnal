package com.github.zocker160.model;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.Callable;

public class ParseWorker implements Callable<LogEntry> {

    private final byte[] work;

    public ParseWorker(byte[] work) {
        this.work = work;
    }

    @Override
    public LogEntry call() {
        try {
            return LogEntry.parseNew(this.work);
        } catch (UnknownHostException | ParseException e) {
            //System.out.println("Ignoring line: "+new String(this.work));
        }
        return null;
    }
}
