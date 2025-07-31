package com.saswat.GeminiShell.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini.api")
public record GeminiConfiguration (String key, String endpoint){
}
