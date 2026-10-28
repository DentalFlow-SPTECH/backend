package dental.flow.api.application.out;
import dental.flow.api.domain.dailymap.DailyMap; import java.time.LocalDate; import java.util.*;
public interface DailyMapRepository { DailyMap save(DailyMap map,Long userId,String action); Optional<DailyMap> findById(Long id,Long clinicId,Long dentistId); Optional<DailyMap> findByDate(Long clinicId,Long dentistId,LocalDate date); List<DailyMap> list(Long clinicId,Long dentistId,LocalDate start,LocalDate end); boolean exists(Long clinicId,Long dentistId,LocalDate date); DailyMap close(DailyMap map,Long userId); }
