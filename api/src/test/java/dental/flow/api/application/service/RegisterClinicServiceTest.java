package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterClinicCommand;
import dental.flow.api.application.out.RegisterClinicRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.clinic.Clinic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterClinicServiceTest {
    @Mock
    private RegisterClinicRepository repository;
    @InjectMocks
    private RegisterClinicService service;

    @Test
    void shouldRegisterGetAndUpdateClinic() {
        RegisterClinicCommand command = command("12.345.678/0001-99");
        Clinic saved = clinic(10L, "12.345.678/0001-99");
        when(repository.existsByCnpj("12.345.678/0001-99")).thenReturn(false);
        when(repository.register(any())).thenReturn("Clinic id:10 registered successfully");
        when(repository.findById(10L)).thenReturn(Optional.of(saved));
        when(repository.update(any())).thenReturn(saved);

        assertEquals(10L, service.register(command).id());
        assertEquals("DentalFlow", service.getById(10L).name());
        assertEquals(10L, service.update(10L, command).id());
    }

    @Test
    void shouldRegisterClinicWithoutCnpj() {
        Clinic saved = clinic(11L, null);
        when(repository.register(any())).thenReturn("Clinic id:11 registered successfully");
        when(repository.findById(11L)).thenReturn(Optional.of(saved));
        assertEquals(11L, service.register(command(null)).id());
        verify(repository, never()).existsByCnpj(any());
    }

    @Test
    void shouldRejectDuplicateCnpjOnRegisterAndUpdate() {
        when(repository.existsByCnpj("12.345.678/0001-99")).thenReturn(true);
        assertThrows(BusinessException.class, () -> service.register(command("12.345.678/0001-99")));

        when(repository.findById(10L)).thenReturn(Optional.of(clinic(10L, "11.111.111/0001-11")));
        assertThrows(BusinessException.class, () -> service.update(10L, command("12.345.678/0001-99")));
    }

    @Test
    void shouldReportClinicNotFoundInEveryLookupFlow() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertThrows(NotFoundException.class, () -> service.update(99L, command(null)));

        when(repository.register(any())).thenReturn("sem identificador");
        when(repository.findById(null)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.register(command(null)));
    }

    private RegisterClinicCommand command(String cnpj) {
        return new RegisterClinicCommand("DentalFlow", cnpj, "Rua A", "11999999999");
    }

    private Clinic clinic(Long id, String cnpj) {
        Clinic clinic = new Clinic("DentalFlow", cnpj, "Rua A", "11999999999");
        clinic.setId(id);
        return clinic;
    }
}
