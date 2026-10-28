package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.DailyMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMapJpaRepository extends JpaRepository<DailyMapEntity, Long> {
    Optional<DailyMapEntity> findByIdAndClinicIdAndDentistId(Long id, Long clinicId, Long dentistId);

    Optional<DailyMapEntity> findByClinicIdAndDentistIdAndDate(Long clinicId, Long dentistId, LocalDate date);

    boolean existsByClinicIdAndDentistIdAndDate(Long clinicId, Long dentistId, LocalDate date);

    List<DailyMapEntity> findByClinicIdAndDentistIdAndDateBetweenOrderByDate(Long clinicId, Long dentistId, LocalDate start, LocalDate end);
}
