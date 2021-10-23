/*
 * Copyright (c) 2020. SimplyATX.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.txstate.ctl_parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Borislav S. Sabotinov
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * Returns a Docket - A builder which is intended to be the primary interface into the swagger-springmvc framework.
     * @return Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointsInfo());
    }

    /**
     *
     * @return ApiInfo object initialized with information about our API
     */
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("CS5392 CTL Checker API")
                .description("Back-end component for the CS5392 Formal Methods CTL checker website.")
                .contact(new Contact("Sivaranjani, Boris, Sneha, Sriram", "localhost:8080", "none"))
                .license("GNU Affero General Public License")
                .licenseUrl("http://www.gnu.org/licenses/")
                .version("1.0.0")
                .build();
    }
}
