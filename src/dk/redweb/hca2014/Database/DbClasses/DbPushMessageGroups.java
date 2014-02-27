package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.hca2014.Database.Converters;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.Database.JsonSchemas;
import dk.redweb.hca2014.DatabaseModel.PushMessageGroup;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.ViewModels.PushMessageGroupVM;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/23/13
 * Time: 16:34
 */
public class DbPushMessageGroups {
    
    RedEventApplication _app;
    SQLiteDatabase _sql;
    DbInterface _db;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.PushGroup.GROUP_ID, DbSchemas.PushGroup.NAME, DbSchemas.PushGroup.SUBSCRIBED};
    
    public DbPushMessageGroups(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml) {
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public PushMessageGroup[] getAll(){
        String sortString = DbSchemas.PushGroup.NAME + " ASC";

        Cursor c = _sql.query(DbSchemas.PushGroup.TABLE_NAME, ALL_COLUMNS, null, null, null, null, sortString);

        PushMessageGroup[] articles = new PushMessageGroup[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakePushMessageGroupFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbPushMessageGroups:getAll(): No pushmessagegroups were received from database when trying to get all pushmessagegroups");
        }

        c.close();

        return articles;
    }

    public PushMessageGroupVM[] getAllVMs() {
        PushMessageGroup[] groups = getAll();
        PushMessageGroupVM[] groupVMs = new PushMessageGroupVM[groups.length];
        for (int i = 0; i < groups.length; i++){
            groupVMs[i] = new PushMessageGroupVM(groups[i]);
        }
        return groupVMs;
    }

    public int[] getSubscribedGroupIds(){
        String whereString = DbSchemas.PushGroup.SUBSCRIBED + " = " + Converters.BooleanToInteger(true);

        Cursor c = _sql.query(DbSchemas.PushGroup.TABLE_NAME, new String[]{DbSchemas.PushGroup.GROUP_ID}, whereString, null, null, null, null);

        int[] subscribedGroupIds = new int[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            subscribedGroupIds[i] = c.getInt(0);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbPushMessageGroups:getSubscribedGroupIds(): No subscribed pushmessagegroups were received from database");
        }

        c.close();

        return subscribedGroupIds;
    }

    public boolean isSubscribedByGroupId(int groupId){
        String whereString = DbSchemas.PushGroup.GROUP_ID + " = " + groupId;

        Cursor c = _sql.query(DbSchemas.PushGroup.TABLE_NAME, new String[]{DbSchemas.PushGroup.SUBSCRIBED}, whereString, null, null, null, null);

        c.moveToFirst();
        int intValue = c.getInt(0);;
        c.close();

        boolean subscribed = (intValue == 1)? true : false;
        return subscribed;
    }

    public void updateSubscriptionByGroupId(int groupId, boolean subscribed){
        String whereString = DbSchemas.PushGroup.GROUP_ID + " = " + groupId;

        ContentValues values = new ContentValues();
        values.put(DbSchemas.PushGroup.SUBSCRIBED, subscribed);
        _sql.update(DbSchemas.PushGroup.TABLE_NAME, values, whereString, null);
    }

    public void unsubscribeAll() {
        ContentValues values = new ContentValues();
        values.put(DbSchemas.PushGroup.SUBSCRIBED, false);
        _sql.update(DbSchemas.PushGroup.TABLE_NAME, values, null, null);
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int groupId = Integer.parseInt(jsonObject.getString(JsonSchemas.PushGroup.GROUP_ID));
        values.put(DbSchemas.PushGroup.GROUP_ID, groupId);
        values.put(DbSchemas.PushGroup.NAME, jsonObject.getString(JsonSchemas.PushGroup.NAME));

        String groupExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.PushGroup.TABLE_NAME +
                " WHERE " + DbSchemas.PushGroup.GROUP_ID + " = " + groupId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(groupExistsQuery, null);
        c.moveToFirst();
        Boolean messageExists = c.getInt(0) > 0;

        if(messageExists){
            String whereString = DbSchemas.PushGroup.GROUP_ID + " = " + groupId;
            _sql.update(DbSchemas.PushGroup.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated pushmessagegroup data for id:" + groupId + " written to database");
        } else {
            values.put(DbSchemas.PushGroup.SUBSCRIBED, false);
            _sql.insert(DbSchemas.PushGroup.TABLE_NAME, null, values);
            MyLog.v("New pushmessagegroup with id:" + groupId + " written to database");
        }
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.PushGroup.GROUP_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.PushGroup.GROUP_ID));
        _sql.delete(DbSchemas.PushGroup.TABLE_NAME, whereString, null);
    }

    public PushMessageGroup MakePushMessageGroupFromCursor(Cursor c)
    {
        PushMessageGroup newPushMessageGroup = new PushMessageGroup();
        try{
            newPushMessageGroup.GroupId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.PushGroup.GROUP_ID));
            newPushMessageGroup.Name = c.getString(c.getColumnIndexOrThrow(DbSchemas.PushGroup.NAME));
            newPushMessageGroup.Subscribing = Converters.IntegerToBoolean(c.getInt(c.getColumnIndexOrThrow(DbSchemas.PushGroup.SUBSCRIBED)));
        }
        catch (Exception e)
        {
            MyLog.e("Exception when inserting cursor data into new PushMessageGroup object", e);
        }
        return newPushMessageGroup;
    }
}
