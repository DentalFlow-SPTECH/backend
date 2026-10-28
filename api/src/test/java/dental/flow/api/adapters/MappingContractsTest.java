package dental.flow.api.adapters;

import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterAppointmentRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterClinicRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterDentistRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterPatientRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.request.RegisterProcedureRequestDTO;
import dental.flow.api.adapters.rest.in.controller.mapper.AppointmentWebMapper;
import dental.flow.api.adapters.rest.in.controller.mapper.ClinicWebMapper;
import dental.flow.api.adapters.rest.in.controller.mapper.DentistWebMapper;
import dental.flow.api.adapters.rest.in.controller.mapper.PatientWebMapper;
import dental.flow.api.adapters.rest.in.controller.mapper.ProcedureWebMapper;
import dental.flow.api.adapters.rest.out.persistence.entity.AppointmentEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.ClinicEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.DentistEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.MonthlyDashboardEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.MonthlyDashboardId;
import dental.flow.api.adapters.rest.out.persistence.entity.PatientEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.ProcedureEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.UserEntity;
import dental.flow.api.adapters.rest.out.persistence.mapper.MonthlyDashboardPersistenceMapper;
import dental.flow.api.domain.appointment.Appointment;
import dental.flow.api.domain.clinic.Clinic;
import dental.flow.api.domain.dentist.Dentist;
import dental.flow.api.domain.patient.Patient;
import dental.flow.api.domain.procedure.Procedure;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappingContractsTest {
    @Test
    void shouldMapEveryWebResponseAndCommand() {
        Clinic clinic = new Clinic("Clínica", "CNPJ", "Rua", "11"); clinic.setId(1L);
        Dentist dentist = new Dentist(1L, "Ana", "CRO", "Orto", "11"); dentist.setId(2L);
        Patient patient = new Patient(1L, "Maria", "123", "Rua", "Centro", "01234", "11", "a@b.com", LocalDate.of(1990, 1, 1)); patient.setId(3L);
        Procedure procedure = new Procedure(1L, "Limpeza", BigDecimal.TEN, 30); procedure.setId(4L);
        Appointment appointment = new Appointment(1L, 3L, 2L, 4L, 1L, LocalDateTime.of(2026, 1, 1, 9, 0), "AGENDADO", null); appointment.setId(5L);

        assertEquals("Clínica", ClinicWebMapper.toResponse(clinic).name());
        assertEquals("Ana", DentistWebMapper.toResponse(dentist).name());
        assertEquals("Maria", PatientWebMapper.toResponse(patient).name());
        assertEquals("Limpeza", ProcedureWebMapper.toResponse(procedure).name());
        assertEquals(5L, AppointmentWebMapper.toResponse(appointment).id());

        assertEquals("Clínica", ClinicWebMapper.toCommand(new RegisterClinicRequestDTO("Clínica", "CNPJ", "Rua", "11")).name());
        assertEquals("CRO", DentistWebMapper.toCommand(new RegisterDentistRequestDTO(1L, "Ana", "CRO", "Orto", "11")).cro());
        assertEquals("123", PatientWebMapper.toCommand(new RegisterPatientRequestDTO(1L, "Maria", "123", "Rua", "Centro", "01234", "11", "a@b.com", LocalDate.of(1990, 1, 1))).cpf());
        assertEquals(BigDecimal.TEN, ProcedureWebMapper.toCommand(new RegisterProcedureRequestDTO(1L, "Limpeza", BigDecimal.TEN, 30)).price());
        assertEquals(2L, AppointmentWebMapper.toCommand(new RegisterAppointmentRequestDTO(1L, 3L, 2L, 4L, 1L, LocalDateTime.of(2026, 1, 1, 9, 0), "AGENDADO", null)).dentistId());
    }

    @Test
    void shouldCoverEntityRelationshipsAndMutableProperties() {
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(1L); clinic.setName("Clínica"); clinic.setCnpj("CNPJ"); clinic.setAddress("Rua"); clinic.setPhone("11");
        clinic.setUsers(List.of(new UserEntity())); clinic.setPatients(List.of(new PatientEntity()));
        clinic.setDentists(List.of(new DentistEntity())); clinic.setProcedures(List.of(new ProcedureEntity()));
        clinic.setAppointments(List.of(new AppointmentEntity()));
        assertEquals(1, clinic.getUsers().size()); assertEquals(1, clinic.getPatients().size());
        assertEquals(1, clinic.getDentists().size()); assertEquals(1, clinic.getProcedures().size());
        assertEquals(1, clinic.getAppointments().size()); assertEquals("CNPJ", clinic.getCnpj());

        UserEntity user = new UserEntity();
        assertNull(user.getClinicId()); user.setClinicId(1L); user.setClinicId(2L);
        user.setName("Ana"); user.setLogin("ana"); user.setPassword("hash"); user.setAccessLevel("DENTISTA"); user.setActive(true);
        user.setDentistId(2L);
        user.setAppointments(List.of(new AppointmentEntity())); user.setClinic(clinic);
        assertEquals("Ana", user.getName()); assertEquals("ana", user.getLogin()); assertEquals("hash", user.getPassword());
        assertEquals("DENTISTA", user.getAccessLevel()); assertEquals(2L, user.getDentistId());
        assertTrue(user.getActive()); assertEquals(1, user.getAppointments().size());
        user.setClinicId(null); assertNull(user.getClinic());

        DentistEntity dentist = new DentistEntity();
        assertNull(dentist.getClinicId()); dentist.setClinicId(1L); dentist.setClinicId(2L); dentist.setClinic(clinic);
        dentist.setName("Ana"); dentist.setCro("CRO"); dentist.setSpecialty("Orto"); dentist.setPhone("11");
        dentist.setAppointments(List.of(new AppointmentEntity()));
        assertEquals("Ana", dentist.getName()); assertEquals("CRO", dentist.getCro()); assertEquals("Orto", dentist.getSpecialty());
        assertEquals("11", dentist.getPhone()); assertEquals(1, dentist.getAppointments().size()); dentist.setClinicId(null);

        PatientEntity patient = new PatientEntity();
        assertNull(patient.getClinicId()); patient.setClinicId(1L); patient.setClinicId(2L); patient.setClinic(clinic);
        patient.setName("Maria"); patient.setCpf("123"); patient.setAddress("Rua"); patient.setNeighborhood("Centro");
        patient.setZipCode("01234"); patient.setPhone("11"); patient.setEmail("a@b.com"); patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setAppointments(List.of(new AppointmentEntity()));
        assertEquals("123", patient.getCpf()); assertEquals("Rua", patient.getAddress()); assertEquals("Centro", patient.getNeighborhood());
        assertEquals("01234", patient.getZipCode()); assertEquals("11", patient.getPhone()); assertEquals("a@b.com", patient.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate()); assertEquals(1, patient.getAppointments().size()); patient.setClinicId(null);

        ProcedureEntity procedure = new ProcedureEntity();
        assertNull(procedure.getClinicId()); procedure.setClinicId(1L); procedure.setClinicId(2L); procedure.setClinic(clinic);
        procedure.setName("Limpeza"); procedure.setPrice(BigDecimal.TEN); procedure.setDurationMinutes(30);
        procedure.setAppointments(List.of(new AppointmentEntity()));
        assertEquals("Limpeza", procedure.getName()); assertEquals(BigDecimal.TEN, procedure.getPrice());
        assertEquals(30, procedure.getDurationMinutes()); assertEquals(1, procedure.getAppointments().size()); procedure.setClinicId(null);

        AppointmentEntity appointment = new AppointmentEntity();
        assertNull(appointment.getClinicId()); assertNull(appointment.getPatientId()); assertNull(appointment.getDentistId());
        assertNull(appointment.getProcedureId()); assertNull(appointment.getUserId());
        appointment.setClinicId(1L); appointment.setClinicId(2L); appointment.setPatientId(3L); appointment.setPatientId(4L);
        appointment.setDentistId(5L); appointment.setDentistId(6L); appointment.setProcedureId(7L); appointment.setProcedureId(8L);
        appointment.setUserId(9L); appointment.setUserId(10L);
        appointment.setClinic(clinic); appointment.setPatient(patient); appointment.setDentist(dentist); appointment.setProcedure(procedure); appointment.setUser(user);
        LocalDateTime scheduledAt = LocalDateTime.of(2026, 1, 1, 9, 0);
        appointment.setScheduledAt(scheduledAt); appointment.setStatus("AGENDADO"); appointment.setNotes("Nota");
        assertEquals(scheduledAt, appointment.getScheduledAt()); assertEquals("AGENDADO", appointment.getStatus()); assertEquals("Nota", appointment.getNotes());
        assertEquals(clinic, appointment.getClinic()); assertEquals(patient, appointment.getPatient()); assertEquals(dentist, appointment.getDentist());
        assertEquals(procedure, appointment.getProcedure()); assertEquals(user, appointment.getUser());
        appointment.setClinicId(null); appointment.setPatientId(null); appointment.setDentistId(null); appointment.setProcedureId(null); appointment.setUserId(null);
    }

    @Test
    void shouldMapRealMonthlyDashboardEntity() {
        MonthlyDashboardEntity entity = new MonthlyDashboardEntity();
        ClinicEntity clinic = new ClinicEntity(); clinic.setName("Clínica");
        DentistEntity dentist = new DentistEntity(); dentist.setName("Dra. Ana");
        set(entity, "clinicId", 1L); set(entity, "dentistId", 2L); set(entity, "year", 2026); set(entity, "month", 10);
        set(entity, "clinic", clinic); set(entity, "dentist", dentist); set(entity, "registeredDays", 20L);
        for (String field : List.of("clinicalTotal", "prosthesisTotal", "clinicalGross", "pediatricSurgeryGross",
                "generalGross", "totalExpenses", "cardDiscount", "predatedCheckDiscount", "cashCheckDiscount",
                "voucherDiscount", "otherDiscount", "netTotal", "finalBalance")) {
            set(entity, field, BigDecimal.TEN);
        }
        for (String field : List.of("patientCount", "moldingCount", "biteCount", "fittingCount", "deliveryCount", "repetitionCount")) {
            set(entity, field, 1L);
        }
        assertEquals("Clínica", MonthlyDashboardPersistenceMapper.toDomain(entity).clinicName());
        assertEquals(BigDecimal.TEN, MonthlyDashboardPersistenceMapper.toDomain(entity).finalBalance());
    }

    @Test
    void shouldImplementMonthlyDashboardCompositeIdEquality() {
        MonthlyDashboardId id = new MonthlyDashboardId(1L, 2L, 2026, 10);
        MonthlyDashboardId equal = new MonthlyDashboardId(1L, 2L, 2026, 10);
        assertEquals(id, id);
        assertEquals(id, equal);
        assertEquals(id.hashCode(), equal.hashCode());
        assertNotEquals(id, new MonthlyDashboardId(9L, 2L, 2026, 10));
        assertNotEquals(id, new MonthlyDashboardId(1L, 9L, 2026, 10));
        assertNotEquals(id, new MonthlyDashboardId(1L, 2L, 2025, 10));
        assertNotEquals(id, new MonthlyDashboardId(1L, 2L, 2026, 9));
        assertNotEquals(id, null);
        assertNotEquals(id, "id");
        assertFalse(new MonthlyDashboardId().equals(id));
    }

    private void set(Object target, String field, Object value) {
        ReflectionTestUtils.setField(target, field, value);
    }
}
