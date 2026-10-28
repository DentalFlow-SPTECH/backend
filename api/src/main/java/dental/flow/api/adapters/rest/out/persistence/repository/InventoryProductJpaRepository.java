package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.InventoryProductEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InventoryProductJpaRepository extends JpaRepository<InventoryProductEntity, Long> {
    Optional<InventoryProductEntity> findByIdAndClinicId(Long id, Long clinicId);
    List<InventoryProductEntity> findByClinicIdOrderByName(Long clinicId);
    List<InventoryProductEntity> findByClinicIdAndActiveOrderByName(Long clinicId, Boolean active);
    boolean existsByClinicIdAndNameIgnoreCaseAndIdNot(Long clinicId, String name, Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select product from InventoryProductEntity product where product.id = :id and product.clinicId = :clinicId")
    Optional<InventoryProductEntity> findLockedByIdAndClinicId(@Param("id") Long id, @Param("clinicId") Long clinicId);
}
