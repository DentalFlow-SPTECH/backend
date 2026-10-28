package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementJpaRepository extends JpaRepository<StockMovementEntity, Long> {
    List<StockMovementEntity> findByProductIdOrderByOccurredAtDesc(Long productId);
}
