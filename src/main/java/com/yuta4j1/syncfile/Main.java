package com.yuta4j1.syncfile;

import java.nio.file.Paths;

import com.beust.jcommander.JCommander;

public class Main {

	public static void main(String[] args) {

		OptionEntity entity = commandLineParse(args);
		entity.validate();
		System.out.println(entity);

		FileModifyObserver fmo = new FileModifyObserver(Paths.get(entity.getFrom()), Paths.get(entity.getTo()));
		fmo.observe();

	}

	private static OptionEntity commandLineParse(String[] args) {
		OptionEntity entity = new OptionEntity();
		JCommander.newBuilder().addObject(entity).build().parse(args);
		return entity;
	}



}
