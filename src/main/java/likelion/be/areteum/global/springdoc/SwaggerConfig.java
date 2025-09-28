package likelion.be.areteum.global.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "축제사이트 API 명세서",
                description = "백엔드 API 명세서",
                version = "V1"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi controllerGroup() {
        return GroupedOpenApi.builder()
                .group("controller")
                .pathsToExclude("/api/**")
                .build();
    }

    // 추가: 서버 URL을 상대경로로 고정 (프록시/HTTPS 환경에서 Failed to fetch 방지)
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().servers(List.of(
                new Server().url("/") // 현재 열려있는 도메인을 그대로 사용
        ));
    }
}
