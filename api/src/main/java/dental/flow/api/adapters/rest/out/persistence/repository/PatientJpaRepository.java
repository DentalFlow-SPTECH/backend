package dental.flow.api.adapters.rest.out.persistence.repository;

import dental.flow.api.adapters.rest.out.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    Boolean existsByClinic_IdAndCpf(Long clinicId, String cpf);
    List<PatientEntity> findByClinic_Id(Long clinicId);
}
