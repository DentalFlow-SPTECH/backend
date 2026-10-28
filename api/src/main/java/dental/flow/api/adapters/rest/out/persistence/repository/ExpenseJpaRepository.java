package dental.flow.api.adapters.rest.out.persistence.repository;
import dental.flow.api.adapters.rest.out.persistence.entity.ExpenseEntity; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param; import java.util.List;
public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity,Long>{ List<ExpenseEntity> findByDailyMapIdOrderById(Long id); @Modifying @Query("delete from ExpenseEntity v where v.dailyMapId=:id") void deleteByMap(@Param("id")Long id); }
