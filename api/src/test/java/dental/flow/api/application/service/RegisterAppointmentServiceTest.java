package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterAppointmentCommand;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.appointment.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterAppointmentServiceTest {
    @Mock private RegisterAppointmentRepository repository;
    @InjectMocks private RegisterAppointmentService service;

    @Test
    void shouldCoverAppointmentHappyFlowsAndSortAgenda() {
        Appointment first = appointment(15L, LocalDateTime.of(2026, 10, 20, 9, 0), "AGENDADO");
        Appointment second = appointment(16L, LocalDateTime.of(2026, 10, 20, 10, 0), "CONFIRMADO");
        when(repository.register(any())).thenReturn("Appointment id:15 registered successfully");
        when(repository.findById(15L)).thenReturn(Optional.of(first));
        when(repository.findByClinicIdAndDentistIdAndDate(1L, 20L, LocalDate.of(2026, 10, 20)))
                .thenReturn(List.of(second, first));
        when(repository.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(15L, service.register(command("AGENDADO")).id());
        assertEquals(15L, service.getById(15L).id());
        assertEquals(15L, service.listByDateAndDentist(LocalDate.of(2026, 10, 20), 1L, 20L).getFirst().id());
        assertEquals(15L, service.update(15L, command("CONFIRMADO")).id());
        assertEquals("CONCLUIDO", service.updateStatus(15L, "concluido").status());
    }

    @Test
    void shouldRejectInvalidStatusAndClosedAppointmentReopening() {
        assertThrows(DomainException.class, () -> service.register(command("INVALIDO")));

        when(repository.findById(15L)).thenReturn(Optional.of(
                appointment(15L, LocalDateTime.of(2026, 10, 20, 9, 0), "CANCELADO")));
        assertThrows(BusinessException.class, () -> service.updateStatus(15L, "AGENDADO"));

        when(repository.findById(16L)).thenReturn(Optional.of(
                appointment(16L, LocalDateTime.of(2026, 10, 20, 10, 0), "FALTOU")));
        assertThrows(BusinessException.class, () -> service.updateStatus(16L, "AGENDADO"));
    }

    @Test
    void shouldReportAppointmentNotFoundInEveryLookupFlow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertThrows(NotFoundException.class, () -> service.update(99L, command("AGENDADO")));
        assertThrows(NotFoundException.class, () -> service.updateStatus(99L, "CONFIRMADO"));

        when(repository.register(any())).thenReturn("sem identificador");
        when(repository.findById(null)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.register(command("AGENDADO")));
    }

    private RegisterAppointmentCommand command(String status) {
        return new RegisterAppointmentCommand(
                1L, 10L, 20L, 30L, 40L,
                LocalDateTime.of(2026, 10, 20, 9, 0), status, "Consulta inicial"
        );
    }

    private Appointment appointment(Long id, LocalDateTime date, String status) {
        Appointment appointment = new Appointment(1L, 10L, 20L, 30L, 40L, date, status, "Consulta inicial");
        appointment.setId(id);
        return appointment;
    }
}
