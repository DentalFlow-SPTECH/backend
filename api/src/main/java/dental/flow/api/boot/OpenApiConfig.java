package dental.flow.api.boot;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "basicAuth";

    @Bean
    OpenAPI dentalFlowOpenApi() {
        Info information = new Info()
                .title("DentalFlow API")
                .version("v1")
                .description("API para gestão de clínicas odontológicas, agenda, pacientes, dashboard, mapa diário e inventário.");

        SecurityScheme basicAuthentication = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");

        return new OpenAPI()
                .info(information)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME, basicAuthentication));
    }
}
