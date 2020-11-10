package org.fms.cfs.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.riozenc.titanTool.mongo.MongoPoolFactory;

/**
 * Hello world!
 *
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "org.fms.cfs", exclude = MongoAutoConfiguration.class)
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		MongoPoolFactory.init();

	}

}