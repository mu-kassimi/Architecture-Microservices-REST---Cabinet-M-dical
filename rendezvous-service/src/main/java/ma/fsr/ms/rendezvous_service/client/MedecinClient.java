package ma.fsr.ms.rendezvous_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
@RequiredArgsConstructor
public class MedecinClient {

    private final RestTemplate restTemplate;

    @Value("${medecin.service.url}")
    private String medecinServiceUrl;

    public boolean medecinExists(Long medecinId) {
        try {
            String url = medecinServiceUrl + "/internal/api/v1/medecins/" + medecinId;
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification du médecin", e);
        }
    }
}