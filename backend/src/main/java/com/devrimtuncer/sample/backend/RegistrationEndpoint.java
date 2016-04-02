/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.devrimtuncer.sample.backend;

import com.devrimtuncer.sample.backend.model.RegisterRequest;
import com.devrimtuncer.sample.utils.RegistrationUtils;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 *
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
  name = "registration",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.sample.devrimtuncer.com",
    ownerName = "backend.sample.devrimtuncer.com",
    packagePath=""
  )
)
public class RegistrationEndpoint {
    /**
     * Register a device to the backend
     *
     * @param registerRequest Model which has the Google Cloud Messaging registration Id to add
     */
    @ApiMethod(name = "register")
    public void registerDevice(RegisterRequest registerRequest) throws Exception{
        // 1) Validate incoming request
        registerRequest.validate();

        // 2) Save registration
        RegistrationRecord record = new RegistrationRecord();
        record.setRegId(registerRequest.getRegId());
        RegistrationUtils.addRegistration(record);
    }

    /**
     * Return a collection of registered devices,
     *
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "listAllDevices")
    public CollectionResponse<RegistrationRecord> listAllDevices() {
        // TODO: Remove this method for vulnerability or allow access to admin only
        List<RegistrationRecord> records = RegistrationUtils.getRegistrations();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }
}
