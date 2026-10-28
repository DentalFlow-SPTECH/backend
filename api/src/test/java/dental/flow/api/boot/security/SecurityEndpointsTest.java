package dental.flow.api.boot.security;

import dental.flow.api.adapters.rest.in.controller.*;
import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.application.in.DailyMapUseCase;
import dental.flow.api.application.in.InventoryUseCase;
import dental.flow.api.application.in.QueryMonthlyDashboardUseCase;
import dental.flow.api.application.service.RegisterPatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, DashboardController.class, PatientController.class,
        InventoryController.class, DailyMapController.class})
@Import({SecurityConfig.class, AuthController.class, DashboardController.class, PatientController.class,
        InventoryController.class, DailyMapController.class})
@ImportAutoConfiguration({SecurityAutoConfiguration.class, ServletWebSecurityAutoConfiguration.class})
class SecurityEndpointsTest {
    @SpringBootConfiguration
    static class TestApplication {
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DatabaseUserDetailsService userDetailsService;
    @MockitoBean
    private QueryMonthlyDashboardUseCase dashboardUseCase;
    @MockitoBean
    private RegisterPatientService patientService;
    @MockitoBean
    private InventoryUseCase inventoryUseCase;
    @MockitoBean
    private DailyMapUseCase dailyMapUseCase;

    private DentalFlowUserPrincipal dentist;
    private DentalFlowUserPrincipal receptionist;

    @BeforeEach
    void setUp() {
        dentist = new DentalFlowUserPrincipal(1L, 10L, 20L, "Dra. Ana", "dentista", "$2a$10$invalid", "DENTISTA", true);
        receptionist = new DentalFlowUserPrincipal(2L, 10L, null, "Recepção", "recepcao", "$2a$10$invalid", "RECEPCIONISTA", true);
        when(userDetailsService.loadUserByUsername("dentista")).thenReturn(dentist);
        when(userDetailsService.loadUserByUsername("recepcao")).thenReturn(receptionist);
    }

    @Test
    void shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/dashboard").param("year", "2026").param("month", "10"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH001"));
    }

    @Test
    void shouldForbidReceptionistFinancialAccess() throws Exception {
        mockMvc.perform(get("/dashboard").with(user(receptionist))
                        .param("year", "2026").param("month", "10"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("AUTH002"));
    }

    @Test
    void shouldExposeAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/auth/me").with(user(dentist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.clinicId").value(10))
                .andExpect(jsonPath("$.data.dentistId").value(20))
                .andExpect(jsonPath("$.data.accessLevel").value("DENTISTA"));
    }

    @Test
    void shouldAllowReceptionistPatientAccessWithinOwnClinic() throws Exception {
        PatientResponseDTO patient = new PatientResponseDTO(
                3L, 10L, "Maria", "123", "Rua", "Centro", "01234", null, null, LocalDate.of(1990, 1, 1));
        when(patientService.getById(3L)).thenReturn(patient);

        mockMvc.perform(get("/patient/3").with(user(receptionist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Maria"));
    }

    @Test
    void shouldAllowDentistDashboardOnlyForOwnClinic() throws Exception {
        when(dashboardUseCase.query(2026, 10, 10L, null))
                .thenReturn(MonthlyDashboardResponseDTO.from(2026, 10, 10L, null, List.of()));

        mockMvc.perform(get("/dashboard").with(user(dentist))
                        .param("year", "2026").param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.clinicId").value(10));

        mockMvc.perform(get("/dashboard").with(user(dentist))
                        .param("year", "2026").param("month", "10").param("clinicId", "99"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldProtectInventoryAndDailyMapFromReceptionist() throws Exception {
        mockMvc.perform(get("/inventory/products").with(user(receptionist)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/daily-map").with(user(receptionist))
                        .param("start", "2026-10-01").param("end", "2026-10-31"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowDentistToReadInventoryAndDailyMap() throws Exception {
        when(inventoryUseCase.listProducts(10L, true)).thenReturn(List.of());
        when(dailyMapUseCase.list(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), 10L, 20L))
                .thenReturn(List.of());
        mockMvc.perform(get("/inventory/products").with(user(dentist)).param("active", "true"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/daily-map").with(user(dentist))
                        .param("start", "2026-10-01").param("end", "2026-10-31"))
                .andExpect(status().isOk());
    }
}
