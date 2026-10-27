package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.response.PatientResponseDTO;
import dental.flow.api.application.command.RegisterPatientCommand;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.patient.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterPatientServiceTest {

    @Mock
    private RegisterPatientRepository registerPatientRepository;

    @InjectMocks
    private RegisterPatientService registerPatientService;

    @Test
    void shouldRegisterPatientWhenCpfIsUnique() {
        RegisterPatientCommand command = new RegisterPatientCommand(
                1L,
                "Ana Silva",
                "12345678909",
                "Rua A, 10",
                "Centro",
                "01000-000",
                "11999999999",
                "ana@email.com",
                LocalDate.of(1990, 1, 10)
        );

        when(registerPatientRepository.existsByClinicIdAndCpf(anyLong(), anyString())).thenReturn(false);
        when(registerPatientRepository.register(org.mockito.ArgumentMatchers.any())).thenReturn("Patient id:10 registered successfully");
        when(registerPatientRepository.findById(10L)).thenReturn(Optional.of(new Patient(
                1L,
                "Ana Silva",
                "12345678909",
                "Rua A, 10",
                "Centro",
                "01000-000",
                "11999999999",
                "ana@email.com",
                LocalDate.of(1990, 1, 10)
        )));

        PatientResponseDTO response = registerPatientService.register(command);

        assertEquals("Ana Silva", response.name());
    }

    @Test
    void shouldRejectDuplicateCpfInSameClinic() {
        RegisterPatientCommand command = new RegisterPatientCommand(
                1L,
                "Ana Silva",
                "12345678909",
                "Rua A, 10",
                "Centro",
                "01000-000",
                "11999999999",
                "ana@email.com",
                LocalDate.of(1990, 1, 10)
        );

        when(registerPatientRepository.existsByClinicIdAndCpf(1L, "12345678909")).thenReturn(true);

        assertThrows(BusinessException.class, () -> registerPatientService.register(command));
    }

    @Test
    void shouldRejectInvalidDomainData() {
        RegisterPatientCommand command = new RegisterPatientCommand(
                null,
                "Ana Silva",
                "12345678909",
                "Rua A, 10",
                "Centro",
                "01000-000",
                "11999999999",
                "ana@email.com",
                LocalDate.of(1990, 1, 10)
        );

        assertThrows(DomainException.class, () -> registerPatientService.register(command));
    }
}
