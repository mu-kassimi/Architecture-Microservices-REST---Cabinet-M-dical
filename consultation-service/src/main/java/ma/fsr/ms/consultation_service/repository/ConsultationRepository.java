package ma.fsr.ms.consultation_service.repository;

import ma.fsr.ms.consultation_service.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByRendezVousId(Long rendezVousId);
}