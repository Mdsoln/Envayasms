package com.iCaresms.iCaresms.Constants;

public class SMSConstants {
    //ACTIONS
    public static final String ACTION_INCOMING = "incoming";
    public static final String ACTION_FORWARD_SENT = "forward_sent";
    public static final String ACTION_SEND_STATUS = "send_status";
    public static final String ACTION_DEVICE_STATUS = "device_status";
    public static final String ACTION_TEST = "test";
    public static final String ACTION_OUTGOING = "outgoing";
    public static final String ACTION_AMQP_STARTED = "amqp_started";

    //STATUSES
    public static final String STATUS_QUEUED = "queued";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_SENT = "sent";
    public static final String STATUS_CANCELLED = "cancelled";

    //EVENTS
    public static final String EVENT_SEND = "send";
    public static final String EVENT_CANCEL = "cancel";
    public static final String EVENT_CANCEL_ALL = "cancel_all";
    public static final String EVENT_LOG = "log";
    public static final String EVENT_SETTINGS = "settings";

    //DEVICE
    public static final String DEVICE_STATUS_POWER_CONNECTED = "power_connected";
    public static final String DEVICE_STATUS_POWER_DISCONNECTED = "power_disconnected";
    public static final String DEVICE_STATUS_BATTERY_LOW = "battery_low";
    public static final String DEVICE_STATUS_BATTERY_OKAY = "battery_okay";
    public static final String DEVICE_STATUS_SEND_LIMIT_EXCEED = "send_limit_exceed";

    //TYPE OF MESSAGE
    public static final String MESSAGE_TYPE_SMS = "sms";
    public static final String MESSAGE_TYPE_MMS = "mms";
    public static final String MESSAGE_TYPE_CALL = "call";

    //POWER SOURCE
    public static final int POWER_SOURCE_BATTERY = 0;
    public static final int POWER_SOURCE_AC = 1;
    public static final int POWER_SOURCE_USB = 2;
}
