package pl.sportevents.matchresultsapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidNumberOfRecords extends ResponseStatusException {
    public InvalidNumberOfRecords(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
