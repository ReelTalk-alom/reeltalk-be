package com.alom.reeltalkbe.global;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;


public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info().title("ReelTalk")
            .version("1.0")
            .description("ReelTalk API 문서"));
  }
}
