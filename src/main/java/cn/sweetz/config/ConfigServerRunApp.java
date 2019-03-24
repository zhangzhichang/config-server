package cn.sweetz.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerRunApp {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerRunApp.class, args);
	}

}
