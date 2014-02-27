package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import dk.redweb.hca2014.Database.Converters;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.Database.JsonSchemas;
import dk.redweb.hca2014.DatabaseModel.Session;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 2:28 PM
 */
public class DbSessions {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    DbInterface _db;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.Ses.SESSION_ID, DbSchemas.Ses.EVENT_ID, DbSchemas.Ses.VENUE_ID,
            DbSchemas.Ses.TITLE, DbSchemas.Ses.DETAILS, DbSchemas.Ses.STARTDATETIME, DbSchemas.Ses.ENDDATETIME, DbSchemas.Ses.EVENTTYPE};

    public DbSessions(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public SessionVM[] getAllVM() {
        Cursor c = _sql.query(true, DbSchemas.Ses.TABLE_NAME, ALL_COLUMNS,
                null, null, null, null, DbSchemas.Ses.TITLE, null);

        SessionVM[] sessions = new SessionVM[c.getCount()];

        int i = 0;
        while(c.moveToNext()){
            sessions[i] = new SessionVM(MakeSessionFromCursor(c));
            i++;
        }

        c.close();

        return sessions;
    }

    public SessionVM[] getNextThreeVM(DateTime currentDateTime) {
        String chosenDay = "'" + Converters.JodaDateTimeToSQLDateTime(currentDateTime) + "'";
        String whereString = "datetime(" + DbSchemas.Ses.STARTDATETIME + ") >= date(" + chosenDay + ")";

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, DbSchemas.Ses.STARTDATETIME, "3");

        SessionVM[] sessions = new SessionVM[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            sessions[i] = new SessionVM(MakeSessionFromCursor(c));
            i++;
        }

        if(c.getCount() == 0)
        {
            MyLog.d("DbSessions.getVMListFromDayAndVenueId(" +
                    currentDateTime.getDayOfMonth() + "/" + currentDateTime.getMonthOfYear() + "/" + currentDateTime.getYear() + "): No sessions selected!");
        }

        c.close();

        return sessions;
    }

    public SessionVM[] getVMListFromDayAndVenueId(LocalDate day, int venueId, String type) {
        String chosenDay = "'" + Converters.LocalDateToSQLDate(day) + "'";
        String dateWhereString = "date(" + DbSchemas.Ses.STARTDATETIME + ") = date(" + chosenDay + ")";
        String venueWhereString = "";
        if(venueId >= 0) venueWhereString = " AND " + DbSchemas.Ses.VENUE_ID + " = '" + venueId + "'";
        String typeWhereString = "";
        if(type != null) typeWhereString = " AND " + DbSchemas.Ses.EVENTTYPE + " = '" + type + "'";
        String whereString = dateWhereString + venueWhereString + typeWhereString;

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, DbSchemas.Ses.STARTDATETIME);

        SessionVM[] sessions = new SessionVM[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            sessions[i] = new SessionVM(MakeSessionFromCursor(c));
            i++;
        }

        if(c.getCount() == 0)
        {
            MyLog.d("DbSessions.getVMListFromDayAndVenueId(" +
                    day.getDayOfMonth() + "/" + day.getMonthOfYear() + "/" + day.getYear() + ", " +
                    venueId + "): No sessions selected!");
        }

        c.close();

        return sessions;
    }

    public LocalDate getDateForNextFromDateAndVenueId(LocalDate date, int venueId) {

        String dateString = "'" + Converters.LocalDateToSQLDate(date) + "'";
        String dateWhereString = "date(" + DbSchemas.Ses.STARTDATETIME + ") > date(" + dateString + ")";

        String venueWhereString = "";
        if(venueId >= 0) venueWhereString = " AND " + DbSchemas.Ses.VENUE_ID + " = '" + venueId + "'";

        String whereString = dateWhereString + venueWhereString;
        String sortString = DbSchemas.Ses.STARTDATETIME;

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, new String[] {DbSchemas.Ses.STARTDATETIME}, whereString, null, null, null, sortString, "1");

        c.moveToFirst();

        if(c.getCount() == 0){
            return null;
        }

        LocalDate nextDate = Converters.SQLDateTimeToLocalDate(c.getString(0));

        return nextDate;
    }

    public LocalDate getDateForLastFromDateAndVenueId(LocalDate date, int venueId) {

        String dateTimeString = "'" + Converters.LocalDateToSQLDate(date) + "'";
        String dateWhereString = "datetime(" + DbSchemas.Ses.STARTDATETIME + ") < datetime(" + dateTimeString + ")";

        String venueWhereString = "";
        if(venueId >= 0) venueWhereString = " AND " + DbSchemas.Ses.VENUE_ID + " = '" + venueId + "'";

        String whereString = dateWhereString + venueWhereString;
        String sortString = DbSchemas.Ses.STARTDATETIME + " DESC";

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, new String[] {DbSchemas.Ses.STARTDATETIME}, whereString, null, null, null, sortString, "1");

        c.moveToFirst();

        return Converters.SQLDateTimeToLocalDate(c.getString(0));
    }

    public LocalDate getDateForEarliestFromVenueId(int venueId) {
        String whereString = "";
        if(venueId >= 0){
            whereString = " WHERE " + DbSchemas.Ses.VENUE_ID + " = " + venueId;
        }

        final SQLiteStatement statement = _sql.compileStatement("SELECT MIN(datetime(" + DbSchemas.Ses.STARTDATETIME + ")) " +
                "FROM " + DbSchemas.Ses.TABLE_NAME + whereString);

        String SQLDate = statement.simpleQueryForString();
        if(SQLDate == null) return null;

        return Converters.SQLDateTimeToLocalDate(SQLDate);
    }

    public LocalDate getDateForLatestFromVenueId(int venueId) {
        String whereString = "";
        if(venueId >= 0){
            whereString = " WHERE " + DbSchemas.Ses.VENUE_ID + " = " + venueId;
        }

        final SQLiteStatement statement = _sql.compileStatement("SELECT MAX(datetime(" + DbSchemas.Ses.STARTDATETIME + ")) " +
                "FROM " + DbSchemas.Ses.TABLE_NAME + whereString);

        String SQLDate = statement.simpleQueryForString();
        if(SQLDate == null) return null;

        return Converters.SQLDateTimeToLocalDate(SQLDate);
    }

    public Session getFromId(int sessionid) {
        String whereString = DbSchemas.Ses.SESSION_ID + " = " + sessionid;

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null, "1");

        c.moveToFirst();

        return MakeSessionFromCursor(c);
    }

    public SessionVM getVMFromId(int sessionid) {
        return new SessionVM(getFromId(sessionid));
    }

    public boolean isDateSessionless(LocalDate day, int venueId) {
        String chosenDay = "'" + Converters.LocalDateToSQLDate(day) + "'";
        String dateWhereString = "date(" + DbSchemas.Ses.STARTDATETIME + ") = date(" + chosenDay + ")";
        String venueWhereString = "";
        if(venueId >= 0) venueWhereString = " AND " + DbSchemas.Ses.VENUE_ID + " = '" + venueId + "'";
        String whereString = dateWhereString + venueWhereString;

        Cursor c = _sql.query(DbSchemas.Ses.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, DbSchemas.Ses.STARTDATETIME);

        return c.getCount() <= 0;
    }

    public int[] activeVenueIds(){
        Cursor c = _sql.rawQuery("select DISTINCT " + DbSchemas.Ses.VENUE_ID + " from " + DbSchemas.Ses.TABLE_NAME, null);

        int[] ids = new int[c.getCount()];
        int i = 0;
        while(c.moveToNext())
        {
            ids[i] = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Ses.VENUE_ID));
            i++;
        }
        return ids;
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int sessionId = Integer.parseInt(jsonObject.getString(JsonSchemas.Ses.SESSION_ID));
        values.put(DbSchemas.Ses.SESSION_ID, sessionId);
        int eventId = Integer.parseInt(jsonObject.getString(JsonSchemas.Ses.EVENT_ID));
        values.put(DbSchemas.Ses.EVENT_ID, eventId);
        int venueId = Integer.parseInt(jsonObject.getString(JsonSchemas.Ses.VENUE_ID));
        values.put(DbSchemas.Ses.VENUE_ID, venueId);
        values.put(DbSchemas.Ses.TITLE, jsonObject.getString(JsonSchemas.Ses.TITLE));
        values.put(DbSchemas.Ses.DETAILS, jsonObject.getString(JsonSchemas.Ses.DETAILS));
        String startDate = jsonObject.getString(JsonSchemas.Ses.STARTDATE);
        String startTime = jsonObject.getString(JsonSchemas.Ses.STARTTIME);
        if(startTime.equals("null"))
            startTime = "01:00:00";
        String startDateTime = startDate + " " + startTime;
        values.put(DbSchemas.Ses.STARTDATETIME, startDateTime);
        String endDate = jsonObject.getString(JsonSchemas.Ses.ENDDATE);
        String endTime = jsonObject.getString(JsonSchemas.Ses.ENDTIME);
        if(endTime.equals("null"))
            endTime = "23:59:59";
        String endDateTime = endDate + " " + endTime;
        values.put(DbSchemas.Ses.ENDDATETIME, endDateTime);
        values.put(DbSchemas.Ses.EVENTTYPE, jsonObject.getString(JsonSchemas.Ses.EVENTTYPE));


        String sesExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.Ses.TABLE_NAME +
                " WHERE " + DbSchemas.Ses.SESSION_ID + " = " + sessionId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(sesExistsQuery, null);
        c.moveToFirst();
        Boolean sessionExists = c.getInt(0) > 0;

        if(sessionExists){
            String whereString = DbSchemas.Ses.SESSION_ID + " = " + sessionId;
            _sql.update(DbSchemas.Ses.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated session data with id:" + sessionId + " written to database");
        } else {
            _sql.insert(DbSchemas.Ses.TABLE_NAME, null, values);
            MyLog.v("New session with id:" + sessionId + " written to database");
        }
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.Ses.SESSION_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.Ses.SESSION_ID));
        _sql.delete(DbSchemas.Ses.TABLE_NAME, whereString, null);
    }

    public Session MakeSessionFromCursor(Cursor c)
    {
        Session newSession = new Session(_db);
        try{
            newSession.SessionId =  c.getInt(c.getColumnIndexOrThrow(DbSchemas.Ses.SESSION_ID));
            newSession.EventId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Ses.EVENT_ID));
            newSession.VenueId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Ses.VENUE_ID));
            newSession.Title = c.getString(c.getColumnIndexOrThrow(DbSchemas.Ses.TITLE));
            newSession.Details = c.getString(c.getColumnIndexOrThrow(DbSchemas.Ses.DETAILS));

            String startDateTime = c.getString(c.getColumnIndexOrThrow(DbSchemas.Ses.STARTDATETIME));
            String[] splitStartDateTime = startDateTime.split(" ");
            String startDate = splitStartDateTime[0];
            String startTime = splitStartDateTime[1];

            newSession.StartDate = Converters.SQLDateToLocalDate(startDate);
            newSession.StartTime = Converters.SQLTimeToLocalTime(startTime);

            String endDateTime = c.getString(c.getColumnIndexOrThrow(DbSchemas.Ses.ENDDATETIME));
            String[] splitEndDateTime = endDateTime.split(" ");
            String endDate = splitEndDateTime[0];
            String endTime = splitEndDateTime[1];

            newSession.EndDate = Converters.SQLDateToLocalDate(endDate);
            newSession.EndTime = Converters.SQLTimeToLocalTime(endTime);

            newSession.Type = c.getString(c.getColumnIndexOrThrow(DbSchemas.Ses.EVENTTYPE));
        }
        catch (Exception e)
        {
            MyLog.e("Error", e);
        }
        return newSession;
    }
}
