package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitJpaRepository extends JpaRepository<VisitEntity, Long> {
    List<VisitEntity> findByDailyMapIdOrderByLineNumber(Long id);

    @Modifying
    @Query("delete from VisitEntity v where v.dailyMapId=:id")
    void deleteByMap(@Param("id") Long id);
}
