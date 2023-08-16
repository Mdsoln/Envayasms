package com.iCaresms.iCaresms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/envayasms")
public class SMSController {

    @Autowired
    private SMSService smsService;
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public SMSResponse handleRequest(@RequestBody SMSAction smsAction){
        String action = smsAction.getAction();
        String responseMessage = new String();

        /* ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"error\": \"Unable to process response.\"}";
        }
    }*/

        if (SMSConstants.ACTION_INCOMING.equals(action)){
            String from = smsAction.getFrom();
            String message = smsAction.getMessage();
            String messageType = smsAction.getMessageType();

            if (SMSConstants.MESSAGE_TYPE_SMS.equals(messageType)){
                responseMessage = smsService.processIncomingSMS(from,message);
            } else if (SMSConstants.MESSAGE_TYPE_MMS.equals(messageType)) {
                responseMessage = smsService.processIncomingSMS(from,message);
            } else if (SMSConstants.MESSAGE_TYPE_CALL.equals(messageType)) {
                responseMessage = smsService.processIncomingSMS(from,message);
            } else {
                responseMessage = "Unsupported message type: "+ messageType;
            }
        }
        else if (SMSConstants.ACTION_OUTGOING.equals(action)) {
              String to = smsAction.getTo();
              String message = smsAction.getMessage();
              String messageType = smsAction.getMessageType();
              
              if (SMSConstants.MESSAGE_TYPE_SMS.equals(messageType)){
                  responseMessage = smsService.processOutgoingMessage(to,message);
              } else if (SMSConstants.MESSAGE_TYPE_MMS.equals(messageType)) {
                  responseMessage = smsService.processOutgoingMessage(to,message);
              } else if (SMSConstants.MESSAGE_TYPE_CALL.equals(messageType)) {
                  responseMessage = smsService.processOutgoingMessage(to,message);
              }
        } else {
            responseMessage = "Unsupported action: " +action;
        }

        return smsService.generatedMessage(responseMessage);
    }
}
