package com.devrimtuncer.sample.backend.model;

import com.devrimtuncer.sample.utils.HashUtils;
import com.devrimtuncer.sample.utils.TextUtils;
import com.google.api.server.spi.response.BadRequestException;

import java.util.List;

/**
 *
 * This should be used as a base request class which all requests must extend
 *
 * Created by devrimtuncer on 03/09/15.
 */
public abstract class BaseRequest {
    // keep secret your EXTRA_HASH_PARAM value
    private static final String EXTRA_HASH_PARAM = "secret extra hash param";
    private int appVersionCode;
    private String timestamp;
    private String hashValue;

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp    (String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }


    void validate() throws BadRequestException {
        validateAppVersionCode(appVersionCode);
        validateTimestamp(timestamp);

        validateHash();
    }

    private void validateHash() throws BadRequestException{
        if(TextUtils.isNullOrEmpty(hashValue)) {
            throw new BadRequestException("Invalid Request [01].");
        }

        String expectedHashValue = createExpectedHashValue();
        if(!hashValue.equals(expectedHashValue)) {
            throw new BadRequestException("Invalid Request [02].");
        }
    }

    private String createExpectedHashValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(EXTRA_HASH_PARAM);
        sb.append(appVersionCode);
        sb.append(timestamp);

        List<String> subHasValueList = getHashValues();
        if(subHasValueList != null && subHasValueList.size() > 0) {
            for (String subHashValue: subHasValueList) {
                sb.append(subHashValue);
            }
        }

        return HashUtils.getHash(sb.toString());
    }

    public static void validateAppVersionCode(int appVersionCode) throws BadRequestException {
        if(0 == appVersionCode) {
            throw new BadRequestException("Invalid Request [03].");
        }
    }

    public static void validateTimestamp(String timestamp) throws BadRequestException {
        if(TextUtils.isNullOrEmpty(timestamp)) {
            throw new BadRequestException("Invalid Request [04].");
        }
    }

    protected abstract List<String> getHashValues();
}
