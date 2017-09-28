package com.myth.common.exception;

/**
 * 服务器连接异常
 */
public class ConnectionException extends MythException{
    public ConnectionException() {
        super();
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Class location) {
        super(message, location);
    }

    public ConnectionException(String message, Throwable cause, Class location) {
        super(message, cause, location);
        System.out.println(90);
    }
}
