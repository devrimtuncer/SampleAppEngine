package com.devrimtuncer.sample.servlet;

import com.devrimtuncer.sample.backend.RegistrationRecord;
import com.devrimtuncer.sample.utils.CloudMessagingUtils;
import com.devrimtuncer.sample.utils.RegistrationUtils;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is going to be called by a cron task. see cron.cml
 *
 * Created by devrimtuncer on 28/03/16.
 */
public class NotifyServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        process();
    }

    private void process() {
        // Your logic here
        // notify users
    }

    private void notifyAllUsers(String messageTitle, String messageContent) {
        List<RegistrationRecord> registrationRecordList = RegistrationUtils.getRegistrations();
        CloudMessagingUtils.sendGcmMessageWithSeparateThread(registrationRecordList, messageTitle, messageContent);
    }
}
