package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.AppointmentResponseDTO;
import dental.flow.api.application.command.RegisterAppointmentCommand;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.appointment.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterAppointmentServiceTest {
    @Mock
    private RegisterAppointmentRepository repository;

    @InjectMocks
    private RegisterAppointmentService service;

    @Test
    void shouldRegisterAppointment() {
        RegisterAppointmentCommand command = new RegisterAppointmentCommand(
                1L,
                10L,
                20L,
                30L,
                40L,
                LocalDateTime.of(2026, 10, 20, 9, 0),
                "AGENDADO",
                "Consulta inicial"
        );

        when(repository.register(org.mockito.ArgumentMatchers.any())).thenReturn("Appointment id:15 registered successfully");
        when(repository.findById(15L)).thenReturn(Optional.of(new Appointment(
                1L, 10L, 20L, 30L, 40L,
                LocalDateTime.of(2026, 10, 20, 9, 0),
                "AGENDADO",
                "Consulta inicial"
        )));

        AppointmentResponseDTO response = service.register(command);
        assertEquals("AGENDADO", response.status());
    }

    @Test
    void shouldRejectInvalidStatus() {
        RegisterAppointmentCommand command = new RegisterAppointmentCommand(
                1L,
                10L,
                20L,
                30L,
                40L,
                LocalDateTime.of(2026, 10, 20, 9, 0),
                "INVALIDO",
                "Consulta inicial"
        );

        assertThrows(DomainException.class, () -> service.register(command));
    }

    @Test
    void shouldRejectReopeningCancelledAppointment() {
        Appointment appointment = new Appointment(1L, 10L, 20L, 30L, 40L,
                LocalDateTime.of(2026, 10, 20, 9, 0), "CANCELADO", "Consulta inicial");
        when(repository.findById(15L)).thenReturn(Optional.of(appointment));

        assertThrows(BusinessException.class, () -> service.updateStatus(15L, "AGENDADO"));
    }
}
