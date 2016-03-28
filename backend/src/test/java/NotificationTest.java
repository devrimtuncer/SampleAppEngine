import com.devrimtuncer.sample.backend.RegistrationRecord;
import com.devrimtuncer.sample.utils.CloudMessagingUtils;
import com.devrimtuncer.sample.utils.RegistrationUtils;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cache.AsyncCacheFilter;
import com.googlecode.objectify.util.Closeable;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 *
 * test class for notifications. This test won't send any cloud messages to clients.
 *
 * Created by devrimtuncer on 28/03/16.
 */
public class NotificationTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    protected Closeable session;

    @BeforeClass
    public static void setUpBeforeClass() {
        // Reset the Factory so that all translators work properly.
        ObjectifyService.setFactory(new ObjectifyFactory());
        ObjectifyService.register(RegistrationRecord.class);

        System.setProperty("gcm.api.key", "dummy_api_key");
    }

    @Before
    public void setUp() {
        this.session = ObjectifyService.begin();
        this.helper.setUp();
    }

    @After
    public void tearDown() {
        AsyncCacheFilter.complete();
        this.session.close();
        this.helper.tearDown();
    }

    @Test
    public void doTest() {
        // Store some registration records
        int registrationCount = 5;
        for(int i = 0; i < registrationCount; i ++) {
            RegistrationRecord record = new RegistrationRecord();
            record.setRegId(String.valueOf(i));
            RegistrationUtils.addRegistration(record);
        }

        // Retrieve stored registration records
        List<RegistrationRecord> registrationRecordList = RegistrationUtils.getRegistrations();

        assertNotNull("registrationRecordList can not be null!", registrationRecordList);
        assertEquals("registrationRecordList's size unexpected!", registrationRecordList.size(), registrationCount);

        // Send cloud message to all registration records
        CloudMessagingUtils.sendGcmMessageWithSeparateThread(registrationRecordList, "hello", "hello world!");

        // Query a registration record
        String registrationIdToQuery = String.valueOf(3);
        RegistrationRecord registrationRecord = RegistrationUtils.getRegistration(registrationIdToQuery);

        assertNotNull("registrationRecord with regId : " + registrationIdToQuery + " null!", registrationRecord);
        assertEquals("Retrieved registrationRecord unexpected!", registrationRecord.getRegId(), registrationIdToQuery);

        // Send a cloud message to a specific registration record
        CloudMessagingUtils.sendGcmMessageWithSeparateThread(registrationIdToQuery, "hello", "hello world!");

    }
}
