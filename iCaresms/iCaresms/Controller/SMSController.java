package com.iCaresms.iCaresms.Controller;
import com.iCaresms.iCaresms.Constants.SMSConstants;
import com.iCaresms.iCaresms.EnvayaSMS.OutgoinngSMS;
import com.iCaresms.iCaresms.Service.SMSService;
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
@RequestMapping(path = "/sms")
public class SMSController {
    private final SMSService smsService;
    @Autowired
    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

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

            switch (messageType) {
                case SMSConstants.MESSAGE_TYPE_SMS, SMSConstants.MESSAGE_TYPE_MMS -> {
                    String jsonResponse = smsService.processIncomingAction(from, message, messageType);
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    return new ResponseEntity<>(jsonResponse, httpHeaders, HttpStatus.OK);
                }
                case SMSConstants.MESSAGE_TYPE_CALL -> {
                    String json = smsService.processIncomingCall(from,messageType);
                    HttpHeaders http = new HttpHeaders();
                    http.setContentType(MediaType.APPLICATION_JSON);
                    return new ResponseEntity<>(json, http, HttpStatus.OK);
                }
            }
        }

        //-->>>>polling is here
        else if (SMSConstants.ACTION_OUTGOING.equals(action)) {
            String outgoingsms = smsService.handleOutgoingSms();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(outgoingsms,headers,HttpStatus.OK);
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
        //-->>>>alert the client
        return smsService.error();
    }
}
