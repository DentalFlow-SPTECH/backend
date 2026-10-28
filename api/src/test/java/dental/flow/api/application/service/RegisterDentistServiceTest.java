package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterDentistCommand;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.dentist.Dentist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterDentistServiceTest {
    @Mock
    private RegisterDentistRepository repository;
    @InjectMocks
    private RegisterDentistService service;

    @Test
    void shouldCoverDentistHappyFlows() {
        Dentist dentist = dentist(10L, "CRO-1");
        when(repository.existsByCro("CRO-1")).thenReturn(false);
        when(repository.register(any())).thenReturn("Dentist id:10 registered successfully");
        when(repository.findById(10L)).thenReturn(Optional.of(dentist));
        when(repository.findByClinicId(1L)).thenReturn(List.of(dentist));
        when(repository.update(any())).thenReturn(dentist);

        assertEquals(10L, service.register(command("CRO-1")).id());
        assertEquals("Dra. Ana", service.getById(10L).name());
        assertEquals(1, service.listByClinic(1L).size());
        assertEquals("CRO-1", service.update(10L, command("CRO-1")).cro());
    }

    @Test
    void shouldRejectDuplicateCroOnRegisterAndUpdate() {
        when(repository.existsByCro("CRO-1")).thenReturn(true);
        assertThrows(BusinessException.class, () -> service.register(command("CRO-1")));

        when(repository.findById(10L)).thenReturn(Optional.of(dentist(10L, "CRO-OLD")));
        assertThrows(BusinessException.class, () -> service.update(10L, command("CRO-1")));
    }

    @Test
    void shouldReportDentistNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertThrows(NotFoundException.class, () -> service.update(99L, command("CRO-1")));
    }

    private RegisterDentistCommand command(String cro) {
        return new RegisterDentistCommand(1L, "Dra. Ana", cro, "Ortodontia", "11999999999");
    }

    private Dentist dentist(Long id, String cro) {
        Dentist dentist = new Dentist(1L, "Dra. Ana", cro, "Ortodontia", "11999999999");
        dentist.setId(id);
        return dentist;
    }
}
