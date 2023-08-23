package com.iCaresms.iCaresms;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/envayasms")
public class SMSController {

    @Autowired
    private SMSService smsService;
    @CrossOrigin()
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleRequest(HttpServletRequest request) throws JSONException {
        Map<String,String[]> params = request.getParameterMap();

        String action = smsService.getParameterValue(params,"action");
        if (action == null){
            return ResponseEntity.badRequest().build();
        }
              // ACTION-->>INCOMING
        if (SMSConstants.ACTION_INCOMING.equals(action)){
            String from = smsService.getParameterValue(params,"from");
            String message = smsService.getParameterValue(params,"message");
            String messageType = smsService.getParameterValue(params,"messageType");
            if (from == null || message == null || messageType == null){
                return ResponseEntity.badRequest().build();
            }
            if (SMSConstants.MESSAGE_TYPE_SMS.equals(messageType)){
                String jsonResponse = smsService.processIncomingAction(from,message,messageType);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(jsonResponse,httpHeaders,HttpStatus.OK);
            } else if (SMSConstants.MESSAGE_TYPE_MMS.equals(messageType)) {
                String jsonResponse = smsService.processIncomingAction(from,message,messageType);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(jsonResponse,httpHeaders,HttpStatus.OK);
            } else if (SMSConstants.MESSAGE_TYPE_CALL.equals(messageType)) {
                String json = smsService.processIncomingCall(from,message,messageType);
                HttpHeaders http = new HttpHeaders();
                http.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(json,http,HttpStatus.OK);
            }
        }
           // ACTION-->>SEND_STATUS
        else if (SMSConstants.ACTION_SEND_STATUS.equals(action)) {
            String ID = smsService.getParameterValue(params,"id");
            String status = smsService.getParameterValue(params,"status");

             if (SMSConstants.STATUS_SENT.equals(status)) {
                 String json = smsService.sentResponse(ID, status);
                 HttpHeaders headers = new HttpHeaders();
                 headers.setContentType(MediaType.APPLICATION_JSON);
                 return new ResponseEntity<>(json, headers, HttpStatus.OK);
             } else if (SMSConstants.STATUS_QUEUED.equals(status)) {
                 String json = smsService.queuedResponse(ID,status);
                 HttpHeaders headers = new HttpHeaders();
                 headers.setContentType(MediaType.APPLICATION_JSON);
                 return new ResponseEntity<>(json,headers,HttpStatus.ACCEPTED);
             }
        }
         
        //-->>>>outgoing
        else if (SMSConstants.ACTION_OUTGOING.equals(action)) {
            List<OutgoingMessage> outgoingMessages = smsService.getOutgoingMessage();
            String jsonResponse = smsService.convertOutgoingMessagesToJson(outgoingMessages);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(jsonResponse, headers, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }
}
