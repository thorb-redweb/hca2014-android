package dk.redweb.hca2014.Database;

import android.os.AsyncTask;
import dk.redweb.hca2014.Interfaces.Delegate_updateToDatabase;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 12:15 PM
 */
public class Handler_UpdateToDatabase extends AsyncTask<String, Void, String> {

    private final RedEventApplication _app;
    private final DbInterface _db;
    private final Delegate_updateToDatabase _delegate;
    private int newVersion;

    public Handler_UpdateToDatabase(RedEventApplication app, DbInterface db, Delegate_updateToDatabase delegate){
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
                MyLog.v("Database load was halted due to Error");
                return "Error: En fejl skete under kontakt med databasen. Prøv at starte appen igen.";
            }
            else
            {
                MyLog.d("Starting database load from GetJSONDatabase.onPostExecute...");
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String itemtype = jsonObject.getString("itemtype");
                    if(itemtype.equals("sys"))
                    {
                        int newVersion = jsonObject.getInt("version");
                        MyLog.v("LatestDatabaseVersion will be updated to " + newVersion);
                        this.newVersion = newVersion;
                    }
                    else
                    {
                        String actiontype = jsonObject.getString("actiontype");
                        if(actiontype.equals("c")){
                            createOrUpdateItem(jsonObject);
                        }
                        else if(actiontype.equals("d")){
                            deleteItem(jsonObject);
                        }
                        else{
                            MyLog.w("Unidentified actiontype received");
                        }
                    }
                }
            }

        } catch (JSONException e) {
            MyLog.e("JSONException when attempting to update database", e);
            return "Error: " + e.getMessage();
        }

        return "";
    }

    private void createOrUpdateItem(JSONObject jsonObject) throws JSONException {
        String itemtype = jsonObject.getString("itemtype");
        if(itemtype.equals("a"))
        {
            LocalDate startOfYear2014 = new LocalDate(2014,1,1);
            if(Converters.SQLDateTimeToLocalDate(jsonObject.getString(JsonSchemas.Art.PUBLISHDATE)).isAfter(startOfYear2014) ||
                    jsonObject.getInt(JsonSchemas.Art.ARTICLE_ID) == 1){
                MyLog.v("Creating/updating Article ID: " + jsonObject.getInt(JsonSchemas.Art.ARTICLE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Art.ITEMTYPE));
                _db.Articles.importSingleFromJSON(jsonObject);
            }
            else{
                MyLog.v("Skipping Article ID: " + jsonObject.getInt(JsonSchemas.Art.ARTICLE_ID) + " because it is too old");
            }
        }
        else if(itemtype.equals("e"))
        {
            MyLog.v("Creating/updating Event ID: " + jsonObject.getInt(JsonSchemas.Event.EVENT_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Event.ITEMTYPE));
            _db.Events.importSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("pm"))
        {
            MyLog.v("Creating/updating PushMessage ID: " + jsonObject.getInt(JsonSchemas.Push.PUSHMESSAGE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Push.ITEMTYPE));
            _db.PushMessages.importSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("pmg"))
        {
            MyLog.v("Creating/updating PushMessageGroup ID: " + jsonObject.getInt(JsonSchemas.PushGroup.GROUP_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.PushGroup.ITEMTYPE));
            _db.PushMessageGroups.importSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("s"))
        {
            MyLog.v("Creating/updating Session ID: " + jsonObject.getInt(JsonSchemas.Ses.SESSION_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Ses.ITEMTYPE));
            _db.Sessions.importSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("v"))
        {
            MyLog.v("Creating/updating Venue ID: " + jsonObject.getInt(JsonSchemas.Venue.VENUE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Venue.ITEMTYPE));
            _db.Venues.importSingleFromJSON(jsonObject);
        }
        else{
            MyLog.e("Unidentified itemtype received");
        }
    }

    private void deleteItem(JSONObject jsonObject) throws JSONException {
        String itemtype = jsonObject.getString("itemtype");
        if(itemtype.equals("a"))
        {
            MyLog.v("Deleting Article ID: " + jsonObject.getInt(JsonSchemas.Art.ARTICLE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Art.ITEMTYPE));
            _db.Articles.deleteSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("e"))
        {
            MyLog.v("Deleting Event ID: " + jsonObject.getInt(JsonSchemas.Event.EVENT_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Event.ITEMTYPE));
            _db.Events.deleteSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("pm"))
        {
            MyLog.v("Deleting PushMessage ID: " + jsonObject.getInt(JsonSchemas.Push.PUSHMESSAGE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Push.PUSHMESSAGE_ID));
            _db.PushMessages.deleteSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("pmg"))
        {
            MyLog.v("Deleting PushMessageGroup ID: " + jsonObject.getInt(JsonSchemas.PushGroup.GROUP_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.PushGroup.GROUP_ID));
            _db.PushMessageGroups.deleteSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("s"))
        {
            MyLog.v("Deleting Session ID: " + jsonObject.getInt(JsonSchemas.Ses.SESSION_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Ses.ITEMTYPE));
            _db.Sessions.deleteSingleFromJSON(jsonObject);
        }
        else if(itemtype.equals("v"))
        {
            MyLog.v("Deleting Venue ID: " + jsonObject.getInt(JsonSchemas.Venue.VENUE_ID) + " Itemtype: " + jsonObject.getString(JsonSchemas.Venue.ITEMTYPE));
            _db.Venues.deleteSingleFromJSON(jsonObject);
        }
        else{
            MyLog.e("Unidentified itemtype received");
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            _delegate.errorOccured(result);
            return;
        }
        MyLog.d("Serverdump transfered to Database");
        _app.setDatabaseDataVersion(newVersion);
        MyLog.v("Databaseversion updated to " + newVersion);
        _delegate.returnFromUpdateToDatabase();
    }
}
