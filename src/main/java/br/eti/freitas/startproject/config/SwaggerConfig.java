package br.eti.freitas.startproject.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.eti.freitas.startproject.security.constant.AuthorizationConstant;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* Contains default constant messages
*
* @author  Roberto Freitas
* @version 1.0
* @since   2023-03-01
*/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * Generate documentation for authentication endpoints
	 *
	 * @return Swagger documentation
	 */
	@Bean
	public Docket AutenticateApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Authenticate")
                .select()
				.apis(RequestHandlerSelectors.basePackage("br.eti.freitas.startproject.security.controller"))
                .paths(PathSelectors.ant("/api/auth/*"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInformation())
                ;
	}

	/**
	 * Generate documentation for application endpoints
	 *
	 * @return Swagger documentation
	 */
	@Bean
	public Docket ApplicationApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Application")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .useDefaultResponseMessages(false)
                .securitySchemes(securitySchemes())
                .securityContexts(List.of(securityContext()))
                .apiInfo(apiInformation())
                ;
	}

	/**
	 *
	 * @return Author information and license of use
	 */
	private ApiInfo apiInformation() {
		return new ApiInfoBuilder().title("Security API")
				.description("API reference for developers")
				.version("1.0")
                .contact(new Contact("Roberto Freitas", null, "roberto@freitas.eti.br"))
				.build()
				;
	}

	private List<SecurityScheme> securitySchemes() {
		return List.of(new ApiKey(AuthorizationConstant.TOKEN_PREFIX, AuthorizationConstant.AUTHENTICATION_HEADER, "header"));
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				  .securityReferences(List.of(bearerAuthReference()))
				  .forPaths(PathSelectors.ant("/api/v1/**"))
				  .build()
				  ;
	}

	private SecurityReference bearerAuthReference() {
		return new SecurityReference(AuthorizationConstant.TOKEN_PREFIX, new AuthorizationScope[0]);
	}

}
