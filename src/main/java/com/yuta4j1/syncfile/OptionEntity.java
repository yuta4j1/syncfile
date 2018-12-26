package com.yuta4j1.syncfile;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

public class OptionEntity {

	@Parameter
    public List<String> parameters = Lists.newArrayList();

	@Parameter(names = {"--from"}, description = "file path to be monitored and copied")
	private String from = "";

	@Parameter(names = {"--to"}, description = "destination directory path")
	private String to = "";

	public void validate() {
		if (from.equals("") || to.equals("")) {
			throw new IllegalArgumentException("Invalid arguments.");
		}
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

}
