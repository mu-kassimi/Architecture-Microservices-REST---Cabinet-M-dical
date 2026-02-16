package ma.fsr.ms.dossier_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierPatientDto {
    private PatientDto patient;
    private List<RendezVousDto> rendezVous;
    private List<ConsultationDto> consultations;
}