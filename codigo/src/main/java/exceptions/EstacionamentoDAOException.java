package exceptions;

public class EstacionamentoDAOException extends Exception {

    public EstacionamentoDAOException(String message) {
        super(message);
    }

    public EstacionamentoDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}