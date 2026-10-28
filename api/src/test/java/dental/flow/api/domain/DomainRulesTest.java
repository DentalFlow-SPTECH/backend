package dental.flow.api.domain;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.appointment.Appointment;
import dental.flow.api.domain.clinic.Clinic;
import dental.flow.api.domain.dentist.Dentist;
import dental.flow.api.domain.patient.Patient;
import dental.flow.api.domain.procedure.Procedure;
import dental.flow.api.domain.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainRulesTest {
    @Test
    void shouldValidateAndNormalizeUser() {
        User user = new User(" Ana ", " ANA ", "123456", 1L, "recepcionista", null);
        assertEquals("Ana", user.getName());
        assertEquals("ana", user.getLogin());
        assertEquals("RECEPCIONISTA", user.getAccessLevel());
        assertTrue(user.getActive());

        assertThrows(DomainException.class, () -> new User(null, "ana", "123456", 1L));
        assertThrows(DomainException.class, () -> new User(" ", "ana", "123456", 1L));
        assertThrows(DomainException.class, () -> new User("a".repeat(101), "ana", "123456", 1L));
        assertThrows(DomainException.class, () -> new User("Ana", null, "123456", 1L));
        assertThrows(DomainException.class, () -> new User("Ana", "a".repeat(51), "123456", 1L));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", null, 1L));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", "12345", 1L));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", "123456", 0L));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", "123456", 1L, null, "DENTISTA", true));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", "123456", 1L, 2L, "RECEPCIONISTA", true));
        assertThrows(DomainException.class, () -> new User("Ana", "ana", "123456", 1L, null, "ADMIN", true));
        assertEquals(2L, new User("Dr. Ana", "dra", "123456", 1L, 2L, "DENTISTA", true).getDentistId());
    }

    @Test
    void shouldValidateAndNormalizeClinic() {
        Clinic clinic = new Clinic(" Clínica ", " ", " ", " ");
        assertEquals("Clínica", clinic.getName());
        assertNull(clinic.getCnpj());
        assertNull(clinic.getAddress());
        assertNull(clinic.getPhone());

        assertThrows(DomainException.class, () -> new Clinic(null, null, null, null));
        assertThrows(DomainException.class, () -> new Clinic("a".repeat(101), null, null, null));
        assertThrows(DomainException.class, () -> new Clinic("A", "1".repeat(19), null, null));
        assertThrows(DomainException.class, () -> new Clinic("A", null, "a".repeat(151), null));
        assertThrows(DomainException.class, () -> new Clinic("A", null, null, "1".repeat(21)));
    }

    @Test
    void shouldValidateAndNormalizeDentist() {
        Dentist dentist = new Dentist(1L, " Dra. Ana ", " CRO-1 ", " ", " ");
        assertEquals("Dra. Ana", dentist.getName());
        assertEquals("CRO-1", dentist.getCro());
        assertNull(dentist.getSpecialty());
        assertNull(dentist.getPhone());

        assertThrows(DomainException.class, () -> new Dentist(null, "Ana", "CRO", null, null));
        assertThrows(DomainException.class, () -> new Dentist(1L, " ", "CRO", null, null));
        assertThrows(DomainException.class, () -> new Dentist(1L, "a".repeat(101), "CRO", null, null));
        assertThrows(DomainException.class, () -> new Dentist(1L, "Ana", null, null, null));
        assertThrows(DomainException.class, () -> new Dentist(1L, "Ana", "1".repeat(21), null, null));
        assertThrows(DomainException.class, () -> new Dentist(1L, "Ana", "CRO", "a".repeat(51), null));
        assertThrows(DomainException.class, () -> new Dentist(1L, "Ana", "CRO", null, "1".repeat(21)));
    }

    @Test
    void shouldValidateAndNormalizePatient() {
        Patient patient = patient(" Maria ", " MARIA@EMAIL.COM ", null);
        assertEquals("Maria", patient.getName());
        assertEquals("maria@email.com", patient.getEmail());

        assertThrows(DomainException.class, () -> patientWith(null, "Maria", "123", "Rua", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, " ", "123", "Rua", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "a".repeat(101), "123", "Rua", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", null, "Rua", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "1".repeat(15), "Rua", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", " ", "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "a".repeat(151), "Centro", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "Rua", " ", "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "Rua", "a".repeat(81), "01234", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "Rua", "Centro", " ", null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "Rua", "Centro", "1".repeat(10), null));
        assertThrows(DomainException.class, () -> new Patient(1L, "Maria", "123", "Rua", "Centro", "01234", "1".repeat(21), null, null));
        assertThrows(DomainException.class, () -> new Patient(1L, "Maria", "123", "Rua", "Centro", "01234", null, "a".repeat(101), null));
        assertThrows(DomainException.class, () -> patientWith(1L, "Maria", "123", "Rua", "Centro", "01234", LocalDate.now().plusDays(1)));
    }

    @Test
    void shouldValidateProcedure() {
        Procedure procedure = new Procedure(1L, " Limpeza ", null, null);
        assertEquals("Limpeza", procedure.getName());
        assertNull(procedure.getPrice());

        assertThrows(DomainException.class, () -> new Procedure(null, "Limpeza", null, null));
        assertThrows(DomainException.class, () -> new Procedure(1L, " ", null, null));
        assertThrows(DomainException.class, () -> new Procedure(1L, "a".repeat(101), null, null));
        assertThrows(DomainException.class, () -> new Procedure(1L, "Limpeza", new BigDecimal("-0.01"), null));
        assertThrows(DomainException.class, () -> new Procedure(1L, "Limpeza", null, 0));
    }

    @Test
    void shouldValidateNormalizeAndClassifyAppointment() {
        Appointment appointment = appointment(null, " ");
        assertEquals("AGENDADO", appointment.getStatus());
        assertNull(appointment.getNotes());
        assertTrue(appointment.isConflictCandidate());

        appointment.setStatus(" cancelado ");
        assertEquals("CANCELADO", appointment.getStatus());
        assertFalse(appointment.isConflictCandidate());
        appointment.setStatus("FALTOU");
        assertFalse(appointment.isConflictCandidate());
        appointment.setStatus(null);
        assertEquals("AGENDADO", appointment.getStatus());
        appointment.setNotes(" observação ");
        assertEquals("observação", appointment.getNotes());

        assertThrows(DomainException.class, () -> new Appointment(null, 2L, 3L, 4L, null, LocalDateTime.now(), null, null));
        assertThrows(DomainException.class, () -> new Appointment(1L, null, 3L, 4L, null, LocalDateTime.now(), null, null));
        assertThrows(DomainException.class, () -> new Appointment(1L, 2L, null, 4L, null, LocalDateTime.now(), null, null));
        assertThrows(DomainException.class, () -> new Appointment(1L, 2L, 3L, null, null, LocalDateTime.now(), null, null));
        assertThrows(DomainException.class, () -> new Appointment(1L, 2L, 3L, 4L, null, null, null, null));
        assertThrows(DomainException.class, () -> appointment("INVALIDO", null));
        assertThrows(DomainException.class, () -> appointment("AGENDADO", "a".repeat(256)));
        assertThrows(DomainException.class, () -> appointment.setStatus("INVALIDO"));
    }

    private Patient patient(String name, String email, LocalDate birthDate) {
        return new Patient(1L, name, "123.456.789-00", "Rua A", "Centro", "01234-567", null, email, birthDate);
    }

    private Patient patientWith(Long clinicId, String name, String cpf, String address,
                                String neighborhood, String zipCode, LocalDate birthDate) {
        return new Patient(clinicId, name, cpf, address, neighborhood, zipCode, null, null, birthDate);
    }

    private Appointment appointment(String status, String notes) {
        return new Appointment(1L, 2L, 3L, 4L, null, LocalDateTime.of(2026, 10, 20, 9, 0), status, notes);
    }
}
