package ma.fsr.ms.consultation_service.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.consultation_service.client.RendezVousClient;
import ma.fsr.ms.consultation_service.client.RendezVousDto;
import ma.fsr.ms.consultation_service.exception.ConsultationNotFoundException;
import ma.fsr.ms.consultation_service.exception.ValidationException;
import ma.fsr.ms.consultation_service.model.Consultation;
import ma.fsr.ms.consultation_service.repository.ConsultationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final RendezVousClient rendezVousClient;

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ConsultationNotFoundException(id));
    }

    public List<Consultation> getConsultationsByRendezVous(Long rendezVousId) {
        return consultationRepository.findByRendezVousId(rendezVousId);
    }

    public Consultation createConsultation(Consultation consultation) {
        validateConsultation(consultation);
        return consultationRepository.save(consultation);
    }

    public Consultation updateConsultation(Long id, Consultation consultation) {
        Consultation existingConsultation = getConsultationById(id);
        validateConsultation(consultation);

        existingConsultation.setRendezVousId(consultation.getRendezVousId());
        existingConsultation.setDateConsultation(consultation.getDateConsultation());
        existingConsultation.setRapport(consultation.getRapport());
        existingConsultation.setDiagnostic(consultation.getDiagnostic());
        existingConsultation.setTraitement(consultation.getTraitement());

        return consultationRepository.save(existingConsultation);
    }

    public void deleteConsultation(Long id) {
        Consultation consultation = getConsultationById(id);
        consultationRepository.delete(consultation);
    }

    private void validateConsultation(Consultation consultation) {
        if (consultation.getRendezVousId() == null) {
            throw new ValidationException("Le rendez-vous est obligatoire.");
        }

        RendezVousDto rendezVous = rendezVousClient.getRendezVous(consultation.getRendezVousId());
        if (rendezVous == null) {
            throw new ValidationException("Rendez-vous introuvable.");
        }

        if (consultation.getDateConsultation() == null) {
            throw new ValidationException("La date de consultation est obligatoire.");
        }

        if (consultation.getDateConsultation().isBefore(rendezVous.getDateRendezVous())) {
            throw new ValidationException("Date de consultation invalide.");
        }

        if (consultation.getRapport() == null || consultation.getRapport().trim().length() < 10) {
            throw new ValidationException("Rapport de consultation insuffisant.");
        }
    }
}