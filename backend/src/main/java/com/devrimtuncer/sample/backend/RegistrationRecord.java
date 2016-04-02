package com.devrimtuncer.sample.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/** The Objectify object model for device registrations we are persisting */
@Entity
public class RegistrationRecord implements Serializable {

    @Id
    Long id;

    @Index
    private String regId;

    // you can add more fields...

    public RegistrationRecord() {}

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}