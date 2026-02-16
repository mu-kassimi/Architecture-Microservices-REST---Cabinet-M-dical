package ma.fsr.ms.patient_service.service;

import ma.fsr.ms.patient_service.exception.BusinessException;
import ma.fsr.ms.patient_service.exception.PatientNotFoundException;
import ma.fsr.ms.patient_service.model.Patient;
import ma.fsr.ms.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient introuvable : id = " + id));
    }

    @Transactional
    public Patient createPatient(Patient patient) {
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = getPatientById(id);
        validatePatient(patientDetails);

        patient.setNom(patientDetails.getNom());
        patient.setPrenom(patientDetails.getPrenom());
        patient.setDateNaissance(patientDetails.getDateNaissance());
        patient.setTelephone(patientDetails.getTelephone());
        patient.setEmail(patientDetails.getEmail());
        patient.setAdresse(patientDetails.getAdresse());

        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    private void validatePatient(Patient patient) {
        if (patient.getNom() == null || patient.getNom().trim().isEmpty()) {
            throw new BusinessException("Le nom du patient est obligatoire.");
        }

        if (patient.getTelephone() == null || patient.getTelephone().trim().isEmpty()) {
            throw new BusinessException("Le téléphone du patient est obligatoire.");
        }

        if (patient.getDateNaissance() != null && patient.getDateNaissance().isAfter(LocalDate.now())) {
            throw new BusinessException("La date de naissance ne peut pas être future");
        }
    }
}