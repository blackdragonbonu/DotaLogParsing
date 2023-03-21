package gg.bayes.challenge.service;

/**
 * Exception class that is used to indicate an Invalid Log file
 * passed through for ingestion
 */
public class InvalidLogFileException extends Exception{
    public InvalidLogFileException() {
        super();
    }

    public InvalidLogFileException(String message) {
        super(message);
    }

    public InvalidLogFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLogFileException(Throwable cause) {
        super(cause);
    }
}
