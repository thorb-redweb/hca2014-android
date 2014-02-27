package dk.redweb.hca2014.Database;

import android.os.AsyncTask;
import android.util.Log;
import dk.redweb.hca2014.Interfaces.Delegate_dumpToDatabase;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 12:15 PM
 */
public class Handler_DumpToDatabase extends AsyncTask<String, Void, String> {

    private final RedEventApplication _app;
    private final DbInterface _db;
    private final Delegate_dumpToDatabase _delegate;
    private int newVersion;

    public Handler_DumpToDatabase(RedEventApplication app, DbInterface db, Delegate_dumpToDatabase delegate){
        super();
        _app = app;
        _db = db;
        _delegate = delegate;
        newVersion = 0;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = params[0];

        try{
            if(result.length() == 0 || result.toCharArray()[0] != '[')
            {
                if(result.length() == 0){
                    MyLog.e("Empty result string received by Handler_DumpToDatabase:doInBackground.");
                } else if (result.contains("The requested URL") && result.contains("was not found on this server.")){
                    MyLog.e("The requested file does not exist: " + result);
                } else {
                    MyLog.e("Unknown string received on call to server");
                }
                MyLog.e("Dump to database was halted due to Error");
                return "Error: En fejl skete under kontakt med databasen. Prøv at starte appen igen.";
            }
            else
            {
                Log.d("RedEvent", "Starting database load from GetJSONDatabase.onPostExecute...");
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String itemtype = jsonObject.getString("itemtype");
                    if(itemtype.equals("sys"))
                    {
                        int newVersion = jsonObject.getInt("version");
                        MyLog.v("Updating LatestDatabaseVersion to " + newVersion);
                        this.newVersion = newVersion;
                    }
                    else if(itemtype.equals("a"))
                    {
                        MyLog.v("Creating Article ID: " + jsonObject.getInt(JsonSchemas.Art.ARTICLE_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.Art.ITEMTYPE));
                        _db.Articles.importSingleFromJSON(jsonObject);
                    }
                    else if(itemtype.equals("e"))
                    {
                        MyLog.v("Creating Event ID: " + jsonObject.getInt(JsonSchemas.Event.EVENT_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.Event.ITEMTYPE));
                        _db.Events.importSingleFromJSON(jsonObject);
                    }
                    else if(itemtype.equals("pm"))
                    {
                        MyLog.v("Creating PushMessage ID: " + jsonObject.getInt(JsonSchemas.Push.PUSHMESSAGE_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.Push.ITEMTYPE));
                        _db.PushMessages.importSingleFromJSON(jsonObject);
                    }
                    else if(itemtype.equals("pmg"))
                    {
                        MyLog.v("Creating PushMessageGroup ID: " + jsonObject.getInt(JsonSchemas.PushGroup.GROUP_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.PushGroup.ITEMTYPE));
                        _db.PushMessageGroups.importSingleFromJSON(jsonObject);
                    }
                    else if(itemtype.equals("s"))
                    {
                        MyLog.v("Creating Session ID: " + jsonObject.getInt(JsonSchemas.Ses.SESSION_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.Ses.ITEMTYPE));
                        _db.Sessions.importSingleFromJSON(jsonObject);
                    }
                    else if(itemtype.equals("v"))
                    {
                        MyLog.v("Creating Venue ID: " + jsonObject.getInt(JsonSchemas.Venue.VENUE_ID) + " Rowtype: " + jsonObject.getString(JsonSchemas.Venue.ITEMTYPE));
                        _db.Venues.importSingleFromJSON(jsonObject);
                    }
                    else{
                        MyLog.e("Unidentified rowtype received");
                    }
                }
            }

        } catch (JSONException e) {
            MyLog.e("JSONException in Handler_DumpToDatabase:onPostExecute", e);
        }

        return "";
    }

    protected void onPostExecute(String result){
        MyLog.d("Serverdump transfered to Database");
        _app.setDatabaseDataVersion(newVersion);
        _delegate.returnFromDumpToDatabase();
    }
}
