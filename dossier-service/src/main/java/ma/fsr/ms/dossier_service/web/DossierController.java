package ma.fsr.ms.dossier_service.web;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.dossier_service.dto.DossierPatientDto;
import ma.fsr.ms.dossier_service.service.DossierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/api/v1/dossiers")
@RequiredArgsConstructor
public class DossierController {

    private final DossierService dossierService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DossierPatientDto> getDossierPatient(@PathVariable Long patientId) {
        DossierPatientDto dossier = dossierService.getDossierPatient(patientId);
        return ResponseEntity.ok(dossier);
    }
}