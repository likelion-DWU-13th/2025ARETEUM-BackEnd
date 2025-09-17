package likelion.be.areteum.global.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info=@Info(title="축제사이트 API 명세서",
                description = "백엔드 API 명세서",
                version = "V1")
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi apiGroup(){
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi controllerGroup(){
        return GroupedOpenApi.builder()
                .group("controller")
                .pathsToExclude("/api/**")
                .build();
    }
}
