package com.iCaresms.iCaresms.EnvayaSMS;

public class SMSEvent {
    protected String event;

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
}
