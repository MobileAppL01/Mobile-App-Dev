package Bookington2.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter

public enum ErrorCode {
    UNCATERGORIZED_EXCEPTION(9999, "Uncatergorized exception error"),
    INVALID_MESSAGE_KEY(1001, "Invalid message key"),
    USER_EXITSED(  1002, "User has been exitsted"),
    USERNAME_INVALID(1003, "Username must be 3 characters"),
    PASSWORD_INVALID(1004, "Password must be 8 characters"),

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
