package com.iCaresms.iCaresms;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

@Service
public class SMSService {

        // METHODS FOR INCOMING ACTIONS AND RESPONSE IN JSON FORMAT
    public  String processIncomingAction(String from, String message,String messageType) throws JSONException {
        JSONObject jsonResponse = new JSONObject();

        JSONArray eventArray = getJsonArray(from, message, messageType);

        jsonResponse.put("events",eventArray);

        return jsonResponse.toString();
    }

    private static JSONArray getJsonArray(String from, String message, String messageType) throws JSONException {
        JSONArray  eventArray = new JSONArray();
        JSONObject eventObject = new JSONObject();

        JSONArray messageArray = new JSONArray();
        JSONObject messageObject = new JSONObject();

        messageObject.put("From ", from);
        messageObject.put("Message ", message);
        messageObject.put("Message type ", messageType);

        messageArray.put(messageObject);

        eventObject.put("event ","send");
        eventObject.put("messages ",messageArray);

        eventArray.put(eventObject);
        return eventArray;
    }

    public String processIncomingCall(String from,String message,String type) throws JSONException {
        JSONObject jsonResponse = new JSONObject();
        JSONArray eventArray = getJsonArrays(from, message, type);

        jsonResponse.put("events",eventArray);

        return jsonResponse.toString();
    }

    private static JSONArray getJsonArrays(String from, String message, String type) throws JSONException {
        JSONArray  eventArray = new JSONArray();
        JSONObject eventObject = new JSONObject();

        JSONArray messageArray = new JSONArray();
        JSONObject messageObject = new JSONObject();

        messageObject.put("From ", from);
        messageObject.put("Message ", message);
        messageObject.put("Message type ", type);

        messageArray.put(messageObject);

        eventObject.put("event ","send[call]");
        eventObject.put("messages ",messageArray);

        eventArray.put(eventObject);
        return eventArray;
    }

    // METHODS FOR SEND_STATUS 'TO THE SERVER' ACTIONS AND RESPONSE IN JSON FORMAT

    public String errorResponse(String errorMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = getErrorResponse(errorMessage);

        jsonObject.put("Error",jsonArray);

        return jsonObject.toString();
    }
    private static JSONArray getErrorResponse(String error_message) throws JSONException {
        JSONArray reasonForError = new JSONArray();
        JSONObject reason_for_error = new JSONObject();

        reason_for_error.put("message", error_message);
        reasonForError.put(reason_for_error);
        return reasonForError;
    }

    public String sentResponse(String ID,String status_message) throws JSONException {
        JSONObject sentResponse = new JSONObject();
        JSONArray response = getSentResponse(ID,status_message);

        sentResponse.put("Event",response);

        return sentResponse.toString();
    }

    private JSONArray getSentResponse(String id, String statusMessage) throws JSONException {
        JSONArray event = new JSONArray();
        JSONObject status_event = new JSONObject();

        JSONArray statusArray = new JSONArray();
        JSONObject statusObject = new JSONObject();

        statusObject.put("id",id);
        statusObject.put("status",statusMessage);
        statusArray.put(statusObject);

        status_event.put("message",statusArray);
        event.put(status_event);
        return event;
    }

    public String queuedResponse(String server_ID,String status) throws JSONException {
        JSONArray queuedArray = new JSONArray();
        JSONObject queuedObject = new JSONObject();

        JSONArray eventQueued = new JSONArray();
        JSONObject objectQueued = new JSONObject();
        objectQueued.put("server_id",server_ID);
        objectQueued.put("status",status);
        eventQueued.put(objectQueued);

        queuedObject.put("event",eventQueued);
        queuedArray.put(queuedObject);
        return queuedArray.toString();
    }
}
