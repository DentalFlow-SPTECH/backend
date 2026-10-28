package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.DailyMapEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.ExpenseEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.TeamScheduleEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.VisitEntity;
import dental.flow.api.adapters.rest.out.persistence.entity.VoucherEntity;
import dental.flow.api.domain.dailymap.DailyMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyMapRepositoryImplTest {

    @Mock
    DailyMapJpaRepository maps;
    @Mock
    VisitJpaRepository visits;
    @Mock
    VoucherJpaRepository vouchers;
    @Mock
    ExpenseJpaRepository expenses;
    @Mock
    TeamScheduleJpaRepository schedules;
    @Mock
    AuditJpaRepository audits;
    @InjectMocks
    DailyMapRepositoryImpl repository;

    private DailyMapEntity entity;

    @BeforeEach
    void setUp() {
        entity = mapEntity();
        when(visits.findByDailyMapIdOrderByLineNumber(1L)).thenReturn(List.of(visitEntity()));
        when(vouchers.findByDailyMapIdOrderById(1L)).thenReturn(List.of(voucherEntity()));
        when(expenses.findByDailyMapIdOrderById(1L)).thenReturn(List.of(expenseEntity()));
        when(schedules.findByDailyMapIdOrderById(1L)).thenReturn(List.of(scheduleEntity()));
    }

    @Test
    void shouldSaveMapAndReplaceItsChildren() {
        when(maps.saveAndFlush(any())).thenAnswer(invocation -> {
            DailyMapEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        DailyMap saved = repository.save(map(), 7L, "CRIAR");

        assertEquals(1L, saved.getId());
        assertEquals(1, saved.getVisits().size());
        assertEquals(new BigDecimal("90"), saved.getFinalBalance());
        verify(visits).deleteByMap(1L);
        verify(vouchers).deleteByMap(1L);
        verify(expenses).deleteByMap(1L);
        verify(schedules).deleteByMap(1L);
        verify(visits).saveAll(any());
        verify(vouchers).saveAll(any());
        verify(expenses).saveAll(any());
        verify(schedules).saveAll(any());
        verify(audits).save(any());
    }

    @Test
    void shouldFindListCheckExistenceAndCloseMap() {
        when(maps.findByIdAndClinicIdAndDentistId(1L, 2L, 3L)).thenReturn(Optional.of(entity));
        when(maps.findByClinicIdAndDentistIdAndDate(2L, 3L, entity.getDate())).thenReturn(Optional.of(entity));
        when(maps.findByClinicIdAndDentistIdAndDateBetweenOrderByDate(
                2L, 3L, entity.getDate(), entity.getDate())).thenReturn(List.of(entity));
        when(maps.existsByClinicIdAndDentistIdAndDate(2L, 3L, entity.getDate())).thenReturn(true);
        when(maps.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertTrue(repository.findById(1L, 2L, 3L).isPresent());
        assertTrue(repository.findByDate(2L, 3L, entity.getDate()).isPresent());
        assertEquals(1, repository.list(2L, 3L, entity.getDate(), entity.getDate()).size());
        assertTrue(repository.exists(2L, 3L, entity.getDate()));

        DailyMap map = repository.findById(1L, 2L, 3L).orElseThrow();
        map.close(7L);
        DailyMap closed = repository.close(map, 7L);

        assertEquals("FECHADO", closed.getStatus());
        assertEquals(7L, closed.getCheckingUserId());
        verify(audits).save(any());
    }

    private DailyMap map() {
        DailyMap.Visit visit = new DailyMap.Visit(
                null, 11L, 12L, 3L, 13L, 1, "F-1", "Maria",
                LocalTime.of(9, 0), LocalTime.of(9, 5), LocalTime.of(10, 0), null,
                "PIX", new BigDecimal("100"), "CLINICO", true
        );
        DailyMap.Voucher voucher = new DailyMap.Voucher(
                null, "Responsável", "F-1", BigDecimal.ZERO, "Sem desconto", true
        );
        DailyMap.Expense expense = new DailyMap.Expense(null, "Material", new BigDecimal("10"));
        DailyMap.TeamSchedule schedule = new DailyMap.TeamSchedule(
                null, 7L, "Ana", "DENTISTA", LocalTime.of(8, 0),
                LocalTime.NOON, LocalTime.of(13, 0), LocalTime.of(18, 0)
        );
        return new DailyMap(
                2L, 3L, 7L, LocalDate.of(2026, 10, 28),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                1, 2, 3, 4, 0, "Dia normal",
                List.of(visit), List.of(voucher), List.of(expense), List.of(schedule)
        );
    }

    private DailyMapEntity mapEntity() {
        DailyMapEntity value = new DailyMapEntity();
        value.setId(1L);
        value.setClinicId(2L);
        value.setDentistId(3L);
        value.setAttendantUserId(7L);
        value.setDate(LocalDate.of(2026, 10, 28));
        value.setWeekDay("WEDNESDAY");
        value.setStatus("ABERTO");
        value.setClinicalTotal(new BigDecimal("100"));
        value.setProsthesisTotal(BigDecimal.ZERO);
        value.setClinicalGross(new BigDecimal("100"));
        value.setPediatricSurgeryGross(BigDecimal.ZERO);
        value.setGeneralGross(new BigDecimal("100"));
        value.setTotalExpenses(new BigDecimal("10"));
        value.setNetValue(new BigDecimal("100"));
        value.setCardDiscount(BigDecimal.ZERO);
        value.setPredatedCheckDiscount(BigDecimal.ZERO);
        value.setCashCheckDiscount(BigDecimal.ZERO);
        value.setVoucherDiscount(BigDecimal.ZERO);
        value.setOtherDiscount(BigDecimal.ZERO);
        value.setNetTotal(new BigDecimal("100"));
        value.setFinalBalance(new BigDecimal("90"));
        value.setPatientCount(1);
        value.setMoldingCount(1);
        value.setBiteCount(2);
        value.setFittingCount(3);
        value.setDeliveryCount(4);
        value.setRepetitionCount(0);
        value.setNotes("Dia normal");
        return value;
    }

    private VisitEntity visitEntity() {
        VisitEntity value = new VisitEntity();
        value.setId(20L);
        value.setDailyMapId(1L);
        value.setAppointmentId(11L);
        value.setPatientId(12L);
        value.setDentistId(3L);
        value.setProcedureId(13L);
        value.setLineNumber(1);
        value.setRecordNumber("F-1");
        value.setPatientName("Maria");
        value.setScheduledTime(LocalTime.of(9, 0));
        value.setEntryTime(LocalTime.of(9, 5));
        value.setExitTime(LocalTime.of(10, 0));
        value.setPaymentCode("PIX");
        value.setValue(new BigDecimal("100"));
        value.setType("CLINICO");
        value.setChecked(true);
        return value;
    }

    private VoucherEntity voucherEntity() {
        VoucherEntity value = new VoucherEntity();
        value.setId(21L);
        value.setDailyMapId(1L);
        value.setResponsibleName("Responsável");
        value.setRecordNumber("F-1");
        value.setValue(BigDecimal.ZERO);
        value.setDescription("Sem desconto");
        value.setChecked(true);
        return value;
    }

    private ExpenseEntity expenseEntity() {
        ExpenseEntity value = new ExpenseEntity();
        value.setId(22L);
        value.setDailyMapId(1L);
        value.setDescription("Material");
        value.setValue(new BigDecimal("10"));
        return value;
    }

    private TeamScheduleEntity scheduleEntity() {
        TeamScheduleEntity value = new TeamScheduleEntity();
        value.setId(23L);
        value.setDailyMapId(1L);
        value.setUserId(7L);
        value.setEmployeeName("Ana");
        value.setRole("DENTISTA");
        value.setStartTime(LocalTime.of(8, 0));
        value.setLunchOutTime(LocalTime.NOON);
        value.setLunchReturnTime(LocalTime.of(13, 0));
        value.setEndTime(LocalTime.of(18, 0));
        return value;
    }
}
