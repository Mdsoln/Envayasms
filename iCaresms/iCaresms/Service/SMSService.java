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
        JSONArray eventArray = getJsonArray(from, message, messageType);
        jsonResponse.put("events", eventArray);
        return jsonResponse.toString();
    }

    private static JSONArray getJsonArray(String from, String message, String messageType) throws JSONException {
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

    public String processIncomingCall(String from, String type) throws JSONException {
        JSONObject jsonResponse = new JSONObject();
        JSONArray eventArray = getJsonArrays(from,type);

        jsonResponse.put("events", eventArray);

        return jsonResponse.toString();
    }

    private static JSONArray getJsonArrays(String from, String type) throws JSONException {
        JSONArray eventArray = new JSONArray();
        JSONObject eventObject = new JSONObject();

        JSONArray messageArray = new JSONArray();
        JSONObject messageObject = new JSONObject();

        messageObject.put("From ", from);
        messageObject.put("Message ", "");
        messageObject.put("Message type ", type);

        messageArray.put(messageObject);

        eventObject.put("event ", "call notification");
        eventObject.put("messages ", messageArray);

        eventArray.put(eventObject);
        return eventArray;
    }

    // METHODS FOR SEND_STATUS 'TO THE SERVER' ACTIONS AND RESPONSE IN JSON FORMAT

    private static JSONArray getErrorResponse(String error_message) throws JSONException {
        JSONArray reasonForError = new JSONArray();
        JSONObject reason_for_error = new JSONObject();

        reason_for_error.put("message", error_message);
        reasonForError.put(reason_for_error);
        return reasonForError;
    }

    public String sentResponse(String ID, String status_message) throws JSONException {
        JSONObject sentResponse = new JSONObject();
        JSONArray response = getSentResponse(ID, status_message);

        sentResponse.put("Event", response);

        return sentResponse.toString();
    }

    private JSONArray getSentResponse(String id, String statusMessage) throws JSONException {
        JSONArray event = new JSONArray();
        JSONObject status_event = new JSONObject();

        JSONArray statusArray = new JSONArray();
        JSONObject statusObject = new JSONObject();

        statusObject.put("id", id);
        statusObject.put("status", statusMessage);
        statusArray.put(statusObject);

        status_event.put("message", statusArray);
        event.put(status_event);
        return event;
    }

    public String queuedResponse(String server_ID, String status) throws JSONException {
        JSONArray queuedArray = new JSONArray();
        JSONObject queuedObject = new JSONObject();

        JSONArray eventQueued = new JSONArray();
        JSONObject objectQueued = new JSONObject();
        objectQueued.put("server_id", server_ID);
        objectQueued.put("status", status);
        eventQueued.put(objectQueued);

        queuedObject.put("event", eventQueued);
        queuedArray.put(queuedObject);
        return queuedArray.toString();
    }

    //-->>>>get form-data as parameters
    public String getParameterValue(Map<String, String[]> params, String paramName) {
        String[] values = params.get(paramName);
        return values != null && values.length > 0 ? values[0] : null;
    }

    //-->>>>polling outgoing messages from the database by the EnvayaSMS apk
    public String handleOutgoingSms() throws JSONException {
        List<OutgoinngSMS> outgoinngSMS = outgoingRepository.findByStatus(SMSConstants.STATUS_QUEUED);
        //-->>>>logging statement for counting number of fetched sms
        System.out.println("outgoing messages: " + outgoinngSMS.size());
          JSONArray list = new JSONArray();
          //-->>>>looping the table
        if (!outgoinngSMS.isEmpty()) {
            for (OutgoinngSMS outgoinngSMS1 : outgoinngSMS) {
                JSONObject outgoing = new JSONObject();
                outgoing.put("event", SMSConstants.EVENT_SEND);
                outgoing.put("id", outgoinngSMS1.getId());
                outgoing.put("recipient", outgoinngSMS1.getRecipient());
                outgoing.put("message", outgoinngSMS1.getContent());
                list.put(outgoing);
                //-->>>>update status of all polled messages in table
                outgoinngSMS1.setStatus(SMSConstants.STATUS_SENT);
                outgoingRepository.save(outgoinngSMS1);
            }
            JSONObject response = new JSONObject();
            response.put("events", list);
            return response.toString();
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
        return response.toString();
        }
    }

    //-->>>>error message comes from here
    public ResponseEntity<String> error() throws JSONException {
        JSONArray event = new JSONArray();
        JSONObject eventObject = new JSONObject();
        eventObject.put("message","are you hiring");
        event.put(eventObject);
        JSONArray errorArray = new JSONArray();
        JSONObject errorObject = new JSONObject();
        errorObject.put("error",event);
        errorArray.put(errorObject);
        return new ResponseEntity<>(errorArray.toString(), HttpStatus.BAD_REQUEST);
    }

}
