package ma.fsr.ms.consultation_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long rendezVousId;

    @Column(nullable = false)
    private LocalDateTime dateConsultation;

    @Column(nullable = false, length = 2000)
    private String rapport;

    private String diagnostic;

    private String traitement;
}