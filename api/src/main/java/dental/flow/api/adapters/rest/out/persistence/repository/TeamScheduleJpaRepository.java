package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.TeamScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamScheduleJpaRepository extends JpaRepository<TeamScheduleEntity, Long> {
    List<TeamScheduleEntity> findByDailyMapIdOrderById(Long id);

    @Modifying
    @Query("delete from TeamScheduleEntity v where v.dailyMapId=:id")
    void deleteByMap(@Param("id") Long id);
}
