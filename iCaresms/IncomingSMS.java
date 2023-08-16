package com.iCaresms.iCaresms;

public class IncomingSMS {
    private String from;
    private String message;
    private String messageType;

    public IncomingSMS() {
    }

    public IncomingSMS(String from, String message, String messageType) {
        this.from = from;
        this.message = message;
        this.messageType = messageType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "IncomingSMS{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}
