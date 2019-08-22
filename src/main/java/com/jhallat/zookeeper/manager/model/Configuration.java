package com.jhallat.zookeeper.manager.model;

import java.util.List;

import lombok.Data;

@Data
public class Configuration {

	private int tickTime;
	private int initLimit;
	private int syncLimit;
	private String dataDir;
	private int clientPort;
	private List<String> servers;
	
}
