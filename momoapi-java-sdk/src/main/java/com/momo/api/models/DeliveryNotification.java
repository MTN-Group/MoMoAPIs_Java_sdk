package com.momo.api.models;

import com.momo.api.base.util.JSONFormatter;

/**
 *
 * Class DeliveryNotification
 */
public class DeliveryNotification {

    private String notificationMessage;

    /**
     *
     * @return
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     *
     * @param notificationMessage
     */
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    /**
     *
     * @return
     */
    public String toJSON() {
        return JSONFormatter.toJSON(this);
    }

}
