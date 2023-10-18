package com.projectmicrosoft.microsoft;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Project Manager", version = "1.0.0", description = "API for project manager"))
public class MicrosoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrosoftApplication.class, args);
	}

}
