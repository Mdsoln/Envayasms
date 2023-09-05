package com.iCaresms.iCaresms.Controller;
import com.iCaresms.iCaresms.Constants.SMSConstants;
import com.iCaresms.iCaresms.Service.SMSService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(path = "/sms")
public class SMSController {
    private final SMSService smsService;
    @Autowired
    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }
    @CrossOrigin()
    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleRequest(
            @RequestParam(name = "action") String action,
            @RequestParam(name = "from",required =false) String from,
            @RequestParam(name = "message",required = false) String message,
            @RequestParam(name = "messageType",required = false) String messageType
            )throws JSONException{

            Map<String,Object> response = new HashMap<>();

            if (SMSConstants.ACTION_INCOMING.equals(action)){
                smsService.processIncomingAction(from, message, messageType);
                                            } else if (SMSConstants.ACTION_OUTGOING.equals(action)) {
                JSONArray eventsJson = smsService.handleOutgoingSms();
                response.put("events",eventsJson);
            }else {
                response.put("Error",smsService.error());
            }
            return new ResponseEntity<>(new JSONObject(response).toString(), HttpStatus.OK);
    }
}
