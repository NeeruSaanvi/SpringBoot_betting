package com.injury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InjuryToolApplication {

	public static void main(String[] args) {
		//this property is required to set false manually for custom log color
		System.setProperty("log4j.skipJansi", "false");
		SpringApplication.run(InjuryToolApplication.class, args);
		
	}

}
