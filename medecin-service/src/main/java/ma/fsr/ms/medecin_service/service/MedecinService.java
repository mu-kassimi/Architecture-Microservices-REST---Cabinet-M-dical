package ma.fsr.ms.medecin_service.service;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.medecin_service.exception.MedecinNotFoundException;
import ma.fsr.ms.medecin_service.exception.ValidationException;
import ma.fsr.ms.medecin_service.model.Medecin;
import ma.fsr.ms.medecin_service.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MedecinService {

    private final MedecinRepository medecinRepository;

    public List<Medecin> getAllMedecins() {
        return medecinRepository.findAll();
    }

    public Medecin getMedecinById(Long id) {
        return medecinRepository.findById(id)
                .orElseThrow(() -> new MedecinNotFoundException(id));
    }

    public Medecin createMedecin(Medecin medecin) {
        validateMedecin(medecin);
        return medecinRepository.save(medecin);
    }

    public Medecin updateMedecin(Long id, Medecin medecin) {
        Medecin existingMedecin = getMedecinById(id);
        validateMedecin(medecin);

        existingMedecin.setNom(medecin.getNom());
        existingMedecin.setEmail(medecin.getEmail());
        existingMedecin.setSpecialite(medecin.getSpecialite());
        existingMedecin.setTelephone(medecin.getTelephone());

        return medecinRepository.save(existingMedecin);
    }

    public void deleteMedecin(Long id) {
        Medecin medecin = getMedecinById(id);
        medecinRepository.delete(medecin);
    }

    private void validateMedecin(Medecin medecin) {
        if (medecin.getNom() == null || medecin.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du médecin est obligatoire.");
        }

        if (medecin.getEmail() == null || medecin.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email du médecin est obligatoire.");
        }

        if (!medecin.getEmail().contains("@")) {
            throw new ValidationException("Email du médecin invalide.");
        }

        if (medecin.getSpecialite() == null || medecin.getSpecialite().trim().isEmpty()) {
            throw new ValidationException("La spécialité du médecin est obligatoire.");
        }
    }
}