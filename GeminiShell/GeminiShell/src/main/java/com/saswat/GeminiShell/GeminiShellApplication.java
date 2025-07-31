package com.saswat.GeminiShell;

import com.saswat.GeminiShell.config.GeminiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties(GeminiConfiguration.class)
@SpringBootApplication
public class GeminiShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeminiShellApplication.class, args);
	}

}
