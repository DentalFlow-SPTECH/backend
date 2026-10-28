package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.MonthlyDashboardResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.application.in.QueryMonthlyDashboardUseCase;
import dental.flow.api.application.in.RegisterUserUseCase;
import dental.flow.api.application.service.RegisterAppointmentService;
import dental.flow.api.application.service.RegisterClinicService;
import dental.flow.api.application.service.RegisterDentistService;
import dental.flow.api.application.service.RegisterPatientService;
import dental.flow.api.application.service.RegisterProcedureService;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.boot.exception.GlobalExceptionHandler;
import dental.flow.api.boot.security.DentalFlowUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class ControllerEndpointsTest {
    @Mock private RegisterUserUseCase userUseCase;
    @Mock private RegisterClinicService clinicService;
    @Mock private RegisterDentistService dentistService;
    @Mock private RegisterPatientService patientService;
    @Mock private RegisterProcedureService procedureService;
    @Mock private RegisterAppointmentService appointmentService;
    @Mock private QueryMonthlyDashboardUseCase dashboardUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        DentalFlowUserPrincipal principal = new DentalFlowUserPrincipal(
                1L, 1L, 2L, "Dra. Ana", "ana", "hash", "DENTISTA", true
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities())
        );
        mockMvc = standaloneSetup(
                new UserController(userUseCase),
                new ClinicController(clinicService),
                new DentistController(dentistService),
                new PatientController(patientService),
                new ProcedureController(procedureService),
                new AppointmentController(appointmentService),
                new DashboardController(dashboardUseCase)
        ).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void shouldPerformUserRegistration() throws Exception {
        when(userUseCase.register(any())).thenReturn(new RegisterUserResponseDTO("User id:1 registered successfully"));

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ana","login":"ana","password":"123456","clinicId":1,
                                 "dentistId":2,"accessLevel":"DENTISTA","active":true}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.message").value("User id:1 registered successfully"));
    }

    @Test
    void shouldPerformEveryClinicEndpoint() throws Exception {
        ClinicResponseDTO response = new ClinicResponseDTO(1L, "DentalFlow", "12.345.678/0001-99", "Rua A", "11999999999");
        when(clinicService.register(any())).thenReturn(response);
        when(clinicService.getById(1L)).thenReturn(response);
        when(clinicService.update(eq(1L), any())).thenReturn(response);

        String body = """
                {"name":"DentalFlow","cnpj":"12.345.678/0001-99","address":"Rua A","phone":"11999999999"}
                """;
        mockMvc.perform(post("/clinic").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.data.name").value("DentalFlow"));
        mockMvc.perform(get("/clinic/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(1));
        mockMvc.perform(put("/clinic/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.cnpj").value("12.345.678/0001-99"));
    }

    @Test
    void shouldPerformEveryDentistEndpoint() throws Exception {
        DentistResponseDTO response = new DentistResponseDTO(2L, 1L, "Dra. Ana", "CRO-123", "Ortodontia", "11999999999");
        when(dentistService.register(any())).thenReturn(response);
        when(dentistService.getById(2L)).thenReturn(response);
        when(dentistService.listByClinic(1L)).thenReturn(List.of(response));
        when(dentistService.update(eq(2L), any())).thenReturn(response);

        String body = """
                {"clinicId":1,"name":"Dra. Ana","cro":"CRO-123","specialty":"Ortodontia","phone":"11999999999"}
                """;
        mockMvc.perform(post("/dentist").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.data.cro").value("CRO-123"));
        mockMvc.perform(get("/dentist/2"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.name").value("Dra. Ana"));
        mockMvc.perform(get("/dentist").param("clinicId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].id").value(2));
        mockMvc.perform(put("/dentist/2").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPerformEveryPatientEndpoint() throws Exception {
        PatientResponseDTO response = new PatientResponseDTO(
                3L, 1L, "Maria", "123.456.789-00", "Rua B", "Centro", "01234-567",
                "11988888888", "maria@email.com", LocalDate.of(1990, 1, 1)
        );
        when(patientService.register(any())).thenReturn(response);
        when(patientService.getById(3L)).thenReturn(response);
        when(patientService.listByClinicAndName(1L, "Maria")).thenReturn(List.of(response));
        when(patientService.update(eq(3L), any())).thenReturn(response);

        String body = """
                {"clinicId":1,"name":"Maria","cpf":"123.456.789-00","address":"Rua B",
                 "neighborhood":"Centro","zipCode":"01234-567","phone":"11988888888",
                 "email":"maria@email.com","birthDate":"1990-01-01"}
                """;
        mockMvc.perform(post("/patient").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.data.cpf").value("123.456.789-00"));
        mockMvc.perform(get("/patient/3"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.email").value("maria@email.com"));
        mockMvc.perform(get("/patient").param("clinicId", "1").param("name", "Maria"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].name").value("Maria"));
        mockMvc.perform(put("/patient/3").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPerformEveryProcedureEndpoint() throws Exception {
        ProcedureResponseDTO response = new ProcedureResponseDTO(4L, 1L, "Limpeza", new BigDecimal("150.00"), 60);
        when(procedureService.register(any())).thenReturn(response);
        when(procedureService.getById(4L)).thenReturn(response);
        when(procedureService.listByClinic(1L)).thenReturn(List.of(response));
        when(procedureService.update(eq(4L), any())).thenReturn(response);

        String body = """
                {"clinicId":1,"name":"Limpeza","price":150.00,"durationMinutes":60}
                """;
        mockMvc.perform(post("/procedure").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.data.price").value(150.0));
        mockMvc.perform(get("/procedure/4"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.name").value("Limpeza"));
        mockMvc.perform(get("/procedure").param("clinicId", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].durationMinutes").value(60));
        mockMvc.perform(put("/procedure/4").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPerformEveryAppointmentEndpoint() throws Exception {
        AppointmentResponseDTO response = new AppointmentResponseDTO(
                5L, 1L, 3L, 2L, 4L, 1L,
                LocalDateTime.of(2026, 10, 22, 9, 30), "CONFIRMADO", "Avaliação"
        );
        when(appointmentService.register(any())).thenReturn(response);
        when(appointmentService.getById(5L)).thenReturn(response);
        when(appointmentService.listByDateAndDentist(LocalDate.of(2026, 10, 22), 1L, 2L)).thenReturn(List.of(response));
        when(appointmentService.update(eq(5L), any())).thenReturn(response);
        when(appointmentService.updateStatus(5L, "CONFIRMADO")).thenReturn(response);

        String body = """
                {"clinicId":1,"patientId":3,"dentistId":2,"procedureId":4,"userId":1,
                 "scheduledAt":"2026-10-22T09:30:00","status":"CONFIRMADO","notes":"Avaliação"}
                """;
        mockMvc.perform(post("/appointment").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.data.status").value("CONFIRMADO"));
        mockMvc.perform(get("/appointment/5"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.patientId").value(3));
        mockMvc.perform(get("/appointment").param("date", "2026-10-22").param("dentistId", "2"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data[0].id").value(5));
        mockMvc.perform(put("/appointment/5").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/appointment/5/status").param("status", "CONFIRMADO"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.status").value("CONFIRMADO"));
    }

    @Test
    void shouldPerformDashboardEndpoint() throws Exception {
        MonthlyDashboardResponseDTO response = MonthlyDashboardResponseDTO.from(2026, 10, 1L, null, List.of());
        when(dashboardUseCase.query(2026, 10, 1L, null)).thenReturn(response);

        mockMvc.perform(get("/dashboard").param("year", "2026").param("month", "10").param("clinicId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.year").value(2026))
                .andExpect(jsonPath("$.data.summary.totalOutflows").value(0))
                .andExpect(jsonPath("$.data.breakdown").isEmpty());
    }

    @Test
    void shouldSerializeDomainErrorsThroughGlobalHandler() throws Exception {
        when(clinicService.getById(1L))
                .thenThrow(new DomainException(Failures.DOMAIN_ERROR, "Clínica inválida."));

        mockMvc.perform(get("/clinic/1"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("DMN001"))
                .andExpect(jsonPath("$.message").value("Clínica inválida."));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(post("/clinic").contentType(MediaType.APPLICATION_JSON).content("{invalid"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
