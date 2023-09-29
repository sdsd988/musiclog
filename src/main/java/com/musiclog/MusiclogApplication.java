package com.musiclog;

import com.musiclog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class MusiclogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusiclogApplication.class, args);
	}

}
