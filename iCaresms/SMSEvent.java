package com.iCaresms.iCaresms;

public class SMSEvent extends SMSConstants{
    private String event;

    public SMSEvent() {
    }

    public SMSEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "SMSEvent{" +
                "event='" + event + '\'' +
                '}';
    }
}
