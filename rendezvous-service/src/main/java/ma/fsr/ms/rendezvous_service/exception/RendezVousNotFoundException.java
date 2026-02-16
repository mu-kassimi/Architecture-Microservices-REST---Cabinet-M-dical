package ma.fsr.ms.rendezvous_service.exception;

public class RendezVousNotFoundException extends RuntimeException {
    public RendezVousNotFoundException(Long id) {
        super("Rendez-vous introuvable : id = " + id);
    }
}