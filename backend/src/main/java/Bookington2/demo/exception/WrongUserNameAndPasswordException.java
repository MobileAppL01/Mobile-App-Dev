package Bookington2.demo.exception;

public class WrongUserNameAndPasswordException extends RuntimeException {
    public WrongUserNameAndPasswordException(String message) {
        super(message);
    }
}
