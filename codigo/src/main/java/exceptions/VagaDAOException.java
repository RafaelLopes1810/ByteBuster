package exceptions;

public class VagaDAOException extends Exception {

    public VagaDAOException(String message) {
        super(message);
    }

    public VagaDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}