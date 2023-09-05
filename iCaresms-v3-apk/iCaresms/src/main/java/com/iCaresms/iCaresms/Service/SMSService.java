package com.iCaresms.iCaresms.Service;

import com.iCaresms.iCaresms.Constants.SMSConstants;
import com.iCaresms.iCaresms.EnvayaRepository.OutgoingRepository;
import com.iCaresms.iCaresms.EnvayaSMS.OutgoinngSMS;
import com.iCaresms.iCaresms.EnvayaSMS.SMSMessage;
import com.iCaresms.iCaresms.EnvayaRepository.EnvayaRepo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SMSService {
    private final EnvayaRepo envayaRepo;
    private final OutgoingRepository outgoingRepository;

    @Autowired
    public SMSService(EnvayaRepo envayaRepo, OutgoingRepository outgoingRepository) {
        this.envayaRepo = envayaRepo;
        this.outgoingRepository = outgoingRepository;
    }
        /*we save here an incoming SMS*/
    public void processIncomingAction(String from, String message, String type){
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSender(from);
        smsMessage.setMessageContent(message);
        smsMessage.setMessageType(type);
        envayaRepo.save(smsMessage);
    }

    /*polling outgoing messages from the database by the EnvayaSMS apk*/
    public JSONArray handleOutgoingSms() throws JSONException {
        List<OutgoinngSMS> outgoingSMSList = outgoingRepository.findByStatus("queued");

        JSONArray eventsArray = new JSONArray();
        for (OutgoinngSMS outgoingSMS : outgoingSMSList) {
            JSONObject eventObject = getJsonObject(outgoingSMS);
            eventsArray.put(eventObject);
            outgoingSMS.setStatus(SMSConstants.STATUS_SENT);
            outgoingRepository.save(outgoingSMS);
        }

        return eventsArray;
    }
    private static JSONObject getJsonObject(OutgoinngSMS outgoingSMS) throws JSONException {
        JSONObject eventObject = new JSONObject();
        eventObject.put("event", SMSConstants.EVENT_SEND);
        JSONArray messagesArray = new JSONArray();
        JSONObject messageObject = new JSONObject();

        messageObject.put("id", String.valueOf(outgoingSMS.getId()));
        messageObject.put("to", outgoingSMS.getRecipient());
        messageObject.put("message", outgoingSMS.getMessage());
        messagesArray.put(messageObject);

        eventObject.put("messages", messagesArray);
        return eventObject;
    }


    /*generate an error message for other actions if occur*/
    public ResponseEntity<String> error() throws JSONException {
        JSONArray event = new JSONArray();
        JSONObject eventObject = new JSONObject();
        eventObject.put("message","unsupported action!!!");
        event.put(eventObject);
        JSONArray errorArray = new JSONArray();
        JSONObject errorObject = new JSONObject();
        errorObject.put("error",event);
        errorArray.put(errorObject);
        return new ResponseEntity<>(errorArray.toString(3), HttpStatus.BAD_REQUEST);
    }
}
