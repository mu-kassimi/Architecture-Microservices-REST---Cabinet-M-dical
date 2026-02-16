package ma.fsr.ms.medecin_service.repository;

import ma.fsr.ms.medecin_service.model.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
}