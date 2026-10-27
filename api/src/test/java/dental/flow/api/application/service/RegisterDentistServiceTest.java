package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.DentistResponseDTO;
import dental.flow.api.application.command.RegisterDentistCommand;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.dentist.Dentist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterDentistServiceTest {
    @Mock
    private RegisterDentistRepository repository;

    @InjectMocks
    private RegisterDentistService service;

    @Test
    void shouldRegisterDentistWhenCroIsUnique() {
        RegisterDentistCommand command = new RegisterDentistCommand(1L, "Dr. João", "12345", "Ortodontia", "11999999999");

        when(repository.existsByCro(anyString())).thenReturn(false);
        when(repository.register(org.mockito.ArgumentMatchers.any())).thenReturn("Dentist id:10 registered successfully");
        when(repository.findById(10L)).thenReturn(Optional.of(new Dentist(1L, "Dr. João", "12345", "Ortodontia", "11999999999")));

        DentistResponseDTO response = service.register(command);
        assertEquals("Dr. João", response.name());
    }

    @Test
    void shouldRejectDuplicateCro() {
        RegisterDentistCommand command = new RegisterDentistCommand(1L, "Dr. João", "12345", "Ortodontia", "11999999999");
        when(repository.existsByCro("12345")).thenReturn(true);

        assertThrows(BusinessException.class, () -> service.register(command));
    }

    @Test
    void shouldRejectInvalidDomainData() {
        RegisterDentistCommand command = new RegisterDentistCommand(null, "", "12345", "Ortodontia", "11999999999");
        assertThrows(DomainException.class, () -> service.register(command));
    }
}
