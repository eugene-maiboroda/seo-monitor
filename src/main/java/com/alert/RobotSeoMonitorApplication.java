package com.alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RobotSeoMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobotSeoMonitorApplication.class, args);
	}

}
