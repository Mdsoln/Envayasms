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
import java.util.Map;

@Service
public class SMSService {
    private final EnvayaRepo envayaRepo;
    private final OutgoingRepository outgoingRepository;

    @Autowired
    public SMSService(EnvayaRepo envayaRepo, OutgoingRepository outgoingRepository) {
        this.envayaRepo = envayaRepo;
        this.outgoingRepository = outgoingRepository;
    }


    public String processIncomingAction(String from, String message, String messageType) throws JSONException {
        //-->>>>saving to the database
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSender(from);
        smsMessage.setMessageContent(message);
        smsMessage.setMessageType(messageType);
        envayaRepo.save(smsMessage);
        //-->>>>json response
        JSONObject jsonResponse = new JSONObject();
        JSONArray eventArray = getJsonsArray(from, message, messageType);
        jsonResponse.put("events", eventArray);
        return jsonResponse.toString(3);
    }

    private static JSONArray getJsonsArray(String from, String message, String messageType) throws JSONException {
        JSONArray eventArray = new JSONArray();
        JSONObject eventObject = new JSONObject();
        JSONArray messageArray = new JSONArray();
        JSONObject messageObject = new JSONObject();

        messageObject.put("From ", from);
        messageObject.put("Message ", message);
        messageObject.put("Message type ", messageType);
        messageArray.put(messageObject);

        eventObject.put("event ", SMSConstants.ACTION_FORWARD_SENT);
        eventObject.put("messages ", messageArray);
        eventArray.put(eventObject);
        return eventArray;
    }

    /*polling outgoing messages from the database by the EnvayaSMS apk*/
    public String handleOutgoingSms() throws JSONException {
        List<OutgoinngSMS> outgoinngSMS = outgoingRepository.findByStatus(SMSConstants.STATUS_QUEUED);
            /* logging statements starts*/
            System.out.println("outgoing messages: " + outgoinngSMS.size());
            /* logging statements ends*/
            JSONArray list = new JSONArray();
            /*looping the table*/
        if (!outgoinngSMS.isEmpty()) {
            for (OutgoinngSMS outgoinngSMS1 : outgoinngSMS) {
                JSONObject outgoing = new JSONObject();
                /*set the event an apk should do*/
                outgoing.put("event",SMSConstants.EVENT_SEND);
                /*end of setting event*/
                /*starts of populating other fields of outgoing sms*/
                outgoing.put("id", outgoinngSMS1.getId());
                outgoing.put("recipient", outgoinngSMS1.getRecipient());
                outgoing.put("message", outgoinngSMS1.getContent());
                /*end of populating other fields of outgoing sms*/
                list.put(outgoing);
                /*update status of all polled messages in table*/
                outgoinngSMS1.setStatus(SMSConstants.STATUS_SENT);
                /*resend to the database with new status-->>>sent/-->>>canceled*/
                outgoingRepository.save(outgoinngSMS1);
            }
            JSONObject response = new JSONObject();
            response.put("events", list);
            /* logging statements starts*/
            System.out.println(response.toString(3));
            /* logging statements ends*/
            return response.toString(3);
        }
        else {
            List<OutgoinngSMS> outgoinngSMS1 = outgoingRepository.findByStatus(SMSConstants.STATUS_FAILED);
            //-->>>>number of failed sms
            System.out.println("Failed sms: "+outgoinngSMS1.size());
            if (outgoinngSMS1.size() < 2){
            //-->>>>looping the table
            for (OutgoinngSMS failed: outgoinngSMS1){
                JSONObject failedsms = new JSONObject();
                failedsms.put("event",SMSConstants.EVENT_CANCEL);
                failedsms.put("id",failed.getId());
                failedsms.put("recipient",failed.getRecipient());
                failedsms.put("message",failed.getContent());
                failedsms.put("status",failed.getStatus());
                list.put(failedsms);
                //-->>>>update the status of failed sms
                failed.setStatus(SMSConstants.STATUS_CANCELLED);
                outgoingRepository.save(failed);
            }
        }else {
                //-->>>>looping the table
                for (OutgoinngSMS failed: outgoinngSMS1){
                    JSONObject failedsms = new JSONObject();
                    failedsms.put("event",SMSConstants.EVENT_CANCEL_ALL);
                    failedsms.put("id",failed.getId());
                    failedsms.put("recipient",failed.getRecipient());
                    failedsms.put("message",failed.getContent());
                    failedsms.put("status",failed.getStatus());
                    list.put(failedsms);
                    //-->>>>update the status of failed sms
                    failed.setStatus(SMSConstants.STATUS_CANCELLED);
                    outgoingRepository.save(failed);
                }
              }
        JSONObject response = new JSONObject();
        response.put("events", list);
        System.out.println(response.toString(3));
        return response.toString(3);
        }
    }
            /*end of polling outgoing messages*/

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
