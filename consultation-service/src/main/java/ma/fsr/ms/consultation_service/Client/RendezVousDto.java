package ma.fsr.ms.consultation_service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVousDto {
    private Long id;
    private Long patientId;
    private Long medecinId;
    private LocalDateTime dateRendezVous;
    private String statut;
    private String motif;
}