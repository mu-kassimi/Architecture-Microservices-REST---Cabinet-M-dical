package ma.fsr.ms.rendezvous_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long medecinId;

    @Column(nullable = false)
    private LocalDateTime dateRendezVous;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRdv statut = StatutRdv.PLANIFIE;

    private String motif;
}