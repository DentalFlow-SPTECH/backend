package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterProcedureCommand;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.procedure.Procedure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterProcedureServiceTest {
    @Mock private RegisterProcedureRepository repository;
    @InjectMocks private RegisterProcedureService service;

    @Test
    void shouldCoverProcedureHappyFlows() {
        Procedure procedure = procedure(5L);
        when(repository.register(any())).thenReturn("Procedure id:5 registered successfully");
        when(repository.findById(5L)).thenReturn(Optional.of(procedure));
        when(repository.findByClinicId(1L)).thenReturn(List.of(procedure));
        when(repository.update(any())).thenReturn(procedure);

        assertEquals(5L, service.register(command()).id());
        assertEquals("Limpeza", service.getById(5L).name());
        assertEquals(1, service.listByClinic(1L).size());
        assertEquals(new BigDecimal("150.00"), service.update(5L, command()).price());
    }

    @Test
    void shouldReportProcedureNotFoundInAllLookupFlows() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertThrows(NotFoundException.class, () -> service.update(99L, command()));

        when(repository.register(any())).thenReturn("sem identificador");
        when(repository.findById(null)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.register(command()));
    }

    private RegisterProcedureCommand command() {
        return new RegisterProcedureCommand(1L, "Limpeza", new BigDecimal("150.00"), 60);
    }

    private Procedure procedure(Long id) {
        Procedure procedure = new Procedure(1L, "Limpeza", new BigDecimal("150.00"), 60);
        procedure.setId(id);
        return procedure;
    }
}
