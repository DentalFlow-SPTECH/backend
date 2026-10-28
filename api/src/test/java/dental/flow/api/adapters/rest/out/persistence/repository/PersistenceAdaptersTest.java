package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.*;
import dental.flow.api.boot.exception.DatabaseException;
import dental.flow.api.domain.appointment.Appointment;
import dental.flow.api.domain.clinic.Clinic;
import dental.flow.api.domain.dentist.Dentist;
import dental.flow.api.domain.patient.Patient;
import dental.flow.api.domain.procedure.Procedure;
import dental.flow.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistenceAdaptersTest {
    @Mock
    private UserJpaRepository userJpa;
    @Mock
    private ClinicJpaRepository clinicJpa;
    @Mock
    private DentistJpaRepository dentistJpa;
    @Mock
    private PatientJpaRepository patientJpa;
    @Mock
    private ProcedureJpaRepository procedureJpa;
    @Mock
    private AppointmentJpaRepository appointmentJpa;
    @Mock
    private MonthlyDashboardJpaRepository dashboardJpa;

    @Test
    void shouldCoverUserPersistenceAdapter() {
        RegisterUserRepositoryImpl adapter = new RegisterUserRepositoryImpl(userJpa);
        User user = new User("Ana", "ana", "123456", 1L, "RECEPCIONISTA", true);
        UserEntity saved = new UserEntity("Ana", "ana", "hash", 1L, "RECEPCIONISTA", true);
        saved.setId(1L);
        when(userJpa.saveAndFlush(any())).thenReturn(saved);
        when(userJpa.existsByLogin("ana")).thenReturn(true);

        assertEquals("User id:1 registered successfully", adapter.register(user));
        assertTrue(adapter.existsByLogin(user));

        when(userJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(user));
    }

    @Test
    void shouldCoverClinicPersistenceAdapter() {
        RegisterClinicRepositoryImpl adapter = new RegisterClinicRepositoryImpl(clinicJpa);
        Clinic clinic = clinic(1L);
        ClinicEntity entity = new ClinicEntity("Clínica", "CNPJ", "Rua A", "11999999999");
        entity.setId(1L);
        when(clinicJpa.saveAndFlush(any())).thenReturn(entity);
        when(clinicJpa.existsByCnpj("CNPJ")).thenReturn(true);
        when(clinicJpa.findById(1L)).thenReturn(Optional.of(entity));

        assertEquals("Clinic id:1 registered successfully", adapter.register(clinic));
        assertTrue(adapter.existsByCnpj("CNPJ"));
        assertEquals(1L, adapter.findById(1L).orElseThrow().getId());
        assertEquals("Clínica", adapter.update(clinic).getName());

        when(clinicJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(clinic));
        assertThrows(DatabaseException.class, () -> adapter.update(clinic));
    }

    @Test
    void shouldCoverDentistPersistenceAdapter() {
        RegisterDentistRepositoryImpl adapter = new RegisterDentistRepositoryImpl(dentistJpa);
        Dentist dentist = dentist(2L);
        DentistEntity entity = new DentistEntity(1L, "Dra. Ana", "CRO-1", "Orto", "11999999999");
        entity.setId(2L);
        when(dentistJpa.saveAndFlush(any())).thenReturn(entity);
        when(dentistJpa.existsByCro("CRO-1")).thenReturn(true);
        when(dentistJpa.findById(2L)).thenReturn(Optional.of(entity));
        when(dentistJpa.findByClinic_Id(1L)).thenReturn(List.of(entity));

        assertEquals("Dentist id:2 registered successfully", adapter.register(dentist));
        assertTrue(adapter.existsByCro("CRO-1"));
        assertEquals(2L, adapter.findById(2L).orElseThrow().getId());
        assertEquals(1, adapter.findByClinicId(1L).size());
        assertEquals("Dra. Ana", adapter.update(dentist).getName());

        when(dentistJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(dentist));
        assertThrows(DatabaseException.class, () -> adapter.update(dentist));
    }

    @Test
    void shouldCoverPatientPersistenceAdapter() {
        RegisterPatientRepositoryImpl adapter = new RegisterPatientRepositoryImpl(patientJpa);
        Patient patient = patient(3L);
        PatientEntity entity = new PatientEntity(
                1L, "Maria", "123", "Rua A", "Centro", "01234-567",
                "11999999999", "maria@email.com", LocalDate.of(1990, 1, 1)
        );
        entity.setId(3L);
        when(patientJpa.saveAndFlush(any())).thenReturn(entity);
        when(patientJpa.existsByClinic_IdAndCpf(1L, "123")).thenReturn(true);
        when(patientJpa.findById(3L)).thenReturn(Optional.of(entity));
        when(patientJpa.findByClinic_Id(1L)).thenReturn(List.of(entity));

        assertEquals("Patient id:3 registered successfully", adapter.register(patient));
        assertTrue(adapter.existsByClinicIdAndCpf(1L, "123"));
        assertEquals(3L, adapter.findById(3L).orElseThrow().getId());
        assertEquals(1, adapter.findByClinicId(1L).size());
        assertEquals("Maria", adapter.update(patient).getName());

        when(patientJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(patient));
        assertThrows(DatabaseException.class, () -> adapter.update(patient));
    }

    @Test
    void shouldCoverProcedurePersistenceAdapter() {
        RegisterProcedureRepositoryImpl adapter = new RegisterProcedureRepositoryImpl(procedureJpa);
        Procedure procedure = procedure(4L);
        ProcedureEntity entity = new ProcedureEntity(1L, "Limpeza", new BigDecimal("150.00"), 60);
        entity.setId(4L);
        when(procedureJpa.saveAndFlush(any())).thenReturn(entity);
        when(procedureJpa.findById(4L)).thenReturn(Optional.of(entity));
        when(procedureJpa.findByClinic_Id(1L)).thenReturn(List.of(entity));

        assertEquals("Procedure id:4 registered successfully", adapter.register(procedure));
        assertEquals(4L, adapter.findById(4L).orElseThrow().getId());
        assertEquals(1, adapter.findByClinicId(1L).size());
        assertEquals("Limpeza", adapter.update(procedure).getName());

        when(procedureJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(procedure));
        assertThrows(DatabaseException.class, () -> adapter.update(procedure));
    }

    @Test
    void shouldCoverAppointmentPersistenceAdapter() {
        RegisterAppointmentRepositoryImpl adapter = new RegisterAppointmentRepositoryImpl(appointmentJpa);
        Appointment appointment = appointment(5L);
        AppointmentEntity entity = new AppointmentEntity(
                1L, 3L, 2L, 4L, 1L, LocalDateTime.of(2026, 10, 20, 9, 0), "AGENDADO", "Avaliação"
        );
        entity.setId(5L);
        when(appointmentJpa.saveAndFlush(any())).thenReturn(entity);
        when(appointmentJpa.findById(5L)).thenReturn(Optional.of(entity));
        when(appointmentJpa.findByClinic_IdAndDentist_IdAndScheduledAtBetween(eq(1L), eq(2L), any(), any())).thenReturn(List.of(entity));

        assertEquals("Appointment id:5 registered successfully", adapter.register(appointment));
        assertEquals(5L, adapter.findById(5L).orElseThrow().getId());
        assertEquals(1, adapter.findByClinicIdAndDentistIdAndDate(1L, 2L, LocalDate.of(2026, 10, 20)).size());
        assertEquals("AGENDADO", adapter.update(appointment).getStatus());

        when(appointmentJpa.saveAndFlush(any())).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.register(appointment));
        assertThrows(DatabaseException.class, () -> adapter.update(appointment));
    }

    @Test
    void shouldCoverMonthlyDashboardPersistenceAdapterAndNullDefaults() {
        MonthlyDashboardEntity entity = mock(MonthlyDashboardEntity.class);
        ClinicEntity clinic = new ClinicEntity();
        clinic.setName("Clínica");
        DentistEntity dentist = new DentistEntity();
        dentist.setName("Dra. Ana");
        when(entity.getClinicId()).thenReturn(1L);
        when(entity.getDentistId()).thenReturn(2L);
        when(entity.getClinic()).thenReturn(clinic);
        when(entity.getDentist()).thenReturn(dentist);
        when(entity.getYear()).thenReturn(2026);
        when(entity.getMonth()).thenReturn(10);
        when(dashboardJpa.findByPeriod(2026, 10, 1L, 2L)).thenReturn(List.of(entity));
        MonthlyDashboardRepositoryImpl adapter = new MonthlyDashboardRepositoryImpl(dashboardJpa);

        assertEquals(BigDecimal.ZERO, adapter.findByPeriod(2026, 10, 1L, 2L).getFirst().generalGross());

        when(dashboardJpa.findByPeriod(2026, 10, 1L, 2L)).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.findByPeriod(2026, 10, 1L, 2L));
    }

    @Test
    void shouldReadDashboardEvolutionAndAppointmentMetrics() {
        DailyDashboardProjection point = mock(DailyDashboardProjection.class);
        when(point.getMapDate()).thenReturn(LocalDate.of(2026, 10, 1));
        when(point.getGeneralGross()).thenReturn(new BigDecimal("1000.00"));
        when(point.getPatientCount()).thenReturn(10L);
        when(dashboardJpa.findDailyEvolution(2026, 10, 1L, null)).thenReturn(List.of(point));

        AppointmentMetricsProjection metrics = mock(AppointmentMetricsProjection.class);
        when(metrics.getTotalAppointments()).thenReturn(10L);
        when(metrics.getCompletedAppointments()).thenReturn(7L);
        when(metrics.getNoShowAppointments()).thenReturn(2L);
        when(dashboardJpa.findAppointmentMetrics(2026, 10, 1L, null)).thenReturn(metrics);

        MonthlyDashboardRepositoryImpl adapter = new MonthlyDashboardRepositoryImpl(dashboardJpa);
        assertEquals(new BigDecimal("1000.00"),
                adapter.findDailyEvolution(2026, 10, 1L, null).getFirst().generalGross());
        assertEquals(7L, adapter.findAppointmentMetrics(2026, 10, 1L, null).completed());

        when(dashboardJpa.findAppointmentMetrics(2026, 9, 1L, null)).thenReturn(null);
        assertEquals(0L, adapter.findAppointmentMetrics(2026, 9, 1L, null).total());

        when(dashboardJpa.findDailyEvolution(2026, 8, 1L, null)).thenThrow(dbError());
        when(dashboardJpa.findAppointmentMetrics(2026, 8, 1L, null)).thenThrow(dbError());
        assertThrows(DatabaseException.class, () -> adapter.findDailyEvolution(2026, 8, 1L, null));
        assertThrows(DatabaseException.class, () -> adapter.findAppointmentMetrics(2026, 8, 1L, null));
    }

    private DataRetrievalFailureException dbError() {
        return new DataRetrievalFailureException("database unavailable");
    }

    private Clinic clinic(Long id) {
        Clinic clinic = new Clinic("Clínica", "CNPJ", "Rua A", "11999999999");
        clinic.setId(id);
        return clinic;
    }

    private Dentist dentist(Long id) {
        Dentist dentist = new Dentist(1L, "Dra. Ana", "CRO-1", "Orto", "11999999999");
        dentist.setId(id);
        return dentist;
    }

    private Patient patient(Long id) {
        Patient patient = new Patient(1L, "Maria", "123", "Rua A", "Centro", "01234-567",
                "11999999999", "maria@email.com", LocalDate.of(1990, 1, 1));
        patient.setId(id);
        return patient;
    }

    private Procedure procedure(Long id) {
        Procedure procedure = new Procedure(1L, "Limpeza", new BigDecimal("150.00"), 60);
        procedure.setId(id);
        return procedure;
    }

    private Appointment appointment(Long id) {
        Appointment appointment = new Appointment(1L, 3L, 2L, 4L, 1L,
                LocalDateTime.of(2026, 10, 20, 9, 0), "AGENDADO", "Avaliação");
        appointment.setId(id);
        return appointment;
    }
}
