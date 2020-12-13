package mx.com.gruponordan.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		
		  return new Docket(DocumentationType.SWAGGER_2) .select()
		  .apis(RequestHandlerSelectors.basePackage("mx.com.gruponordan.controller"))
		  .paths(regex("/api.*")) .build(); //.apiInfo(metaInfo());
		  
		  }
		 

//		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.any()).build();
//	}

	/*
	 * private ApiInfo metaInfo() { // TODO Auto-generated method stub
	 * List<VendorExtension<String>> list = new
	 * ArrayList<VendorExtension<String>>();
	 * 
	 * //ApiInfo apiInfo = new
	 * ApiInfo("Spring Boot Swagger Example","Documentos del API"
	 * ,"1.0.0","Terms of Service",new
	 * Contact("FDS","http://www.rts.com","ovaldez@rts.com"),"MIT",
	 * "http://www.mitlicence.org",list); ApiInfo apiInfo = new
	 * ApiInfo("Spring Boot Swagger Example","Documentos del API"
	 * ,"1.0.0","Terms of Service", "ovaldez", "MIT", "http://www.rts.com"); return
	 * apiInfo; }
	 */
}
