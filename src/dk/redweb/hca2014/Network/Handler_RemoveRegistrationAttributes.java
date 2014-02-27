package dk.redweb.hca2014.Network;

import android.os.AsyncTask;
import dk.redweb.hca2014.Interfaces.Delegate_removeRegistrationAttributes;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/25/13
 * Time: 12:36
 */
public class Handler_RemoveRegistrationAttributes extends AsyncTask<String, Void, String> {
    private final Delegate_removeRegistrationAttributes _delegate;
    private final XmlStore _xml;

    private final String _registrationId;

    public Handler_RemoveRegistrationAttributes(Delegate_removeRegistrationAttributes delegate, RedEventApplication app, String registrationId){
        super();
        _delegate = delegate;
        _xml = app.getXmlStore();

        _registrationId = registrationId;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            String url = _xml.appDataPath + "pushhost.php";

            MyLog.v("Start Upload with Handler_RemoveRegistrationAttributes");

            List<NameValuePair> posts = new ArrayList<NameValuePair>(4);
            posts.add(new BasicNameValuePair("action","removeUser"));
            posts.add(new BasicNameValuePair("regid",_registrationId));

            return HttpHandler.SendString(url, posts);
        } catch (Exception e){
            MyLog.e("Exception when sending registration removal data to server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            _delegate.errorOccured(result);
            return;
        }
        MyLog.d("PushMessage Registration Removal finished");
    }
}
