package com.brainquizapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.brainquizapi.model.FileStorageProperties;



@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})


public class BrainquizapiApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BrainquizapiApplication.class, args);
	}

}
