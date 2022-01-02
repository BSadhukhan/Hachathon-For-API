package com.ninja.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = "com.ninja.lms")
@EnableSwagger2
public class LmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApiApplication.class, args);
	}
	
	@Bean
	public Docket apiToSwagger() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getApiInformation())
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build();
	
	}
	
	private ApiInfo getApiInformation(){
		
		Contact contact = new Contact("Team - Code Cruncher ","https://www.numpyninja.com/","");
		
		return new ApiInfoBuilder()
	              .title("API Documentation For HACKATHON 21-22")
	              .description("This is the LMS API created using Spring Boot.")
	              .contact(contact)
	              .version("1.0.0")
	              .build();
	}

}
