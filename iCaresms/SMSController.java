package com.iCaresms.iCaresms;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/envayasms")
public class SMSController {

    @Autowired
    private SMSService smsService;
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleRequest(@RequestBody SMSAction smsAction) throws JSONException {
        String action = smsAction.getAction();

        if (SMSConstants.ACTION_INCOMING.equals(action)){
            String from = smsAction.getFrom();
            String message = smsAction.getMessage();
            String messageType = smsAction.getMessageType();

            if (SMSConstants.MESSAGE_TYPE_SMS.equals(messageType)){
                String jsonResponse = smsService.processIncomingAction(from,message,messageType);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);

                return new ResponseEntity<>(jsonResponse, responseHeaders, HttpStatus.OK);

            }

            else if (SMSConstants.MESSAGE_TYPE_MMS.equals(messageType)) {
                String jsonResponse = smsService.processIncomingAction(from,message,messageType);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);

                return new ResponseEntity<>(jsonResponse, responseHeaders, HttpStatus.OK);
            }

            else if (SMSConstants.MESSAGE_TYPE_CALL.equals(messageType)) {
                String jsonResponse = smsService.processIncomingCall(from,message,messageType);

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.APPLICATION_JSON);

                return new ResponseEntity<>(jsonResponse,responseHeaders,HttpStatus.OK);
            }
        }

        else if (SMSConstants.ACTION_OUTGOING.equals(action)) {
                String to = smsAction.getTo();
                String texts = smsAction.getMessage();
                String textsType = smsAction.getType();
        }


        else if (SMSConstants.ACTION_SEND_STATUS.equals(action)) {
              String ID = smsAction.getId();
              String error_message = smsAction.getMessage();
              String status = smsAction.getStatus();
               if (SMSConstants.STATUS_FAILED.equals(status)){
                   String notification = smsService.errorResponse(error_message);

                   HttpHeaders headers = new HttpHeaders();
                   headers.setContentType(MediaType.APPLICATION_JSON);

                   return new ResponseEntity<>(notification,headers,HttpStatus.INTERNAL_SERVER_ERROR);
               }
               else if (SMSConstants.STATUS_SENT.equals(status)) {

                   String notification = smsService.sentResponse(ID,status);

                   HttpHeaders headers = new HttpHeaders();
                   headers.setContentType(MediaType.APPLICATION_JSON);

                   return new ResponseEntity<>(notification,headers,HttpStatus.OK);
               }
               else if (SMSConstants.STATUS_QUEUED.equals(status)) {

                   String response = smsService.queuedResponse(ID,status);

                   HttpHeaders httpHeaders = new HttpHeaders();
                   httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                   return new ResponseEntity<>(response,httpHeaders,HttpStatus.ACCEPTED);
               }
        }

        return ResponseEntity.badRequest().build();
    }
}
