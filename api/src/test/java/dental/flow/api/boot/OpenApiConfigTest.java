package dental.flow.api.boot;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenApiConfigTest {

    @Test
    void shouldConfigureApiInformationAndBasicAuthentication() {
        OpenAPI openApi = new OpenApiConfig().dentalFlowOpenApi();

        assertEquals("DentalFlow API", openApi.getInfo().getTitle());
        assertEquals("v1", openApi.getInfo().getVersion());
        assertNotNull(openApi.getComponents().getSecuritySchemes().get("basicAuth"));
        assertEquals("basic", openApi.getComponents().getSecuritySchemes().get("basicAuth").getScheme());
    }
}
