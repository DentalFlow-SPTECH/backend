package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByDentist_IdAndScheduledAtBetween(Long dentistId, LocalDateTime start, LocalDateTime end);
}
