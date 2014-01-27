package dk.redweb.Red_App.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Database.DbSchemas;
import dk.redweb.Red_App.Database.JsonSchemas;
import dk.redweb.Red_App.DatabaseModel.Event;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 2:28 PM
 */
public class DbEvents {
    RedEventApplication _app;
    DbInterface _db;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.Event.EVENT_ID, DbSchemas.Event.TITLE, DbSchemas.Event.SUMMARY,
            DbSchemas.Event.DETAILS, DbSchemas.Event.SUBMISSION, DbSchemas.Event.IMAGEPATH};

    public DbEvents(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public Event getFromId(int eventid) {
        String whereString = DbSchemas.Event.EVENT_ID + " = " + eventid;

        Cursor c = _sql.query(DbSchemas.Event.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null, "1");

        c.moveToFirst();

        return MakeEventFromCursor(c);
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int eventId = Integer.parseInt(jsonObject.getString(JsonSchemas.Event.EVENT_ID));
        values.put(DbSchemas.Event.EVENT_ID, eventId);
        values.put(DbSchemas.Event.TITLE, jsonObject.getString(JsonSchemas.Event.TITLE));
        values.put(DbSchemas.Event.SUMMARY, jsonObject.getString(JsonSchemas.Event.SUMMARY));
        values.put(DbSchemas.Event.DETAILS, jsonObject.getString(JsonSchemas.Event.DETAILS));
        values.put(DbSchemas.Event.SUBMISSION, jsonObject.getString(JsonSchemas.Event.SUBMISSION));

        String imagePath = jsonObject.getString(JsonSchemas.Event.IMAGEPATH);
        if (imagePath != "" && !imagePath.startsWith(_xml.joomlaPath)) {
            imagePath = _xml.joomlaPath + imagePath;
        }
        values.put(DbSchemas.Event.IMAGEPATH, imagePath);


        String eventExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.Event.TABLE_NAME +
                " WHERE " + DbSchemas.Event.EVENT_ID + " = " + eventId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(eventExistsQuery, null);
        c.moveToFirst();
        Boolean eventExists = c.getInt(0) > 0;

        if(eventExists){
            String whereString = DbSchemas.Event.EVENT_ID + " = " + eventId;
            _sql.update(DbSchemas.Event.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated event data for id:" + eventId + " written to database");
        } else {
            _sql.insert(DbSchemas.Event.TABLE_NAME, null, values);
            MyLog.v("New event with id:" + eventId + " written to database");
        }
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.Event.EVENT_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.Event.EVENT_ID));
        _sql.delete(DbSchemas.Event.TABLE_NAME, whereString, null);
    }

    public Event MakeEventFromCursor(Cursor c)
    {
        Event newEvent = new Event(_db);
        try{
            newEvent.EventId =  c.getInt(c.getColumnIndexOrThrow(DbSchemas.Event.EVENT_ID));
            newEvent.Title = c.getString(c.getColumnIndexOrThrow(DbSchemas.Event.TITLE));
            newEvent.Summary = c.getString(c.getColumnIndexOrThrow(DbSchemas.Event.SUMMARY));
            newEvent.Details = c.getString(c.getColumnIndexOrThrow(DbSchemas.Event.DETAILS));
            newEvent.Submission = c.getString(c.getColumnIndexOrThrow(DbSchemas.Event.SUBMISSION));
            newEvent.Imagepath = c.getString(c.getColumnIndexOrThrow(DbSchemas.Event.IMAGEPATH));
        }
        catch (Exception e)
        {
            MyLog.e("Error", e);
        }
        return newEvent;
    }
}
