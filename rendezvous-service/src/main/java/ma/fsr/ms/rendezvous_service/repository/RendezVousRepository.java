package ma.fsr.ms.rendezvous_service.repository;

import ma.fsr.ms.rendezvous_service.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByPatientId(Long patientId);

    List<RendezVous> findByMedecinId(Long medecinId);
}