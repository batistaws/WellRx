package batista.WellRx.infra.exeption;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String message) {
        super(message);
    }
}
