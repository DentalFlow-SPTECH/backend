package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.request.SaveDailyMapRequestDTO;
import dental.flow.api.adapters.rest.in.controller.dto.response.DailyMapResponseDTO;
import dental.flow.api.application.in.DailyMapUseCase;
import dental.flow.api.application.out.*;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.Failures;
import dental.flow.api.domain.dailymap.DailyMap;
import org.springframework.stereotype.Service;
import java.math.BigDecimal; import java.time.LocalDate; import java.util.*;

@Service
public class DailyMapService implements DailyMapUseCase {
 private final DailyMapRepository repository; private final RegisterAppointmentRepository appointments; private final RegisterPatientRepository patients; private final RegisterProcedureRepository procedures;
 public DailyMapService(DailyMapRepository repository,RegisterAppointmentRepository appointments,RegisterPatientRepository patients,RegisterProcedureRepository procedures){this.repository=repository;this.appointments=appointments;this.patients=patients;this.procedures=procedures;}
 @Override public DailyMapResponseDTO create(Long clinicId,Long dentistId,Long userId,SaveDailyMapRequestDTO request){if(repository.exists(clinicId,dentistId,request.date()))throw rule("Já existe um mapa para este dentista nesta data.");DailyMap map=map(clinicId,dentistId,userId,request);validateReferences(map);return DailyMapResponseDTO.from(repository.save(map,userId,"CRIAR"));}
 @Override public DailyMapResponseDTO update(Long id,Long clinicId,Long dentistId,Long userId,SaveDailyMapRequestDTO request){DailyMap current=find(id,clinicId,dentistId);if("FECHADO".equals(current.getStatus()))throw rule("Mapa fechado não pode ser alterado.");if(!current.getDate().equals(request.date())&&repository.exists(clinicId,dentistId,request.date()))throw rule("Já existe um mapa para este dentista nesta data.");DailyMap map=map(clinicId,dentistId,userId,request);map.restore(id,"ABERTO",null,null,null);validateReferences(map);return DailyMapResponseDTO.from(repository.save(map,userId,"ATUALIZAR"));}
 @Override public DailyMapResponseDTO get(Long id,Long clinicId,Long dentistId){return DailyMapResponseDTO.from(find(id,clinicId,dentistId));}
 @Override public DailyMapResponseDTO getByDate(LocalDate date,Long clinicId,Long dentistId){return DailyMapResponseDTO.from(repository.findByDate(clinicId,dentistId,date).orElseThrow(()->notFound()));}
 @Override public List<DailyMapResponseDTO> list(LocalDate start,LocalDate end,Long clinicId,Long dentistId){if(start==null||end==null||end.isBefore(start))throw rule("O período do mapa diário é inválido.");return repository.list(clinicId,dentistId,start,end).stream().map(DailyMapResponseDTO::from).toList();}
 @Override public DailyMapResponseDTO close(Long id,Long clinicId,Long dentistId,Long userId){DailyMap map=find(id,clinicId,dentistId);map.close(userId);return DailyMapResponseDTO.from(repository.close(map,userId));}
 private DailyMap find(Long id,Long clinicId,Long dentistId){return repository.findById(id,clinicId,dentistId).orElseThrow(()->notFound());}
 private BusinessException notFound(){return new BusinessException(Failures.NOT_FOUND,"Mapa diário não encontrado.");} private BusinessException rule(String m){return new BusinessException(Failures.BUSINESS_FAILURE,m);}
 private DailyMap map(Long clinicId,Long dentistId,Long userId,SaveDailyMapRequestDTO r){
  List<DailyMap.Visit> visits=list(r.visits()).stream().map(v->new DailyMap.Visit(null,v.appointmentId(),v.patientId(),dentistId,v.procedureId(),v.lineNumber(),trim(v.recordNumber()),trim(v.patientName()),v.scheduledTime(),v.entryTime(),v.exitTime(),v.returnTime(),trim(v.paymentCode()),money(v.value()),upper(v.type()),Boolean.TRUE.equals(v.checked()))).toList();
  List<DailyMap.Voucher> vouchers=list(r.vouchers()).stream().map(v->new DailyMap.Voucher(null,trim(v.responsibleName()),trim(v.recordNumber()),money(v.value()),trim(v.description()),Boolean.TRUE.equals(v.checked()))).toList();
  List<DailyMap.Expense> expenses=list(r.expenses()).stream().map(v->new DailyMap.Expense(null,trim(v.description()),money(v.value()))).toList();
  List<DailyMap.TeamSchedule> schedules=list(r.teamSchedules()).stream().map(v->new DailyMap.TeamSchedule(null,v.userId(),trim(v.employeeName()),upper(v.role()),v.startTime(),v.lunchOutTime(),v.lunchReturnTime(),v.endTime())).toList();
  return new DailyMap(clinicId,dentistId,userId,r.date(),r.cardDiscount(),r.predatedCheckDiscount(),r.cashCheckDiscount(),r.otherDiscount(),r.moldingCount(),r.biteCount(),r.fittingCount(),r.deliveryCount(),r.repetitionCount(),r.notes(),visits,vouchers,expenses,schedules);
 }
 private void validateReferences(DailyMap map){for(DailyMap.Visit visit:map.getVisits()){if(visit.appointmentId()!=null){var a=appointments.findById(visit.appointmentId()).orElseThrow(()->rule("Agendamento do atendimento não encontrado."));if(!a.getClinicId().equals(map.getClinicId())||!a.getDentistId().equals(map.getDentistId())||!a.getScheduledAt().toLocalDate().equals(map.getDate()))throw rule("O agendamento deve pertencer ao dentista, à clínica e à data do mapa.");}if(visit.patientId()!=null){var p=patients.findById(visit.patientId()).orElseThrow(()->rule("Paciente do atendimento não encontrado."));if(!p.getClinicId().equals(map.getClinicId()))throw rule("O paciente do atendimento pertence a outra clínica.");}if(visit.procedureId()!=null){var p=procedures.findById(visit.procedureId()).orElseThrow(()->rule("Procedimento do atendimento não encontrado."));if(!p.getClinicId().equals(map.getClinicId()))throw rule("O procedimento do atendimento pertence a outra clínica.");}}}
 private static <T> List<T> list(List<T> value){return value==null?List.of():value;} private static String trim(String v){return v==null||v.isBlank()?null:v.trim();} private static String upper(String v){return v==null?null:v.trim().toUpperCase(Locale.ROOT);} private static BigDecimal money(BigDecimal v){return v==null?BigDecimal.ZERO:v;}
}
