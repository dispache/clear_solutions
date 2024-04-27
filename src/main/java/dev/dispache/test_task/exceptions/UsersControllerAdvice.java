package dev.dispache.test_task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice()
public class UsersControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ArrayList<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final ArrayList<String> responseList = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            responseList.add(message);
        });
        return new ResponseEntity<>(responseList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            value = {
                    MissingServletRequestParameterException.class,
                    FromDateGreaterThanToDateException.class,
                    NotAllowedUserAgeException.class
            }
    )
    public ResponseEntity<String> handleCustomExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
