package dk.redweb.Red_App.Network;

import android.os.AsyncTask;
import dk.redweb.Red_App.Interfaces.Delegate_PushPMGroupSubscriptionUpdate;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.PushMessages.PushMessageInitializationHandling;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 16:20
 */
public class Handler_PushPMGroupSubscriptionUpdate extends AsyncTask<String, Void, String> {

    RedEventApplication _app;
    Delegate_PushPMGroupSubscriptionUpdate _delegate;
    XmlStore _xml;

    int _groupid;
    boolean _subscribed;

    public Handler_PushPMGroupSubscriptionUpdate(Delegate_PushPMGroupSubscriptionUpdate delegate, RedEventApplication app, int groupid, boolean subscribed) {
        _app = app;
        _delegate = delegate;
        _xml = app.getXmlStore();

        _groupid = groupid;
        _subscribed = subscribed;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            String url = _xml.appDataPath + "pushhost.php";

            MyLog.v("Start Upload with Handler_PushPMGroupSubscriptionUpdate");

            String regId = PushMessageInitializationHandling.getRegistrationId(_app);

            List<NameValuePair> posts = new ArrayList<NameValuePair>(4);
            posts.add(new BasicNameValuePair("action","updateGroupxPerson"));
            posts.add(new BasicNameValuePair("regid",String.valueOf(regId)));
            posts.add(new BasicNameValuePair("groupid",String.valueOf(_groupid)));
            posts.add(new BasicNameValuePair("subscribed",String.valueOf(_subscribed)));

            return HttpHandler.SendString(url, posts);
        } catch (Exception e){
            MyLog.e("Exception when sending pushmessagegroupXperson update to server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            _delegate.errorOccured(result);
            return;
        }
        MyLog.d("PushMessageGroup Server Update finished");
    }
}
