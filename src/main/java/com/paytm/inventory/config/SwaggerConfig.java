package com.paytm.inventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    ServletContext servletContext;

    @Value("${swagger.base.url:localhost:8080}")
    String baseUrl;

    @Bean
    public Docket productApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .pathProvider(new RelativePathProvider(servletContext) {
                    @Override
                    public String getApplicationBasePath() {
                        return "/";
                    }

                })
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.paytm.inventory.controller"))
                .build()
                .apiInfo(metaData()).ignoredParameterTypes(ApiIgnore.class);
    }

    private ApiInfo metaData() {
        String description = "Paytm Inventory API";
        return new ApiInfoBuilder()
                .title("Paytm Inventory REST API")
                .description(description)
                .version("0.0.1")
                .build();
    }
}
