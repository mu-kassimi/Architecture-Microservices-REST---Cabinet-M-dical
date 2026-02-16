package ma.fsr.ms.rendezvous_service.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.rendezvous_service.client.MedecinClient;
import ma.fsr.ms.rendezvous_service.client.PatientClient;
import ma.fsr.ms.rendezvous_service.exception.RendezVousNotFoundException;
import ma.fsr.ms.rendezvous_service.exception.ValidationException;
import ma.fsr.ms.rendezvous_service.model.RendezVous;
import ma.fsr.ms.rendezvous_service.model.StatutRdv;
import ma.fsr.ms.rendezvous_service.repository.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientClient patientClient;
    private final MedecinClient medecinClient;

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RendezVousNotFoundException(id));
    }

    public List<RendezVous> getRendezVousByPatient(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }

    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    public RendezVous createRendezVous(RendezVous rendezVous) {
        validateRendezVous(rendezVous);

        if (rendezVous.getStatut() == null) {
            rendezVous.setStatut(StatutRdv.PLANIFIE);
        }

        return rendezVousRepository.save(rendezVous);
    }

    public RendezVous updateRendezVous(Long id, RendezVous rendezVous) {
        RendezVous existingRendezVous = getRendezVousById(id);
        validateRendezVous(rendezVous);

        existingRendezVous.setPatientId(rendezVous.getPatientId());
        existingRendezVous.setMedecinId(rendezVous.getMedecinId());
        existingRendezVous.setDateRendezVous(rendezVous.getDateRendezVous());
        existingRendezVous.setStatut(rendezVous.getStatut());
        existingRendezVous.setMotif(rendezVous.getMotif());

        return rendezVousRepository.save(existingRendezVous);
    }

    public RendezVous updateStatut(Long id, StatutRdv statut) {
        RendezVous rendezVous = getRendezVousById(id);
        validateStatut(statut);
        rendezVous.setStatut(statut);
        return rendezVousRepository.save(rendezVous);
    }

    public void deleteRendezVous(Long id) {
        RendezVous rendezVous = getRendezVousById(id);
        rendezVousRepository.delete(rendezVous);
    }

    private void validateRendezVous(RendezVous rendezVous) {
        if (rendezVous.getDateRendezVous() == null) {
            throw new ValidationException("La date du rendez-vous est obligatoire.");
        }

        if (rendezVous.getDateRendezVous().isBefore(LocalDateTime.now())) {
            throw new ValidationException("La date du rendez-vous doit être future.");
        }

        if (rendezVous.getPatientId() == null) {
            throw new ValidationException("Le patient est obligatoire.");
        }

        if (!patientClient.patientExists(rendezVous.getPatientId())) {
            throw new ValidationException("Patient introuvable.");
        }

        if (rendezVous.getMedecinId() == null) {
            throw new ValidationException("Le médecin est obligatoire.");
        }

        if (!medecinClient.medecinExists(rendezVous.getMedecinId())) {
            throw new ValidationException("Médecin introuvable");
        }

        if (rendezVous.getStatut() != null) {
            validateStatut(rendezVous.getStatut());
        }
    }

    private void validateStatut(StatutRdv statut) {
        if (statut != StatutRdv.PLANIFIE && statut != StatutRdv.ANNULE && statut != StatutRdv.TERMINE) {
            throw new ValidationException("Statut invalide. Valeurs possibles : PLANIFIE, ANNULE, TERMINE.");
        }
    }
}