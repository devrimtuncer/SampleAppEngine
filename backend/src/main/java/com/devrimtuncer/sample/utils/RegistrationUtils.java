package com.devrimtuncer.sample.utils;

import com.devrimtuncer.sample.backend.RegistrationRecord;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.devrimtuncer.sample.backend.OfyService.ofy;

/**
 * Created by devrimtuncer on 28/03/16.
 */
public class RegistrationUtils {

    private static final boolean USE_MEM_CACHE = true;

    private static final int LOAD_AT_A_TIME = 10000;
    private static final String CACHE_KEY = "CACHE_KEY_REGISTRATIONS";

    public static boolean addRegistration(RegistrationRecord registrationRecord) {
        List<RegistrationRecord> registrationList = getRegistrations();

        RegistrationRecord savedRecord = getRegistration(registrationList, registrationRecord.getRegId());

        if (savedRecord != null) {
            // already registered
            return false;
        } else {
            // TODO: Calendar.getInstance() with turkish locale?
            registrationRecord.setCreateTime(Calendar.getInstance().getTime());

            ofy().save().entity(registrationRecord).now();
            registrationList.add(registrationRecord);
        }

        if(USE_MEM_CACHE) {
            CacheUtils.putIntoCache(CACHE_KEY, registrationList);
        }
        return true;
    }

    public static boolean updateRegistration(String oldRegId, String newRegId) {
        List<RegistrationRecord> registrationList = getRegistrations();

        RegistrationRecord savedRecord = getRegistration(registrationList, oldRegId);

        if (savedRecord == null) {
            // not registered, something wrong here!
            return false;
        } else {
            savedRecord.setRegId(newRegId);
            ofy().save().entity(savedRecord).now();
            if(USE_MEM_CACHE) {
                CacheUtils.putIntoCache(CACHE_KEY, registrationList);
            }
            return true;
        }
    }

    public static boolean removeRegistration(String regId) {
        List<RegistrationRecord> registrationList = getRegistrations();

        RegistrationRecord savedRecord = getRegistration(registrationList, regId);

        if (savedRecord == null) {
            // not registered
            return false;
        } else {
            ofy().delete().entity(savedRecord).now();
            registrationList.remove(savedRecord);
            if(USE_MEM_CACHE) {
                CacheUtils.putIntoCache(CACHE_KEY, registrationList);
            }
            return true;
        }
    }

    public static RegistrationRecord getRegistration(String regId) {
        List<RegistrationRecord> registrationList = getRegistrations();
        return getRegistration(registrationList, regId);
    }

    public static RegistrationRecord getRegistration(List<RegistrationRecord> registrationList, String regId) {
        RegistrationRecord record = null;

        if(registrationList != null && registrationList.size() > 0) {
            RegistrationRecord key = new RegistrationRecord();
            key.setRegId(regId);

            int index = Collections.binarySearch(registrationList, key, RegistrationUtils.registrationComparator);
            if (index >= 0) {
                record = registrationList.get(index);
            }
        }
        return record;
    }

    public static List<RegistrationRecord> getRegistrations() {
        List<RegistrationRecord> registrationRecordList;
        Object cachedObject = null;
        if(USE_MEM_CACHE) {
            cachedObject = CacheUtils.getFromCache(CACHE_KEY);
        }
        if(cachedObject == null) {
            registrationRecordList = new ArrayList<>();
            Query<RegistrationRecord> query = ofy().load().type(RegistrationRecord.class).limit(LOAD_AT_A_TIME);

            boolean hasMore = true;
            Cursor cursor = null;

            while(hasMore) {
                if(cursor != null) {
                    query = query.startAt(cursor);
                }

                QueryResultIterator<RegistrationRecord> iterator = query.iterator();
                int itemCount = 0;
                while (iterator.hasNext()) {
                    itemCount ++;
                    RegistrationRecord record = iterator.next();
                    registrationRecordList.add(record);
                }

                cursor = iterator.getCursor();
                if(itemCount < LOAD_AT_A_TIME) {
                    hasMore = false;
                }
            }
            if(USE_MEM_CACHE) {
                CacheUtils.putIntoCache(CACHE_KEY, registrationRecordList);
            }
        } else {
            registrationRecordList = (List<RegistrationRecord>) cachedObject;
        }

        return registrationRecordList;
    }

    // comparators
    private static Comparator<RegistrationRecord> registrationComparator = new Comparator<RegistrationRecord>() {

        public int compare(RegistrationRecord record1, RegistrationRecord record2) {
            return record1.getRegId().compareTo(record2.getRegId());
        }
    };
}
