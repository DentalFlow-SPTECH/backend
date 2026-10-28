package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentJpaRepository extends JpaRepository<EquipmentEntity, Long> {
    Optional<EquipmentEntity> findByIdAndClinicId(Long id, Long clinicId);

    List<EquipmentEntity> findByClinicIdOrderByName(Long clinicId);

    List<EquipmentEntity> findByClinicIdAndActiveOrderByName(Long clinicId, Boolean active);

    boolean existsByClinicIdAndNameIgnoreCaseAndIdNot(Long clinicId, String name, Long id);
}
