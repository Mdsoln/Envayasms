package com.iCaresms.iCaresms.EnvayaSMS;

import jakarta.persistence.*;

@Entity
@Table(name = "outgoing_sms")
public class OutgoinngSMS{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long serverid;
    protected String recipient;

    protected String content;
    protected String status;//sent,failed, queued

    public OutgoinngSMS() {
    }

    public OutgoinngSMS(Long id, String recipient,
                        String content, String status) {
        this.serverid = id;
        this.recipient = recipient;
        this.content = content;
        this.status = status;
    }

    public OutgoinngSMS(String recipient,
                        String content, String status) {
        this.recipient = recipient;
        this.content = content;
        this.status = status;
    }

    public Long getId() {
        return serverid;
    }

    public void setId(Long id) {
        this.serverid = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
