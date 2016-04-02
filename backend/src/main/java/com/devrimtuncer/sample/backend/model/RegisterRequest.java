package com.devrimtuncer.sample.backend.model;

import com.devrimtuncer.sample.utils.TextUtils;
import com.google.api.server.spi.response.BadRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devrimtuncer on 03/09/15.
 */
public class RegisterRequest extends BaseRequest {
    private String regId;

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    @Override
    public void validate() throws BadRequestException {
        super.validate();
        validateRegId(regId);
    }

    @Override
    public List<String> getHashValues() {
        List<String> subHashValues = new ArrayList<>(1);
        subHashValues.add(regId);
        return subHashValues;
    }

    private static void validateRegId(String regId) throws BadRequestException {
        if(TextUtils.isNullOrEmpty(regId)) {
            throw new BadRequestException("Invalid RegId.");
        }
    }

}
