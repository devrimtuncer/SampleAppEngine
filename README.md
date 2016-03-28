# SampleAppEngine

This is a simple app engine module template which uses;

<ol>
<li>DataStore (see RegistrationUtils and OfyService)</li>
<li>Memcache (see RegistrationUtils and CacheUtils)</li>
<li>Google Cloud Messaging Registration and Notification (see RegistrationEndpoint and NotifyServlet)</li>
<li>Cron Tasks (see cron.xml)</li>
<li>Unit Tests for DataStore and MemCache operations (see NotificationTest and RegistrationTest)</li>
</ol>

<b>NOTE:</b> Do not forget to set your api key at appengine-web.xml file.

You can test this template at http://localhost:8080/. Use <i>Cloud Endpoints API Explorer</i> to simulate services. You may have to start your chrome browser at unsafe mode, if you get below error message:
<p>
The API you are exploring is hosted over HTTP, which can cause problems. Learn how to use Explorer with a local HTTP API.
</p>

To start chrome browser at unsafe mode: <br/>
open /Applications/Google\ Chrome.app --args --user-data-dir=test --unsafely-treat-insecure-origin-as-secure=http://localhost:8080
