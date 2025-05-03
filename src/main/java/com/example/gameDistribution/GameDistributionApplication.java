package com.example.gameDistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameDistributionApplication {

	// Bu static blok, main() metodundan önce çalışır
	static {
		System.setProperty("https.protocols", "TLSv1.2");
		System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
	}

	public static void main(String[] args) {
		SpringApplication.run(GameDistributionApplication.class, args);
	}
}