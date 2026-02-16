package ma.fsr.ms.consultation_service.exception;

public class ConsultationNotFoundException extends RuntimeException {
    public ConsultationNotFoundException(Long id) {
        super("Consultation introuvable : id = " + id);
    }
}