package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.*;
import dental.flow.api.application.out.DailyMapRepository;
import dental.flow.api.domain.dailymap.DailyMap;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DailyMapRepositoryImpl implements DailyMapRepository {
    private final DailyMapJpaRepository maps;
    private final VisitJpaRepository visits;
    private final VoucherJpaRepository vouchers;
    private final ExpenseJpaRepository expenses;
    private final TeamScheduleJpaRepository schedules;
    private final AuditJpaRepository audits;

    public DailyMapRepositoryImpl(DailyMapJpaRepository maps,
                                  VisitJpaRepository visits,
                                  VoucherJpaRepository vouchers,
                                  ExpenseJpaRepository expenses,
                                  TeamScheduleJpaRepository schedules,
                                  AuditJpaRepository audits) {
        this.maps = maps;
        this.visits = visits;
        this.vouchers = vouchers;
        this.expenses = expenses;
        this.schedules = schedules;
        this.audits = audits;
    }

    @Override
    @Transactional
    public DailyMap save(DailyMap map, Long userId, String action) {
        String previous = map.getId() == null ? null : maps.findById(map.getId()).map(this::summary).orElse(null);
        DailyMapEntity saved = maps.saveAndFlush(toEntity(map));
        replaceChildren(saved.getId(), map);
        audit(saved.getId(), action, previous, summary(saved), userId);
        return load(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DailyMap> findById(Long id, Long clinicId, Long dentistId) {
        return maps.findByIdAndClinicIdAndDentistId(id, clinicId, dentistId).map(this::load);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DailyMap> findByDate(Long clinicId, Long dentistId, LocalDate date) {
        return maps.findByClinicIdAndDentistIdAndDate(clinicId, dentistId, date).map(this::load);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyMap> list(Long clinicId, Long dentistId, LocalDate start, LocalDate end) {
        return maps.findByClinicIdAndDentistIdAndDateBetweenOrderByDate(clinicId, dentistId, start, end).stream().map(this::load).toList();
    }

    @Override
    public boolean exists(Long clinicId, Long dentistId, LocalDate date) {
        return maps.existsByClinicIdAndDentistIdAndDate(clinicId, dentistId, date);
    }

    @Override
    @Transactional
    public DailyMap close(DailyMap map, Long userId) {
        DailyMapEntity entity = maps.findByIdAndClinicIdAndDentistId(map.getId(), map.getClinicId(), map.getDentistId()).orElseThrow();
        String previous = summary(entity);
        entity.setStatus(map.getStatus());
        entity.setCheckingUserId(map.getCheckingUserId());
        entity.setConferenceDate(map.getConferenceDate());
        entity.setClosedAt(map.getClosedAt());
        DailyMapEntity saved = maps.saveAndFlush(entity);
        audit(saved.getId(), "FECHAR", previous, summary(saved), userId);
        return load(saved);
    }

    private void replaceChildren(Long id, DailyMap map) {
        visits.deleteByMap(id);
        vouchers.deleteByMap(id);
        expenses.deleteByMap(id);
        schedules.deleteByMap(id);
        visits.flush();
        vouchers.flush();
        expenses.flush();
        schedules.flush();
        visits.saveAll(map.getVisits().stream().map(v -> visit(id, v)).toList());
        vouchers.saveAll(map.getVouchers().stream().map(v -> voucher(id, v)).toList());
        expenses.saveAll(map.getExpenses().stream().map(v -> expense(id, v)).toList());
        schedules.saveAll(map.getTeamSchedules().stream().map(v -> schedule(id, v)).toList());
    }

    private DailyMap load(DailyMapEntity e) {
        List<DailyMap.Visit> vl = visits.findByDailyMapIdOrderByLineNumber(e.getId()).stream().map(this::visit).toList();
        List<DailyMap.Voucher> vo = vouchers.findByDailyMapIdOrderById(e.getId()).stream().map(this::voucher).toList();
        List<DailyMap.Expense> ex = expenses.findByDailyMapIdOrderById(e.getId()).stream().map(this::expense).toList();
        List<DailyMap.TeamSchedule> sc = schedules.findByDailyMapIdOrderById(e.getId()).stream().map(this::schedule).toList();
        DailyMap map = new DailyMap(
                e.getClinicId(),
                e.getDentistId(),
                e.getAttendantUserId(),
                e.getDate(),
                e.getCardDiscount(),
                e.getPredatedCheckDiscount(),
                e.getCashCheckDiscount(),
                e.getOtherDiscount(),
                e.getMoldingCount(),
                e.getBiteCount(),
                e.getFittingCount(),
                e.getDeliveryCount(),
                e.getRepetitionCount(),
                e.getNotes(),
                vl,
                vo,
                ex,
                sc
        );
        map.restore(e.getId(), e.getStatus(), e.getCheckingUserId(), e.getConferenceDate(), e.getClosedAt());
        return map;
    }

    private DailyMapEntity toEntity(DailyMap m) {
        DailyMapEntity e = new DailyMapEntity();
        e.setId(m.getId());
        e.setClinicId(m.getClinicId());
        e.setDentistId(m.getDentistId());
        e.setAttendantUserId(m.getAttendantUserId());
        e.setCheckingUserId(m.getCheckingUserId());
        e.setDate(m.getDate());
        e.setWeekDay(m.getDate().getDayOfWeek().name());
        e.setConferenceDate(m.getConferenceDate());
        e.setClosedAt(m.getClosedAt());
        e.setStatus(m.getStatus());
        e.setClinicalTotal(m.getClinicalTotal());
        e.setProsthesisTotal(m.getProsthesisTotal());
        e.setClinicalGross(m.getClinicalGross());
        e.setPediatricSurgeryGross(m.getPediatricSurgeryGross());
        e.setGeneralGross(m.getGeneralGross());
        e.setTotalExpenses(m.getTotalExpenses());
        e.setNetValue(m.getNetValue());
        e.setCardDiscount(m.getCardDiscount());
        e.setPredatedCheckDiscount(m.getPredatedCheckDiscount());
        e.setCashCheckDiscount(m.getCashCheckDiscount());
        e.setVoucherDiscount(m.getVoucherDiscount());
        e.setOtherDiscount(m.getOtherDiscount());
        e.setNetTotal(m.getNetTotal());
        e.setFinalBalance(m.getFinalBalance());
        e.setPatientCount(m.getPatientCount());
        e.setMoldingCount(m.getMoldingCount());
        e.setBiteCount(m.getBiteCount());
        e.setFittingCount(m.getFittingCount());
        e.setDeliveryCount(m.getDeliveryCount());
        e.setRepetitionCount(m.getRepetitionCount());
        e.setNotes(m.getNotes());
        return e;
    }

    private VisitEntity visit(Long id, DailyMap.Visit v) {
        VisitEntity e = new VisitEntity();
        e.setDailyMapId(id);
        e.setAppointmentId(v.appointmentId());
        e.setPatientId(v.patientId());
        e.setDentistId(v.dentistId());
        e.setProcedureId(v.procedureId());
        e.setLineNumber(v.lineNumber());
        e.setRecordNumber(v.recordNumber());
        e.setPatientName(v.patientName());
        e.setScheduledTime(v.scheduledTime());
        e.setEntryTime(v.entryTime());
        e.setExitTime(v.exitTime());
        e.setReturnTime(v.returnTime());
        e.setPaymentCode(v.paymentCode());
        e.setValue(v.value());
        e.setType(v.type());
        e.setChecked(Boolean.TRUE.equals(v.checked()));
        return e;
    }

    private DailyMap.Visit visit(VisitEntity e) {
        return new DailyMap.Visit(
                e.getId(),
                e.getAppointmentId(),
                e.getPatientId(),
                e.getDentistId(),
                e.getProcedureId(),
                e.getLineNumber(),
                e.getRecordNumber(),
                e.getPatientName(),
                e.getScheduledTime(),
                e.getEntryTime(),
                e.getExitTime(),
                e.getReturnTime(),
                e.getPaymentCode(),
                e.getValue(),
                e.getType(),
                e.getChecked()
        );
    }

    private VoucherEntity voucher(Long id, DailyMap.Voucher v) {
        VoucherEntity e = new VoucherEntity();
        e.setDailyMapId(id);
        e.setResponsibleName(v.responsibleName());
        e.setRecordNumber(v.recordNumber());
        e.setValue(v.value());
        e.setDescription(v.description());
        e.setChecked(Boolean.TRUE.equals(v.checked()));
        return e;
    }

    private DailyMap.Voucher voucher(VoucherEntity e) {
        return new DailyMap.Voucher(e.getId(), e.getResponsibleName(), e.getRecordNumber(), e.getValue(), e.getDescription(), e.getChecked());
    }

    private ExpenseEntity expense(Long id, DailyMap.Expense v) {
        ExpenseEntity e = new ExpenseEntity();
        e.setDailyMapId(id);
        e.setDescription(v.description());
        e.setValue(v.value());
        return e;
    }

    private DailyMap.Expense expense(ExpenseEntity e) {
        return new DailyMap.Expense(e.getId(), e.getDescription(), e.getValue());
    }

    private TeamScheduleEntity schedule(Long id, DailyMap.TeamSchedule v) {
        TeamScheduleEntity e = new TeamScheduleEntity();
        e.setDailyMapId(id);
        e.setUserId(v.userId());
        e.setEmployeeName(v.employeeName());
        e.setRole(v.role());
        e.setStartTime(v.startTime());
        e.setLunchOutTime(v.lunchOutTime());
        e.setLunchReturnTime(v.lunchReturnTime());
        e.setEndTime(v.endTime());
        return e;
    }

    private DailyMap.TeamSchedule schedule(TeamScheduleEntity e) {
        return new DailyMap.TeamSchedule(e.getId(), e.getUserId(), e.getEmployeeName(), e.getRole(), e.getStartTime(), e.getLunchOutTime(), e.getLunchReturnTime(), e.getEndTime());
    }

    private void audit(Long id, String action, String previous, String next, Long userId) {
        AuditEntity a = new AuditEntity();
        a.setTableName("mapa_diario");
        a.setRecordId(id);
        a.setAction(action);
        a.setPreviousValue(previous);
        a.setNewValue(next);
        a.setUserId(userId);
        a.setOccurredAt(LocalDateTime.now());
        audits.save(a);
    }

    private String summary(DailyMapEntity e) {
        return "status=" + e.getStatus() + ";bruto=" + e.getGeneralGross() + ";despesas=" + e.getTotalExpenses() + ";saldo=" + e.getFinalBalance();
    }
}
