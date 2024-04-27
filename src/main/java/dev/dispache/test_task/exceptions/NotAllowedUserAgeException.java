package dev.dispache.test_task.exceptions;

public class NotAllowedUserAgeException extends RuntimeException {

    public NotAllowedUserAgeException() {
        super("The lowest allowed registration age is 18");
    }

}
