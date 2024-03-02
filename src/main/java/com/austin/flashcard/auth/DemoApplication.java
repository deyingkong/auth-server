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

	@Autowired
	private UserService userService;

	@GetMapping("/test")
	public Boolean test(){
		boolean result = userService.isUserExist("austin");
		return result;
	}

/*	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			log.info("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				log.info(beanName);
			}
		};
	}*/


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
