package ma.fsr.ms.dossier_service.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.dossier_service.client.ConsultationClient;
import ma.fsr.ms.dossier_service.client.PatientClient;
import ma.fsr.ms.dossier_service.client.RendezVousClient;
import ma.fsr.ms.dossier_service.dto.ConsultationDto;
import ma.fsr.ms.dossier_service.dto.DossierPatientDto;
import ma.fsr.ms.dossier_service.dto.PatientDto;
import ma.fsr.ms.dossier_service.dto.RendezVousDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DossierService {

    private final PatientClient patientClient;
    private final RendezVousClient rendezVousClient;
    private final ConsultationClient consultationClient;

    public DossierPatientDto getDossierPatient(Long patientId) {
        // Récupérer les informations du patient
        PatientDto patient = patientClient.getPatient(patientId);

        // Récupérer les rendez-vous du patient
        List<RendezVousDto> rendezVousList = rendezVousClient.getRendezVousByPatient(patientId);

        // Récupérer les consultations pour chaque rendez-vous
        List<ConsultationDto> allConsultations = new ArrayList<>();
        for (RendezVousDto rdv : rendezVousList) {
            List<ConsultationDto> consultations = consultationClient.getConsultationsByRendezVous(rdv.getId());
            allConsultations.addAll(consultations);
        }

        // Créer et retourner le dossier patient complet
        DossierPatientDto dossier = new DossierPatientDto();
        dossier.setPatient(patient);
        dossier.setRendezVous(rendezVousList);
        dossier.setConsultations(allConsultations);

        return dossier;
    }
}