package com.iCaresms.iCaresms.EnvayaSMS;

import jakarta.persistence.*;

@Entity
@Table( name = "incoming_sms")
public class SMSMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String messageContent;
    private String messageType;

    public SMSMessage() {
    }

    public SMSMessage(Long id, String sender, String messageContent, String messageType) {
        this.id = id;
        this.sender = sender;
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    public SMSMessage(String sender, String messageContent, String messageType) {
        this.sender = sender;
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
