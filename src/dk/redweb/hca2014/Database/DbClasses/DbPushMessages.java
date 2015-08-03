package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.hca2014.Database.Converters;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.Database.JsonSchemas;
import dk.redweb.hca2014.DatabaseModel.PushMessage;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.ViewModels.PushMessageVM;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/22/13
 * Time: 15:36
 */
public class DbPushMessages {
    RedEventApplication _app;
    DbInterface _db;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.Push.PUSHMESSAGE_ID, DbSchemas.Push.GROUP_ID, DbSchemas.Push.INTRO, DbSchemas.Push.MESSAGE, DbSchemas.Push.SENDDATE, DbSchemas.Push.AUTHOR};

    public DbPushMessages(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public PushMessage getFromId(int messageid) {
        String whereString = DbSchemas.Push.PUSHMESSAGE_ID + " = " + messageid;

        Cursor c = _sql.query(DbSchemas.Push.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null, "1");

        c.moveToFirst();

        PushMessage pm = MakePushMessageFromCursor(c);

        c.close();

        return pm;
    }

    public PushMessageVM getVMFromId(int messageid){
        PushMessage pushMessage = getFromId(messageid);
        return new PushMessageVM(pushMessage);
    }

    public PushMessageVM[] getAllVM() {
        String sortString = DbSchemas.Push.MESSAGE + " DESC";

        Cursor c = _sql.query(DbSchemas.Push.TABLE_NAME, ALL_COLUMNS, null, null, null, null, sortString);

        PushMessageVM[] pms = new PushMessageVM[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            pms[i] = new PushMessageVM(MakePushMessageFromCursor(c));
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbPushMessages:getListFromGroupId: No articles received");
        }

        c.close();

        return pms;
    }

    public PushMessage[] getListFromGroupId(int groupId){
        String whereString = DbSchemas.Push.GROUP_ID + " = " + groupId;
        String sortString = DbSchemas.Push.SENDDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Push.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, sortString);

        PushMessage[] articles = new PushMessage[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakePushMessageFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbPushMessages:getListFromGroupId: No articles received");
        }

        c.close();

        return articles;
    }

    public PushMessageVM[] getVMListFromGroupId(int groupId) {
        PushMessage[] pushMessages = getListFromGroupId(groupId);
        PushMessageVM[] pushMessageVMs = new PushMessageVM[pushMessages.length];
        for (int i = 0; i < pushMessages.length; i++){
            pushMessageVMs[i] = new PushMessageVM(pushMessages[i]);
        }
        return pushMessageVMs;
    }

    public PushMessage[] getListFromGroupIds(int[] groupIds){
        String whereString = "";
        for(int groupId : groupIds) {
            whereString += DbSchemas.Push.GROUP_ID + " = " + groupId + " OR ";
        }
        whereString = whereString.substring(0,whereString.length()-3);

        String sortString = DbSchemas.Push.SENDDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Push.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, sortString);

        PushMessage[] articles = new PushMessage[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakePushMessageFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbPushMessages:getListFromGroupIds: No articles received");
        }

        c.close();

        return articles;
    }

    public PushMessageVM[] getVMListFromGroupIds(int[] groupIds) {
        PushMessage[] pushMessages = getListFromGroupIds(groupIds);
        PushMessageVM[] pushMessageVMs = new PushMessageVM[pushMessages.length];
        for (int i = 0; i < pushMessages.length; i++){
            pushMessageVMs[i] = new PushMessageVM(pushMessages[i]);
        }
        return pushMessageVMs;
    }

    public PushMessageVM[] getVMListFromSubscribedGroups() {
        int[] subscribedGroups = _db.PushMessageGroups.getSubscribedGroupIds();
        PushMessageVM[] pushMessageVMs = new PushMessageVM[0];
        if(subscribedGroups.length > 0){
            pushMessageVMs = getVMListFromGroupIds(subscribedGroups);
        }
        return pushMessageVMs;
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int pushMessageId = Integer.parseInt(jsonObject.getString(JsonSchemas.Push.PUSHMESSAGE_ID));
        values.put(DbSchemas.Push.PUSHMESSAGE_ID, pushMessageId);
        int groupId = Integer.parseInt(jsonObject.getString(JsonSchemas.Push.GROUP_ID));
        values.put(DbSchemas.Push.GROUP_ID, groupId);
        values.put(DbSchemas.Push.INTRO, jsonObject.getString(JsonSchemas.Push.INTRO));
        values.put(DbSchemas.Push.MESSAGE, jsonObject.getString(JsonSchemas.Push.MESSAGE));
        values.put(DbSchemas.Push.SENDDATE, jsonObject.getString(JsonSchemas.Push.SENDDATE));
        values.put(DbSchemas.Push.AUTHOR, jsonObject.getString(JsonSchemas.Push.AUTHOR));

        String pushmessageExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.Push.TABLE_NAME +
                " WHERE " + DbSchemas.Push.PUSHMESSAGE_ID + " = " + pushMessageId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(pushmessageExistsQuery, null);
        c.moveToFirst();
        Boolean messageExists = c.getInt(0) > 0;

        if(messageExists){
            String whereString = DbSchemas.Push.PUSHMESSAGE_ID + " = " + pushMessageId;
            _sql.update(DbSchemas.Push.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated pushmessage data for id:" + pushMessageId + " written to database");
        } else {
            _sql.insert(DbSchemas.Push.TABLE_NAME, null, values);
            MyLog.v("New pushmessage with id:" + pushMessageId + " written to database");
        }

        c.close();
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.Push.PUSHMESSAGE_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.Push.PUSHMESSAGE_ID));
        _sql.delete(DbSchemas.Push.TABLE_NAME, whereString, null);
    }

    public PushMessage MakePushMessageFromCursor(Cursor c)
    {
        PushMessage newPushMessage = new PushMessage();
        try{
            newPushMessage.PushMessageId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Push.PUSHMESSAGE_ID));
            newPushMessage.GroupId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Push.GROUP_ID));
            newPushMessage.Intro = c.getString(c.getColumnIndexOrThrow(DbSchemas.Push.INTRO));
            newPushMessage.Message = c.getString(c.getColumnIndexOrThrow(DbSchemas.Push.MESSAGE));
            newPushMessage.Author = c.getString(c.getColumnIndexOrThrow(DbSchemas.Push.AUTHOR));
            newPushMessage.SendDate = Converters.SQLDateTimeToJodaDateTime(c.getString(c.getColumnIndexOrThrow(DbSchemas.Push.SENDDATE)));
        }
        catch (Exception e)
        {
            MyLog.e("Exception when inserting cursor data into new PushMessage object", e);
        }
        return newPushMessage;
    }
}
