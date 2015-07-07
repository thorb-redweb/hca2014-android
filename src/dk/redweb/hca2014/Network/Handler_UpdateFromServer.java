package dk.redweb.hca2014.Network;

import android.os.AsyncTask;
import dk.redweb.hca2014.Interfaces.Delegate_updateFromServer;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //if update file does not exist on server
        if(result.contains("404 Not Found")){
            delegate.updateFromServerWithCoreData();
            return;
        }

        if(result.length() > 0){
            try {
                //Get the system object from the array. As currently designed, the system object is always the last object
                JSONArray resultArray = new JSONArray(result);
                JSONObject sysObject = resultArray.getJSONObject(resultArray.length() - 1);

                //check if the system's current version is below the version where it should be updated with coredata
                //if it is, update it with coredata
                int coredataUpdateVersion = sysObject.getInt("coredataupdateversion");
                int databaseDataVersion = _app.getDatabaseDataVersion();
                if(coredataUpdateVersion > databaseDataVersion){
                    delegate.updateFromServerWithCoreData();
                    return;
                }
            } catch (JSONException e) {
                delegate.errorOccured("Error: Failure when acquiring coredataupdateversion");
                return;
            }
        }

        delegate.returnFromUpdateFromServer(result);
    }
}
