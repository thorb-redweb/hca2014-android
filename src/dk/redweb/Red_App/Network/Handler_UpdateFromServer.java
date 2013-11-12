package dk.redweb.Red_App.Network;

import android.os.AsyncTask;
import android.util.Log;
import dk.redweb.Red_App.Interfaces.Delegate_updateFromServer;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/16/13
 * Time: 9:41 AM
 */
public class Handler_UpdateFromServer extends AsyncTask<String, Void, String> {
    private final Delegate_updateFromServer delegate;
    private final RedEventApplication _app;
    private final XmlStore _xml;
    private final int _version;

    public Handler_UpdateFromServer(Delegate_updateFromServer delegate, RedEventApplication app){
        super();
        this.delegate = delegate;
        this._app = app;
        this._xml = app.getXmlStore();
        _version = _app.getDatabaseDataVersion();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = _xml.appDataPath + "hcam-" + _version +".txt";

        MyLog.v("Start Update Download in Handler_UpdateFromServer with url: " + url);

        try{
            String result = HttpHandler.GetString(url);
            return result;
        }
        catch (Exception e){
            MyLog.e("Exception when getting update from server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            delegate.errorOccured(result);
            return;
        }
        Log.d("RedEvent","Update download finished");
        delegate.returnFromUpdateFromServer(result);
    }
}
