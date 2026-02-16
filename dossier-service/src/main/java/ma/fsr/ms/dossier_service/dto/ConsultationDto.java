package ma.fsr.ms.dossier_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {
    private Long id;
    private Long rendezVousId;
    private LocalDateTime dateConsultation;
    private String rapport;
    private String diagnostic;
    private String traitement;
}