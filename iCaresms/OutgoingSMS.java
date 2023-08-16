package com.iCaresms.iCaresms;

public class OutgoingSMS {
    private String id;
    protected String to;
    protected String message;
    private int priority;
    protected String type;

    public OutgoingSMS() {
    }

    public OutgoingSMS(String id,
                       String to,
                       String message,
                       int priority, String type) {
        this.id = id;
        this.to = to;
        this.message = message;
        this.priority = priority;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OutgoingSMS{" +
                "id='" + id + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", priority=" + priority +
                ", type='" + type + '\'' +
                '}';
    }
}
