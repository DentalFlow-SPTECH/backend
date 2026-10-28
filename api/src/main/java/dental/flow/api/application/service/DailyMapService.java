package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.request.SaveDailyMapRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.DailyMapResponseDTO;
import dental.flow.api.application.in.DailyMapUseCase;
import dental.flow.api.application.out.DailyMapRepository;
import dental.flow.api.application.out.RegisterAppointmentRepository;
import dental.flow.api.application.out.RegisterPatientRepository;
import dental.flow.api.application.out.RegisterProcedureRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.appointment.Appointment;
import dental.flow.api.domain.dailymap.DailyMap;
import dental.flow.api.domain.patient.Patient;
import dental.flow.api.domain.procedure.Procedure;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DailyMapService implements DailyMapUseCase {
    private final DailyMapRepository repository;
    private final RegisterAppointmentRepository appointments;
    private final RegisterPatientRepository patients;
    private final RegisterProcedureRepository procedures;

    public DailyMapService(DailyMapRepository repository,
                           RegisterAppointmentRepository appointments,
                           RegisterPatientRepository patients,
                           RegisterProcedureRepository procedures) {
        this.repository = repository;
        this.appointments = appointments;
        this.patients = patients;
        this.procedures = procedures;
    }

    @Override
    public DailyMapResponseDTO create(Long clinicId, Long dentistId, Long userId, SaveDailyMapRequestDTO request) {
        if (repository.exists(clinicId, dentistId, request.date())) {
            throw rule("Já existe um mapa para este dentista nesta data.");
        }
        DailyMap map = map(clinicId, dentistId, userId, request);
        validateReferences(map);
        return DailyMapResponseDTO.from(repository.save(map, userId, "CRIAR"));
    }

    @Override
    public DailyMapResponseDTO update(Long id, Long clinicId, Long dentistId, Long userId, SaveDailyMapRequestDTO request) {
        DailyMap current = find(id, clinicId, dentistId);
        if ("FECHADO".equals(current.getStatus())) {
            throw rule("Mapa fechado não pode ser alterado.");
        }
        boolean dateChanged = !current.getDate().equals(request.date());
        if (dateChanged && repository.exists(clinicId, dentistId, request.date())) {
            throw rule("Já existe um mapa para este dentista nesta data.");
        }
        DailyMap map = map(clinicId, dentistId, userId, request);
        map.restore(id, "ABERTO", null, null, null);
        validateReferences(map);
        return DailyMapResponseDTO.from(repository.save(map, userId, "ATUALIZAR"));
    }

    @Override
    public DailyMapResponseDTO get(Long id, Long clinicId, Long dentistId) {
        return DailyMapResponseDTO.from(find(id, clinicId, dentistId));
    }

    @Override
    public DailyMapResponseDTO getByDate(LocalDate date, Long clinicId, Long dentistId) {
        return DailyMapResponseDTO.from(repository.findByDate(clinicId, dentistId, date).orElseThrow(() -> notFound()));
    }

    @Override
    public List<DailyMapResponseDTO> list(LocalDate start, LocalDate end, Long clinicId, Long dentistId) {
        if (start == null || end == null || end.isBefore(start)) {
            throw rule("O período do mapa diário é inválido.");
        }

        List<DailyMapResponseDTO> response = new ArrayList<>();
        for (DailyMap map : repository.list(clinicId, dentistId, start, end)) {
            response.add(DailyMapResponseDTO.from(map));
        }
        return response;
    }

    @Override
    public DailyMapResponseDTO close(Long id, Long clinicId, Long dentistId, Long userId) {
        DailyMap map = find(id, clinicId, dentistId);
        map.close(userId);
        return DailyMapResponseDTO.from(repository.close(map, userId));
    }

    private DailyMap find(Long id, Long clinicId, Long dentistId) {
        return repository.findById(id, clinicId, dentistId).orElseThrow(() -> notFound());
    }

    private BusinessException notFound() {
        return new BusinessException(Failures.NOT_FOUND, "Mapa diário não encontrado.");
    }

    private BusinessException rule(String message) {
        return new BusinessException(Failures.BUSINESS_FAILURE, message);
    }

    private DailyMap map(Long clinicId, Long dentistId, Long userId, SaveDailyMapRequestDTO request) {
        List<DailyMap.Visit> visits = mapVisits(request.visits(), dentistId);
        List<DailyMap.Voucher> vouchers = mapVouchers(request.vouchers());
        List<DailyMap.Expense> expenses = mapExpenses(request.expenses());
        List<DailyMap.TeamSchedule> schedules = mapSchedules(request.teamSchedules());

        return new DailyMap(
                clinicId,
                dentistId,
                userId,
                request.date(),
                request.cardDiscount(),
                request.predatedCheckDiscount(),
                request.cashCheckDiscount(),
                request.otherDiscount(),
                request.moldingCount(),
                request.biteCount(),
                request.fittingCount(),
                request.deliveryCount(),
                request.repetitionCount(),
                request.notes(),
                visits,
                vouchers,
                expenses,
                schedules
        );
    }

    private List<DailyMap.Visit> mapVisits(List<SaveDailyMapRequestDTO.VisitInput> inputs, Long dentistId) {
        List<DailyMap.Visit> visits = new ArrayList<>();
        for (SaveDailyMapRequestDTO.VisitInput input : list(inputs)) {
            visits.add(new DailyMap.Visit(
                    null,
                    input.appointmentId(),
                    input.patientId(),
                    dentistId,
                    input.procedureId(),
                    input.lineNumber(),
                    trim(input.recordNumber()),
                    trim(input.patientName()),
                    input.scheduledTime(),
                    input.entryTime(),
                    input.exitTime(),
                    input.returnTime(),
                    trim(input.paymentCode()),
                    money(input.value()),
                    upper(input.type()),
                    Boolean.TRUE.equals(input.checked())
            ));
        }
        return visits;
    }

    private List<DailyMap.Voucher> mapVouchers(List<SaveDailyMapRequestDTO.VoucherInput> inputs) {
        List<DailyMap.Voucher> vouchers = new ArrayList<>();
        for (SaveDailyMapRequestDTO.VoucherInput input : list(inputs)) {
            vouchers.add(new DailyMap.Voucher(
                    null,
                    trim(input.responsibleName()),
                    trim(input.recordNumber()),
                    money(input.value()),
                    trim(input.description()),
                    Boolean.TRUE.equals(input.checked())
            ));
        }
        return vouchers;
    }

    private List<DailyMap.Expense> mapExpenses(List<SaveDailyMapRequestDTO.ExpenseInput> inputs) {
        List<DailyMap.Expense> expenses = new ArrayList<>();
        for (SaveDailyMapRequestDTO.ExpenseInput input : list(inputs)) {
            expenses.add(new DailyMap.Expense(
                    null,
                    trim(input.description()),
                    money(input.value())
            ));
        }
        return expenses;
    }

    private List<DailyMap.TeamSchedule> mapSchedules(List<SaveDailyMapRequestDTO.TeamScheduleInput> inputs) {
        List<DailyMap.TeamSchedule> schedules = new ArrayList<>();
        for (SaveDailyMapRequestDTO.TeamScheduleInput input : list(inputs)) {
            schedules.add(new DailyMap.TeamSchedule(
                    null,
                    input.userId(),
                    trim(input.employeeName()),
                    upper(input.role()),
                    input.startTime(),
                    input.lunchOutTime(),
                    input.lunchReturnTime(),
                    input.endTime()
            ));
        }
        return schedules;
    }

    private void validateReferences(DailyMap map) {
        for (DailyMap.Visit visit : map.getVisits()) {
            if (visit.appointmentId() != null) {
                Appointment appointment = appointments.findById(visit.appointmentId())
                        .orElseThrow(() -> rule("Agendamento do atendimento não encontrado."));
                boolean otherClinic = !appointment.getClinicId().equals(map.getClinicId());
                boolean otherDentist = !appointment.getDentistId().equals(map.getDentistId());
                boolean otherDate = !appointment.getScheduledAt().toLocalDate().equals(map.getDate());
                if (otherClinic || otherDentist || otherDate) {
                    throw rule("O agendamento deve pertencer ao dentista, à clínica e à data do mapa.");
                }
            }
            if (visit.patientId() != null) {
                Patient patient = patients.findById(visit.patientId())
                        .orElseThrow(() -> rule("Paciente do atendimento não encontrado."));
                if (!patient.getClinicId().equals(map.getClinicId())) {
                    throw rule("O paciente do atendimento pertence a outra clínica.");
                }
            }
            if (visit.procedureId() != null) {
                Procedure procedure = procedures.findById(visit.procedureId())
                        .orElseThrow(() -> rule("Procedimento do atendimento não encontrado."));
                if (!procedure.getClinicId().equals(map.getClinicId())) {
                    throw rule("O procedimento do atendimento pertence a outra clínica.");
                }
            }
        }
    }

    private static <T> List<T> list(List<T> value) {
        return value == null ? List.of() : value;
    }

    private static String trim(String v) {
        return v == null || v.isBlank() ? null : v.trim();
    }

    private static String upper(String v) {
        return v == null ? null : v.trim().toUpperCase(Locale.ROOT);
    }

    private static BigDecimal money(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
