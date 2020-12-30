package com.project.demo.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	public ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	
	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(securityReferences())
				.build();
	}
	
	private List<SecurityReference> securityReferences(){
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "description");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0]=authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	public ApiInfo apiInfo() {
		return new ApiInfo("API Person Biodata", "Sample API", "1.0", "http://github.com/femiliapm",
				new Contact("Femilia", "http://github.com", "femiliapm@gmail.com"), "Apache", "https://www.apache.org",
				Collections.emptyList());
	}
}
