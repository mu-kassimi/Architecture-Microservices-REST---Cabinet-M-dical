package ma.fsr.ms.dossier_service.client;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.dossier_service.dto.PatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PatientClient {

    private final RestTemplate restTemplate;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    public PatientDto getPatient(Long patientId) {
        String url = patientServiceUrl + "/internal/api/v1/patients/" + patientId;
        return restTemplate.getForObject(url, PatientDto.class);
    }
}