package ma.fsr.ms.consultation_service.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.consultation_service.model.Consultation;
import ma.fsr.ms.consultation_service.service.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    public ResponseEntity<List<Consultation>> getAllConsultations() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @GetMapping("/rendezvous/{rendezVousId}")
    public ResponseEntity<List<Consultation>> getConsultationsByRendezVous(@PathVariable Long rendezVousId) {
        return ResponseEntity.ok(consultationService.getConsultationsByRendezVous(rendezVousId));
    }

    @PostMapping
    public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation) {
        Consultation createdConsultation = consultationService.createConsultation(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConsultation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consultation> updateConsultation(@PathVariable Long id,
            @RequestBody Consultation consultation) {
        Consultation updatedConsultation = consultationService.updateConsultation(id, consultation);
        return ResponseEntity.ok(updatedConsultation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}