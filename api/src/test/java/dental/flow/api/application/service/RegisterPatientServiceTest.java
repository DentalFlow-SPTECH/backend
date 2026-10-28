package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterPatientCommand;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.NotFoundException;
import dental.flow.api.domain.patient.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterPatientServiceTest {
    @Mock private RegisterPatientRepository repository;
    @InjectMocks private RegisterPatientService service;

    @Test
    void shouldCoverPatientHappyFlowsAndNameFilters() {
        Patient maria = patient(7L, 1L, "Maria Souza", "123.456.789-00");
        Patient joao = patient(8L, 1L, "João Lima", "987.654.321-00");
        when(repository.existsByClinicIdAndCpf(1L, "123.456.789-00")).thenReturn(false);
        when(repository.register(any())).thenReturn("Patient id:7 registered successfully");
        when(repository.findById(7L)).thenReturn(Optional.of(maria));
        when(repository.findByClinicId(1L)).thenReturn(List.of(maria, joao));
        when(repository.update(any())).thenReturn(maria);

        assertEquals(7L, service.register(command(1L, "Maria Souza", "123.456.789-00")).id());
        assertEquals("Maria Souza", service.getById(7L).name());
        assertEquals(2, service.listByClinicAndName(1L, null).size());
        assertEquals(2, service.listByClinicAndName(1L, " ").size());
        assertEquals(1, service.listByClinicAndName(1L, "maria").size());
        assertEquals(7L, service.update(7L, command(1L, "Maria Souza", "123.456.789-00")).id());
    }

    @Test
    void shouldRejectDuplicateCpfOnRegisterAndChangedIdentityOnUpdate() {
        when(repository.existsByClinicIdAndCpf(1L, "123.456.789-00")).thenReturn(true);
        assertThrows(BusinessException.class,
                () -> service.register(command(1L, "Maria", "123.456.789-00")));

        when(repository.findById(7L)).thenReturn(Optional.of(patient(7L, 1L, "Maria", "111.111.111-11")));
        assertThrows(BusinessException.class,
                () -> service.update(7L, command(1L, "Maria", "123.456.789-00")));
    }

    @Test
    void shouldAllowUpdateWhenCpfChangesToUniqueValue() {
        Patient existing = patient(7L, 1L, "Maria", "111.111.111-11");
        Patient updated = patient(7L, 2L, "Maria", "123.456.789-00");
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        when(repository.existsByClinicIdAndCpf(2L, "123.456.789-00")).thenReturn(false);
        when(repository.update(any())).thenReturn(updated);
        assertEquals(2L, service.update(7L, command(2L, "Maria", "123.456.789-00")).clinicId());
    }

    @Test
    void shouldReportPatientNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(99L));
        assertThrows(NotFoundException.class,
                () -> service.update(99L, command(1L, "Maria", "123.456.789-00")));
    }

    private RegisterPatientCommand command(Long clinicId, String name, String cpf) {
        return new RegisterPatientCommand(
                clinicId, name, cpf, "Rua A", "Centro", "01234-567",
                "11999999999", "MARIA@EMAIL.COM", LocalDate.of(1990, 1, 1)
        );
    }

    private Patient patient(Long id, Long clinicId, String name, String cpf) {
        Patient patient = new Patient(
                clinicId, name, cpf, "Rua A", "Centro", "01234-567",
                "11999999999", "maria@email.com", LocalDate.of(1990, 1, 1)
        );
        patient.setId(id);
        return patient;
    }
}
