package ma.fsr.ms.medecin_service.exception;

public class MedecinNotFoundException extends RuntimeException {
    public MedecinNotFoundException(Long id) {
        super("Médecin introuvable : id = " + id);
    }
}