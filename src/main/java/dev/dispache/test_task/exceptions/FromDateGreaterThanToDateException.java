package dev.dispache.test_task.exceptions;

public class FromDateGreaterThanToDateException extends RuntimeException {

    public FromDateGreaterThanToDateException() {
        super("'from' request parameter is greater than 'to' request parameter");
    }

}
