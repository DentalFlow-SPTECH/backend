package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.ClinicResponseDTO;
import dental.flow.api.application.command.RegisterClinicCommand;
import dental.flow.api.application.out.RegisterClinicRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.clinic.Clinic;
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
class RegisterClinicServiceTest {

    @Mock
    private RegisterClinicRepository registerClinicRepository;

    @InjectMocks
    private RegisterClinicService registerClinicService;

    @Test
    void shouldRegisterClinicWhenCnpjIsUnique() {
        RegisterClinicCommand command = new RegisterClinicCommand("DentalFlow", "12.345.678/0001-99", "Rua A, 10", "11999999999");

        when(registerClinicRepository.existsByCnpj(anyString())).thenReturn(false);
        when(registerClinicRepository.register(org.mockito.ArgumentMatchers.any())).thenReturn("Clinic id:10 registered successfully");
        when(registerClinicRepository.findById(10L)).thenReturn(Optional.of(new Clinic("DentalFlow", "12.345.678/0001-99", "Rua A, 10", "11999999999")));

        ClinicResponseDTO response = registerClinicService.register(command);

        assertEquals("DentalFlow", response.name());
    }

    @Test
    void shouldRejectDuplicateCnpj() {
        RegisterClinicCommand command = new RegisterClinicCommand("DentalFlow", "12.345.678/0001-99", "Rua A, 10", "11999999999");

        when(registerClinicRepository.existsByCnpj("12.345.678/0001-99")).thenReturn(true);

        assertThrows(BusinessException.class, () -> registerClinicService.register(command));
    }

    @Test
    void shouldRejectInvalidDomainData() {
        RegisterClinicCommand command = new RegisterClinicCommand("", "12.345.678/0001-99", "Rua A, 10", "11999999999");

        assertThrows(DomainException.class, () -> registerClinicService.register(command));
    }
}
