package dk.redweb.hca2014.Network;

import android.os.AsyncTask;
import android.util.Log;
import dk.redweb.hca2014.Interfaces.Delegate_dumpServer;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 4:16 PM
 */
public class Handler_DumpServer extends AsyncTask<String, Void, String>{
    private final Delegate_dumpServer delegate;
    private final XmlStore xmlStore;

    public Handler_DumpServer(Delegate_dumpServer delegate, XmlStore xmlStore){
        super();
        this.delegate = delegate;
        this.xmlStore = xmlStore;
    }

    protected String doInBackground(String... args){
        String url = xmlStore.appDataPath + "coreData.txt";

        Log.v("RedEvent", "Start Dump Download in Handler_DumpServer");

        try {
            String result = HttpHandler.GetString(url);
            return result;
        } catch (Exception e) {
            MyLog.e("Exception when getting update from server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            delegate.errorOccured(result);
            return;
        }
        Log.d("RedEvent","Server dump finished");
        delegate.returnFromDumpServer(result);
    }
}
