package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.request.SaveDailyMapRequestDTO;
import dental.flow.api.application.out.DailyMapRepository;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.appointment.Appointment;
import dental.flow.api.domain.dailymap.DailyMap;
import dental.flow.api.domain.patient.Patient;
import dental.flow.api.domain.procedure.Procedure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyMapServiceTest {
    @Mock
    DailyMapRepository repository;
    @Mock
    RegisterAppointmentRepository appointments;
    @Mock
    RegisterPatientRepository patients;
    @Mock
    RegisterProcedureRepository procedures;
    @InjectMocks
    DailyMapService service;

    @Test
    void shouldCalculateAndCreateCompleteDailyMap() {
        SaveDailyMapRequestDTO request = request(true);
        when(repository.save(any(), eq(7L), eq("CRIAR"))).thenAnswer(i -> {
            DailyMap m = i.getArgument(0);
            m.restore(9L, "ABERTO", null, null, null);
            return m;
        });
        when(appointments.findById(20L)).thenReturn(Optional.of(appointment()));
        when(patients.findById(30L)).thenReturn(Optional.of(patient(1L)));
        when(procedures.findById(40L)).thenReturn(Optional.of(procedure(1L)));
        var response = service.create(1L, 2L, 7L, request);
        assertEquals(0, new BigDecimal("150.00").compareTo(response.generalGross()));
        assertEquals(0, new BigDecimal("20.00").compareTo(response.totalExpenses()));
        assertEquals(0, new BigDecimal("115.00").compareTo(response.finalBalance()));
        assertEquals(1, response.patientCount());
        assertEquals(9L, response.id());
    }

    @Test
    void shouldRejectDuplicateAndForeignReferences() {
        when(repository.exists(1L, 2L, LocalDate.of(2026, 10, 28))).thenReturn(true);
        assertThrows(BusinessException.class, () -> service.create(1L, 2L, 7L, request(true)));
        reset(repository);
        when(patients.findById(30L)).thenReturn(Optional.of(patient(99L)));
        when(appointments.findById(20L)).thenReturn(Optional.of(appointment()));
        assertThrows(BusinessException.class, () -> service.create(1L, 2L, 7L, request(true)));
    }

    @Test
    void shouldRequireChecksBeforeCloseThenClose() {
        DailyMap unchecked = domain(false);
        unchecked.restore(9L, "ABERTO", null, null, null);
        when(repository.findById(9L, 1L, 2L)).thenReturn(Optional.of(unchecked));
        assertThrows(DomainException.class, () -> service.close(9L, 1L, 2L, 7L));
        DailyMap checked = domain(true);
        checked.restore(9L, "ABERTO", null, null, null);
        when(repository.findById(9L, 1L, 2L)).thenReturn(Optional.of(checked));
        when(repository.close(any(), eq(7L))).thenAnswer(i -> i.getArgument(0));
        var response = service.close(9L, 1L, 2L, 7L);
        assertEquals("FECHADO", response.status());
        assertEquals(7L, response.checkingUserId());
    }

    @Test
    void shouldRejectUpdateOfClosedMapAndInvalidPeriod() {
        DailyMap closed = domain(true);
        closed.restore(9L, "FECHADO", 7L, LocalDate.now(), LocalDateTime.now());
        when(repository.findById(9L, 1L, 2L)).thenReturn(Optional.of(closed));
        assertThrows(BusinessException.class, () -> service.update(9L, 1L, 2L, 7L, request(true)));
        assertThrows(BusinessException.class, () -> service.list(LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 1), 1L, 2L));
    }

    private DailyMap domain(boolean checked) {
        return new DailyMap(1L, 2L, 7L, LocalDate.of(2026, 10, 28), new BigDecimal("5"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, 0, null, List.of(new DailyMap.Visit(null, 20L, 30L, 2L, 40L, 1, "F1", "Maria", LocalTime.of(9, 0), null, null, null, "PIX", new BigDecimal("150"), "CLINICO", checked)), List.of(new DailyMap.Voucher(null, "Ana", "F2", new BigDecimal("10"), "Vale", checked)), List.of(new DailyMap.Expense(null, "Material", new BigDecimal("20"))), List.of());
    }

    private SaveDailyMapRequestDTO request(boolean checked) {
        return new SaveDailyMapRequestDTO(LocalDate.of(2026, 10, 28), new BigDecimal("5"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, 0, 0, 0, null, List.of(new SaveDailyMapRequestDTO.VisitInput(20L, 30L, 40L, 1, "F1", "Maria", LocalTime.of(9, 0), null, null, null, "PIX", new BigDecimal("150"), "clinico", checked)), List.of(new SaveDailyMapRequestDTO.VoucherInput("Ana", "F2", new BigDecimal("10"), "Vale", checked)), List.of(new SaveDailyMapRequestDTO.ExpenseInput("Material", new BigDecimal("20"))), List.of());
    }

    private Appointment appointment() {
        return new Appointment(1L, 30L, 2L, 40L, 7L, LocalDateTime.of(2026, 10, 28, 9, 0), "CONCLUIDO", null);
    }

    private Patient patient(Long clinic) {
        return new Patient(clinic, "Maria", "123", "Rua", "Centro", "01234", null, null, null);
    }

    private Procedure procedure(Long clinic) {
        return new Procedure(clinic, "Limpeza", new BigDecimal("150"), 60);
    }
}
