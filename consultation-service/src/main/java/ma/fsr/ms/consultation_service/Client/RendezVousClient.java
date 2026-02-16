package ma.fsr.ms.consultation_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
@RequiredArgsConstructor
public class RendezVousClient {

    private final RestTemplate restTemplate;

    @Value("${rendezvous.service.url}")
    private String rendezVousServiceUrl;

    public RendezVousDto getRendezVous(Long rendezVousId) {
        try {
            String url = rendezVousServiceUrl + "/internal/api/v1/rendezvous/" + rendezVousId;
            return restTemplate.getForObject(url, RendezVousDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du rendez-vous", e);
        }
    }

    public boolean rendezVousExists(Long rendezVousId) {
        return getRendezVous(rendezVousId) != null;
    }
}