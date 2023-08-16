package com.iCaresms.iCaresms;

public class SMSCancel extends SMSEvent{
    private String id;

    public SMSCancel() {
    }

    public SMSCancel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SMSCancel{" +
                "id='" + id + '\'' +
                '}';
    }
}
