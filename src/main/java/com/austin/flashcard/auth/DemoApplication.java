package com.austin.flashcard.auth;

import com.austin.flashcard.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
@Slf4j
@SpringBootApplication
@RestController
public class DemoApplication {


/*	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			log.info("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
			var object = ctx.getBean(beanName);
				if(object.getClass().toString().contains("org.springframework.security")
				&& !object.getClass().toString().contains("annotation"))
					log.info("bean name:{}, class:{}", beanName, object.getClass());
			}
		};
	}*/


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
