package dk.redweb.Red_App.Network;

import android.os.AsyncTask;
import dk.redweb.Red_App.Interfaces.Delegate_uploadRegistrationAttributes;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/25/13
 * Time: 12:36
 */
public class Handler_UploadRegistrationAttributes extends AsyncTask<String, Void, String> {
    private final Delegate_uploadRegistrationAttributes _delegate;
    private final XmlStore _xml;

    private final String _registrationId;
    private final String _userName;

    public Handler_UploadRegistrationAttributes(Delegate_uploadRegistrationAttributes delegate, RedEventApplication app, String registrationId, String userName){
        super();
        _delegate = delegate;
        _xml = app.getXmlStore();

        _registrationId = registrationId;
        _userName = userName;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            String url = _xml.appDataPath + "pushhost.php";

            MyLog.v("Start Upload with Handler_UploadRegistrationAttributes");

            List<NameValuePair> posts = new ArrayList<NameValuePair>(4);
            posts.add(new BasicNameValuePair("action","uploadUserData"));
            posts.add(new BasicNameValuePair("platformid","1"));
            posts.add(new BasicNameValuePair("regid",_registrationId));
            posts.add(new BasicNameValuePair("name",_userName));

            return HttpHandler.SendString(url, posts);
        } catch (Exception e){
            MyLog.e("Exception when sending registration data update to server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            _delegate.errorOccured(result);
            return;
        }
        MyLog.d("PushMessage Registration Attributes Upload finished");
        _delegate.returnFromUploadToServer(result);
    }
}
