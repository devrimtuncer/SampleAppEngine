package com.devrimtuncer.sample.utils;

import com.devrimtuncer.sample.backend.RegistrationRecord;
import com.devrimtuncer.sample.servlet.NotifyServlet;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.util.List;
import java.util.logging.Logger;

/**
 * class for cloud messaging operations
 *
 * Created by devrimtuncer on 28/03/16.
 */
public abstract class CloudMessagingUtils {
    private static final Logger LOGGER = Logger.getLogger(NotifyServlet.class.getName());

    private static final String API_KEY = System.getProperty("gcm.api.key");

    public static void sendGcmMessageWithSeparateThread(final List<RegistrationRecord> registrationRecordList, final String messageTitle, final String messageContent) {
        // TODO : Use bulk messaging better performance?
        for (RegistrationRecord registrationRecord : registrationRecordList) {
            sendGcmMessageWithSeparateThread(registrationRecord.getRegId(), messageTitle, messageContent);
        }
    }

    public static void sendGcmMessageWithSeparateThread(final String regId, final String messageTitle, final String messageContent) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sendGcmMessage(regId, messageTitle, messageContent);
            }
        };
        runnable.run();
    }

    private static void sendGcmMessage(String regId, String messageTitle, String messageContent) {
        try {
            if (TextUtils.isNullOrEmpty(regId)
                    || TextUtils.isNullOrEmpty(messageTitle)
                    || TextUtils.isNullOrEmpty(messageContent) ) {
                // Something wrong here!
                return;
            }

            // crop longer messages
            if (messageContent.length() > 1000) {
                messageContent = messageContent.substring(0, 1000) + "[...]";
            }

            List<RegistrationRecord> registrationList = RegistrationUtils.getRegistrations();
            RegistrationRecord registrationRecord = RegistrationUtils.getRegistration(registrationList, regId);

            if(registrationRecord != null) {
                Sender sender = new Sender(API_KEY);

                Message.Builder messageBuilder = new Message.Builder();
                messageBuilder.addData("title", messageTitle.trim());
                messageBuilder.addData("message", messageContent.trim());

                Message msg = messageBuilder.build();

                Result result = sender.send(msg, regId, 5);
                if (result.getMessageId() != null) {
                    String canonicalRegId = result.getCanonicalRegistrationId();
                    if (canonicalRegId != null) {
                        // registration must be updated
                        RegistrationUtils.updateRegistration(regId, canonicalRegId);
                    }
                } else {
                    String error = result.getErrorCodeName();
                    if (error.equals(Constants.ERROR_NOT_REGISTERED)
                            || error.equals(Constants.ERROR_INVALID_REGISTRATION)) {
                        LOGGER.warning("Registration Id " + registrationRecord.getRegId() + " no longer registered with GCM, removing from datastore");
                        // if the device is no longer registered with Gcm, remove it from the datastore
                        RegistrationUtils.removeRegistration(regId);
                    }
                    else {
                        LOGGER.warning("Error when sending message : " + error);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("SendMessage(" + regId + ") : FAILED. Exception: " + e);
        }
    }
}
