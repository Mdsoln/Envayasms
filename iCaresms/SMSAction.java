package com.iCaresms.iCaresms;

public class SMSAction extends OutgoingSMS{
        private String action;
        private String from;
        private String message;
        private String messageType;

    public SMSAction() {
    }

    public SMSAction(String action, String from, String message, String messageType) {
        this.action = action;
        this.from = from;
        this.message = message;
        this.messageType = messageType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
        return "SMSAction{" +
                "action='" + action + '\'' +
                ", from='" + from + '\'' +
                ", message='" + message + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}

