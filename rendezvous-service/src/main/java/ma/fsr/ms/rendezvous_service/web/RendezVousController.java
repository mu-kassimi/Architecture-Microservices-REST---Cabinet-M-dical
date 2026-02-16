package ma.fsr.ms.rendezvous_service.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.rendezvous_service.model.RendezVous;
import ma.fsr.ms.rendezvous_service.model.StatutRdv;
import ma.fsr.ms.rendezvous_service.service.RendezVousService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/api/v1/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.getRendezVousById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(rendezVousService.getRendezVousByPatient(patientId));
    }

    @GetMapping("/medecin/{medecinId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByMedecin(@PathVariable Long medecinId) {
        return ResponseEntity.ok(rendezVousService.getRendezVousByMedecin(medecinId));
    }

    @PostMapping
    public ResponseEntity<RendezVous> createRendezVous(@RequestBody RendezVous rendezVous) {
        RendezVous createdRendezVous = rendezVousService.createRendezVous(rendezVous);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRendezVous);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> updateRendezVous(@PathVariable Long id, @RequestBody RendezVous rendezVous) {
        RendezVous updatedRendezVous = rendezVousService.updateRendezVous(id, rendezVous);
        return ResponseEntity.ok(updatedRendezVous);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<RendezVous> updateStatut(@PathVariable Long id, @RequestBody Map<String, String> body) {
        StatutRdv statut = StatutRdv.valueOf(body.get("statut"));
        RendezVous updatedRendezVous = rendezVousService.updateStatut(id, statut);
        return ResponseEntity.ok(updatedRendezVous);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }
}