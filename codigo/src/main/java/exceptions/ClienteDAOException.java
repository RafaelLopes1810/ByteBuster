package exceptions;

public class ClienteDAOException extends Exception {
    public ClienteDAOException(String message) {
        super(message);
    }

    public ClienteDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}