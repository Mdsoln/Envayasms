package com.iCaresms.iCaresms;

import org.springframework.stereotype.Service;

@Service
public class SMSService {

    public /*IncomingSMS*/ String processIncomingSMS(String from, String message/*String messageType*/) {
        if (message.equalsIgnoreCase("help")) {
            return "Thank you for using our service. For assistance, reply with 'info'.";
        } else if (message.equalsIgnoreCase("info")) {
            return "Welcome to our service! Reply with 'subscribe' to receive updates.";
        } else {
            return "Thank you for your message. We have received it.";
        }

//        IncomingSMS response = new IncomingSMS();
//        response.setFrom(from);
//        response.setMessage(message);
//        response.setMessageType(messageType);
//
//        return response;
    }

    SMSResponse generatedMessage(String message){
        SMSResponse response = new SMSResponse();
        response.setStatus("successfully");
        response.setMessage(message);
        return response;
    }

    public String processOutgoingMessage(String to, String message) {
        return "'outgoing_message'" +
                "'Event: send'" +
                "'to: '"+to + "\n " +
                "'message: '"+message;
    }
}
