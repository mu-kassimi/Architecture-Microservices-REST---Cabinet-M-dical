package ma.fsr.ms.dossier_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private Long id;
    private String nom;
    private String telephone;
    private LocalDate dateNaissance;
    private String adresse;
}