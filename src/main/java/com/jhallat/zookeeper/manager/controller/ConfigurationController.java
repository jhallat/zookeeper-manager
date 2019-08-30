package com.jhallat.zookeeper.manager.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.jhallat.zookeeper.manager.model.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ConfigurationController {

	private final String zookeeperHome;
	private final String zookeeperConfiguration;
	
	public ConfigurationController(@Value("${zookeeper.home}")String zookeeperHome,
			                       @Value("${zookeeper.configuration}") String zookeeperConfiguration) {
		this.zookeeperHome = zookeeperHome;
		this.zookeeperConfiguration = zookeeperConfiguration;
	}
	
	@GetMapping("/configurations")
	public ResponseEntity<List<String>> getConfigurations() {
		
		List<String> configurations = new ArrayList<>();
		File file = new File(zookeeperHome + "/" + zookeeperConfiguration);
		if (file.isDirectory()) {
			File[] files = file.listFiles(item -> {
				return item.getName().endsWith(".cfg");
			});
			for (File configFile : files) {
				String configName = configFile.getName().substring(0, configFile.getName().indexOf(".cfg"));
				configurations.add(configName);
			}
		} else {
			log.warn(file.getPath() + " is not a directory.");
			//TODO there is a better way to handle this
			//return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(configurations, HttpStatus.OK);
	}
	
	@GetMapping("/configuration/{config}")
	public ResponseEntity<Configuration> getConfiguration(@PathVariable("config") String config) {
		
		//TODO Add validation to config parameter
		File file = new File(zookeeperHome + "/" + zookeeperConfiguration + "/" + config + ".cfg");
		Configuration configuration = new Configuration();
		if (file.exists()) {
			try(Scanner scanner = new Scanner(file)) {
				scanner.useDelimiter("\n");
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] values = line.split("=");
					if (values.length != 2) {
						log.warn("Invalid setting in configuration: " + config);
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
				}
			} catch (IOException exception) {
				log.error("Error reading configuration", exception);
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);				
			}
			
		} else {
			log.warn(config + " is not a valid configuration");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(configuration, HttpStatus.OK);
	}
}
