package ma.fsr.ms.dossier_service.client;

import lombok.RequiredArgsConstructor;
import ma.fsr.ms.dossier_service.dto.ConsultationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsultationClient {

    private final RestTemplate restTemplate;

    @Value("${consultation.service.url}")
    private String consultationServiceUrl;

    public List<ConsultationDto> getConsultationsByRendezVous(Long rendezVousId) {
        try {
            String url = consultationServiceUrl + "/internal/api/v1/consultations/rendezvous/" + rendezVousId;
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ConsultationDto>>() {}
            ).getBody();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}    