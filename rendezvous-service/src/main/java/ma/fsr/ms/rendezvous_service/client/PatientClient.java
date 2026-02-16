package ma.fsr.ms.rendezvous_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
@RequiredArgsConstructor
public class PatientClient {

    private final RestTemplate restTemplate;

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    public boolean patientExists(Long patientId) {
        try {
            String url = patientServiceUrl + "/internal/api/v1/patients/" + patientId;
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification du patient", e);
        }
    }
}