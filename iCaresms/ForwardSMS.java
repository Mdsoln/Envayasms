package com.iCaresms.iCaresms;

public class ForwardSMS {
    private String to;
    private String message;
    private String messageType;

    public ForwardSMS() {
    }

    public ForwardSMS(String to, String message, String messageType) {
        this.to = to;
        this.message = message;
        this.messageType = messageType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
        return "ForwardSMS{" +
                "to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}
