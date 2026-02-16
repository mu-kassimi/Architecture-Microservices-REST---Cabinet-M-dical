package ma.fsr.ms.medecin_service.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.medecin_service.model.Medecin;
import ma.fsr.ms.medecin_service.service.MedecinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/v1/medecins")
@RequiredArgsConstructor
public class MedecinController {

    private final MedecinService medecinService;

    @GetMapping
    public ResponseEntity<List<Medecin>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medecin> getMedecinById(@PathVariable Long id) {
        return ResponseEntity.ok(medecinService.getMedecinById(id));
    }

    @PostMapping
    public ResponseEntity<Medecin> createMedecin(@RequestBody Medecin medecin) {
        Medecin createdMedecin = medecinService.createMedecin(medecin);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMedecin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medecin> updateMedecin(@PathVariable Long id, @RequestBody Medecin medecin) {
        Medecin updatedMedecin = medecinService.updateMedecin(id, medecin);
        return ResponseEntity.ok(updatedMedecin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }
}