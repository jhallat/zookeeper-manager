package com.jhallat.zookeeper.manager.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfigurationController {

	private final String zookeeperHome;
	private final String zookeeperConfiguration;
	
	public ConfigurationController(@Value("${zookeeper.home}")String zookeeperHome,
			                       @Value("${zookeeper.conf") String zookeeperConfiguration) {
		this.zookeeperHome = zookeeperHome;
		this.zookeeperConfiguration = zookeeperConfiguration;
	}
	
	@GetMapping("/configurations")
	public List<String> getConfigurations() {
		
		File file = new File(zookeeperHome + "/" + zookeeperConfiguration);
		
		return null;
	}
	
}
