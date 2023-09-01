package com.iCaresms.iCaresms.EnvayaSMS;

public class IncomingSMS {
    private String sender;
    private String message;

    public IncomingSMS() {
    }

    public IncomingSMS(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IncomingSMS{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}