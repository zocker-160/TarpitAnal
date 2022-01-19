package com.github.zocker160;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(mixinStandardHelpOptions = true, helpCommand = true)
public class Arguments {
	@Parameters(paramLabel="File", arity="1", description="tarpit log file")
	private String file;
	
	public String getFile() {
		return file;
	}
}
