package dental.flow.api.adapters.rest.out.persistence.repository;
import dental.flow.api.adapters.rest.out.persistence.entity.VoucherEntity; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.List;
public interface VoucherJpaRepository extends JpaRepository<VoucherEntity,Long>{ List<VoucherEntity> findByDailyMapIdOrderById(Long id); @Modifying @Query("delete from VoucherEntity v where v.dailyMapId=:id") void deleteByMap(@Param("id")Long id); }
