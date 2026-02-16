package ma.fsr.ms.dossier_service.client;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.dossier_service.dto.RendezVousDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RendezVousClient {

    private final RestTemplate restTemplate;

    @Value("${rendezvous.service.url}")
    private String rendezVousServiceUrl;

    public List<RendezVousDto> getRendezVousByPatient(Long patientId) {
        String url = rendezVousServiceUrl + "/internal/api/v1/rendezvous/patient/" + patientId;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RendezVousDto>>() {
                }).getBody();
    }
}