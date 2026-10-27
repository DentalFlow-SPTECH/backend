package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.ProcedureResponseDTO;
import dental.flow.api.application.command.RegisterProcedureCommand;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.procedure.Procedure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterProcedureServiceTest {
    @Mock
    private RegisterProcedureRepository repository;

    @InjectMocks
    private RegisterProcedureService service;

    @Test
    void shouldRegisterProcedure() {
        RegisterProcedureCommand command = new RegisterProcedureCommand(1L, "Limpeza", new BigDecimal("150.00"), 45);

        when(repository.register(org.mockito.ArgumentMatchers.any())).thenReturn("Procedure id:7 registered successfully");
        when(repository.findById(7L)).thenReturn(Optional.of(new Procedure(1L, "Limpeza", new BigDecimal("150.00"), 45)));

        ProcedureResponseDTO response = service.register(command);
        assertEquals("Limpeza", response.name());
    }

    @Test
    void shouldRejectInvalidDuration() {
        RegisterProcedureCommand command = new RegisterProcedureCommand(1L, "Limpeza", new BigDecimal("150.00"), 0);
        assertThrows(DomainException.class, () -> service.register(command));
    }
}
