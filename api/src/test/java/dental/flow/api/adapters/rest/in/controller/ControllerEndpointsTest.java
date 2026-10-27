package dental.flow.api.adapters.rest.in.controller;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterAppointmentRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterClinicRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterDentistRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterPatientRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterProcedureRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterUserRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ApiResponse;
import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.RegisterUserResponseDTO;
import dental.flow.api.application.in.RegisterUserUseCase;
import dental.flow.api.application.service.RegisterAppointmentService;
import dental.flow.api.application.service.RegisterClinicService;
import dental.flow.api.application.service.RegisterDentistService;
import dental.flow.api.application.service.RegisterPatientService;
import dental.flow.api.application.service.RegisterProcedureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerEndpointsTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private RegisterClinicService registerClinicService;

    @Mock
    private RegisterDentistService registerDentistService;

    @Mock
    private RegisterPatientService registerPatientService;

    @Mock
    private RegisterProcedureService registerProcedureService;

    @Mock
    private RegisterAppointmentService registerAppointmentService;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private ClinicController clinicController;

    @InjectMocks
    private DentistController dentistController;

    @InjectMocks
    private PatientController patientController;

    @InjectMocks
    private ProcedureController procedureController;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        userController = new UserController(registerUserUseCase);
        clinicController = new ClinicController(registerClinicService);
        dentistController = new DentistController(registerDentistService);
        patientController = new PatientController(registerPatientService);
        procedureController = new ProcedureController(registerProcedureService);
        appointmentController = new AppointmentController(registerAppointmentService);
    }

    @Test
    void shouldCallUserControllerRegisterEndpoint() {
        RegisterUserResponseDTO responseDto = new RegisterUserResponseDTO("Usuário cadastrado com sucesso.");
        when(registerUserUseCase.register(any())).thenReturn(responseDto);

        ResponseEntity<ApiResponse<RegisterUserResponseDTO>> response = userController.register(
                new RegisterUserRequestDTO("Ana", "ana", "123456", 1L, "ADMIN", true)
        );

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Usuário cadastrado com sucesso.", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void shouldCallClinicControllerEndpoints() {
        ClinicResponseDTO clinicResponse = new ClinicResponseDTO(1L, "DentalFlow", "12.345.678/0001-99", "Rua A, 10", "11999999999");
        when(registerClinicService.register(any())).thenReturn(clinicResponse);
        when(registerClinicService.getById(1L)).thenReturn(clinicResponse);
        when(registerClinicService.update(eq(1L), any())).thenReturn(clinicResponse);

        ResponseEntity<ApiResponse<ClinicResponseDTO>> created = clinicController.register(
                new RegisterClinicRequestDTO("DentalFlow", "12.345.678/0001-99", "Rua A, 10", "11999999999")
        );
        ResponseEntity<ApiResponse<ClinicResponseDTO>> found = clinicController.getById(1L);
        ResponseEntity<ApiResponse<ClinicResponseDTO>> updated = clinicController.update(
                1L,
                new RegisterClinicRequestDTO("DentalFlow", "12.345.678/0001-99", "Rua B, 20", "11888888888")
        );

        assertEquals(201, created.getStatusCode().value());
        assertEquals("DentalFlow", created.getBody().data().name());
        assertEquals(200, found.getStatusCode().value());
        assertEquals("DentalFlow", found.getBody().data().name());
        assertEquals(200, updated.getStatusCode().value());
        assertEquals("DentalFlow", updated.getBody().data().name());
    }

    @Test
    void shouldCallDentistControllerEndpoints() {
        DentistResponseDTO response = new DentistResponseDTO(1L, 5L, "Dr. Carlos", "123456", "Ortodontia", "11988888888");
        when(registerDentistService.register(any())).thenReturn(response);
        when(registerDentistService.getById(1L)).thenReturn(response);
        when(registerDentistService.listByClinic(5L)).thenReturn(List.of(response));
        when(registerDentistService.update(eq(1L), any())).thenReturn(response);

        ResponseEntity<ApiResponse<DentistResponseDTO>> created = dentistController.register(
                new RegisterDentistRequestDTO(5L, "Dr. Carlos", "123456", "Ortodontia", "11988888888")
        );
        ResponseEntity<ApiResponse<DentistResponseDTO>> found = dentistController.getById(1L);
        ResponseEntity<ApiResponse<List<DentistResponseDTO>>> listed = dentistController.listByClinic(5L);
        ResponseEntity<ApiResponse<DentistResponseDTO>> updated = dentistController.update(
                1L,
                new RegisterDentistRequestDTO(5L, "Dr. Carlos", "789012", "Ortodontia", "11988888888")
        );

        assertEquals(201, created.getStatusCode().value());
        assertEquals("123456", found.getBody().data().cro());
        assertEquals("Dr. Carlos", listed.getBody().data().getFirst().name());
        assertEquals(200, updated.getStatusCode().value());
    }

    @Test
    void shouldCallPatientControllerEndpoints() {
        PatientResponseDTO response = new PatientResponseDTO(
                1L,
                5L,
                "Maria Souza",
                "123.456.789-00",
                "Rua C, 30",
                "Centro",
                "01234-567",
                "11977777777",
                "maria@email.com",
                LocalDate.of(1995, 8, 15)
        );
        when(registerPatientService.register(any())).thenReturn(response);
        when(registerPatientService.getById(1L)).thenReturn(response);
        when(registerPatientService.listByClinicAndName(5L, "Maria")).thenReturn(List.of(response));
        when(registerPatientService.update(eq(1L), any())).thenReturn(response);

        ResponseEntity<ApiResponse<PatientResponseDTO>> created = patientController.register(
                new RegisterPatientRequestDTO(5L, "Maria Souza", "123.456.789-00", "Rua C, 30", "Centro", "01234-567", "11977777777", "maria@email.com", LocalDate.of(1995, 8, 15))
        );
        ResponseEntity<ApiResponse<PatientResponseDTO>> found = patientController.getById(1L);
        ResponseEntity<ApiResponse<List<PatientResponseDTO>>> listed = patientController.listByClinicAndName(5L, "Maria");
        ResponseEntity<ApiResponse<PatientResponseDTO>> updated = patientController.update(
                1L,
                new RegisterPatientRequestDTO(5L, "Maria Souza", "123.456.789-00", "Rua D, 40", "Centro", "01234-567", "11977777777", "maria@email.com", LocalDate.of(1995, 8, 15))
        );

        assertEquals(201, created.getStatusCode().value());
        assertEquals("Maria Souza", found.getBody().data().name());
        assertEquals("maria@email.com", listed.getBody().data().getFirst().email());
        assertEquals(200, updated.getStatusCode().value());
    }

    @Test
    void shouldCallProcedureControllerEndpoints() {
        ProcedureResponseDTO response = new ProcedureResponseDTO(1L, 5L, "Limpeza", new BigDecimal("150.00"), 60);
        when(registerProcedureService.register(any())).thenReturn(response);
        when(registerProcedureService.getById(1L)).thenReturn(response);
        when(registerProcedureService.listByClinic(5L)).thenReturn(List.of(response));
        when(registerProcedureService.update(eq(1L), any())).thenReturn(response);

        ResponseEntity<ApiResponse<ProcedureResponseDTO>> created = procedureController.register(
                new RegisterProcedureRequestDTO(5L, "Limpeza", new BigDecimal("150.00"), 60)
        );
        ResponseEntity<ApiResponse<ProcedureResponseDTO>> found = procedureController.getById(1L);
        ResponseEntity<ApiResponse<List<ProcedureResponseDTO>>> listed = procedureController.listByClinic(5L);
        ResponseEntity<ApiResponse<ProcedureResponseDTO>> updated = procedureController.update(
                1L,
                new RegisterProcedureRequestDTO(5L, "Limpeza", new BigDecimal("180.00"), 60)
        );

        assertEquals(201, created.getStatusCode().value());
        assertEquals("Limpeza", found.getBody().data().name());
        assertEquals("Limpeza", listed.getBody().data().getFirst().name());
        assertEquals(200, updated.getStatusCode().value());
    }

    @Test
    void shouldCallAppointmentControllerEndpoints() {
        AppointmentResponseDTO response = new AppointmentResponseDTO(
                1L,
                5L,
                10L,
                7L,
                3L,
                2L,
                LocalDateTime.of(2026, 10, 22, 9, 30),
                "CONFIRMADO",
                "Consulta de avaliação"
        );
        when(registerAppointmentService.register(any())).thenReturn(response);
        when(registerAppointmentService.getById(1L)).thenReturn(response);
        when(registerAppointmentService.listByDateAndDentist(LocalDate.of(2026, 10, 22), 7L)).thenReturn(List.of(response));
        when(registerAppointmentService.update(eq(1L), any())).thenReturn(response);
        when(registerAppointmentService.updateStatus(1L, "CONFIRMADO")).thenReturn(response);

        ResponseEntity<ApiResponse<AppointmentResponseDTO>> created = appointmentController.register(
                new RegisterAppointmentRequestDTO(5L, 10L, 7L, 3L, 2L, LocalDateTime.of(2026, 10, 22, 9, 30), "CONFIRMADO", "Consulta de avaliação")
        );
        ResponseEntity<ApiResponse<AppointmentResponseDTO>> found = appointmentController.getById(1L);
        ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> listed = appointmentController.listByDateAndDentist(LocalDate.of(2026, 10, 22), 7L);
        ResponseEntity<ApiResponse<AppointmentResponseDTO>> updated = appointmentController.update(
                1L,
                new RegisterAppointmentRequestDTO(5L, 10L, 7L, 3L, 2L, LocalDateTime.of(2026, 10, 22, 10, 0), "CONFIRMADO", "Consulta de avaliação")
        );
        ResponseEntity<ApiResponse<AppointmentResponseDTO>> statusUpdated = appointmentController.updateStatus(1L, "CONFIRMADO");

        assertEquals(201, created.getStatusCode().value());
        assertEquals("CONFIRMADO", found.getBody().data().status());
        assertEquals(10L, listed.getBody().data().getFirst().patientId());
        assertEquals(200, updated.getStatusCode().value());
        assertEquals("CONFIRMADO", statusUpdated.getBody().data().status());
    }
}
