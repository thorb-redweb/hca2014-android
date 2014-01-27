package dk.redweb.Red_App.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Database.DbSchemas;
import dk.redweb.Red_App.Database.JsonSchemas;
import dk.redweb.Red_App.DatabaseModel.Venue;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.ViewModels.VenueVM;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:47 AM
 */
public class DbVenues {
    RedEventApplication _app;
    DbInterface _db;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.Venue.VENUE_ID, DbSchemas.Venue.TITLE, DbSchemas.Venue.DESCRIPTION,
            DbSchemas.Venue.STREET, DbSchemas.Venue.CITY, DbSchemas.Venue.LATITUDE, DbSchemas.Venue.LONGITUDE,
            DbSchemas.Venue.IMAGEPATH};

    public DbVenues(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public String[] getAllNames() {
        Cursor c = _sql.query(true, DbSchemas.Venue.TABLE_NAME, new String[]{DbSchemas.Venue.TITLE},
                null, null, null, null, DbSchemas.Venue.TITLE, null);

        String[] venues = new String[c.getCount()];

        int i = 0;
        while(c.moveToNext()){
            venues[i] = c.getString(0);
            i++;
        }

        c.close();

        return venues;
    }

    public Venue getFromId(int venueid) {
        String whereString = DbSchemas.Venue.VENUE_ID + " = " + venueid;
        Cursor c = _sql.query(DbSchemas.Venue.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null);

        Venue selectedVenue = new Venue(_db);

        if (c.moveToNext())
        {
            selectedVenue = MakeVenueFromCursor(c);
        }
        else{
            MyLog.d("DbVenues.getFromTitle: No venue selected!");
        }

        c.close();

        return selectedVenue;
    }

    public VenueVM getVMFromId(int venueId) {
        Venue venue = getFromId(venueId);
        return new VenueVM(venue);
    }

    public Venue getFromName(String venueTitle) {
        String whereString = DbSchemas.Venue.TITLE + " = '" + venueTitle + "'";
        Cursor c = _sql.query(DbSchemas.Venue.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null);

        Venue selectedVenue = new Venue(_db);

        if (c.moveToNext())
        {
            selectedVenue = MakeVenueFromCursor(c);
        }
        else{
            MyLog.d("DbVenues.getFromTitle: No venue selected!");
        }

        c.close();

        return selectedVenue;
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int venueId = Integer.parseInt(jsonObject.getString(JsonSchemas.Venue.VENUE_ID));
        values.put(DbSchemas.Venue.VENUE_ID, venueId);
        values.put(DbSchemas.Venue.TITLE, jsonObject.getString(JsonSchemas.Venue.TITLE));
        values.put(DbSchemas.Venue.DESCRIPTION, jsonObject.getString(JsonSchemas.Venue.DESCRIPTION));
        values.put(DbSchemas.Venue.STREET, jsonObject.getString(JsonSchemas.Venue.STREET));
        values.put(DbSchemas.Venue.CITY, jsonObject.getString(JsonSchemas.Venue.CITY));
        double latitude = Double.parseDouble(jsonObject.getString(JsonSchemas.Venue.LATITUDE));
        values.put(DbSchemas.Venue.LATITUDE, latitude);
        double longitude = Double.parseDouble(jsonObject.getString(JsonSchemas.Venue.LONGITUDE));
        values.put(DbSchemas.Venue.LONGITUDE, longitude);

        String imagePath = jsonObject.getString(JsonSchemas.Event.IMAGEPATH);
        if (imagePath != "" && !imagePath.startsWith(_xml.joomlaPath)) {
            imagePath = _xml.joomlaPath + imagePath;
        }
        values.put(DbSchemas.Venue.IMAGEPATH, imagePath);

        String venueExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.Venue.TABLE_NAME +
                " WHERE " + DbSchemas.Venue.VENUE_ID + " = " + venueId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(venueExistsQuery, null);
        c.moveToFirst();
        Boolean venueExists = c.getInt(0) > 0;

        if(venueExists){
            String whereString = DbSchemas.Venue.VENUE_ID + " = " + venueId;
            _sql.update(DbSchemas.Venue.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated venue data for id:" + venueId + " written to database");
        } else {
            _sql.insert(DbSchemas.Venue.TABLE_NAME, null, values);
            MyLog.v("New venue with id:" + venueId + " written to database");
        }
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.Venue.VENUE_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.Venue.VENUE_ID));
        _sql.delete(DbSchemas.Venue.TABLE_NAME, whereString, null);
    }

    public Venue MakeVenueFromCursor(Cursor c)
    {
        Venue newVenue = new Venue(_db);
        try{
            newVenue.VenueId =  c.getInt(c.getColumnIndexOrThrow(DbSchemas.Venue.VENUE_ID));
            newVenue.Title = c.getString(c.getColumnIndexOrThrow(DbSchemas.Venue.TITLE));
            newVenue.Description = c.getString(c.getColumnIndexOrThrow(DbSchemas.Venue.DESCRIPTION));
            newVenue.Street = c.getString(c.getColumnIndexOrThrow(DbSchemas.Venue.STREET));
            newVenue.City = c.getString(c.getColumnIndexOrThrow(DbSchemas.Venue.CITY));
            newVenue.Latitude = c.getDouble(c.getColumnIndexOrThrow(DbSchemas.Venue.LATITUDE));
            newVenue.Longitude = c.getDouble(c.getColumnIndexOrThrow(DbSchemas.Venue.LONGITUDE));
            newVenue.ImagePath = c.getString(c.getColumnIndexOrThrow(DbSchemas.Venue.IMAGEPATH));
        }
        catch (Exception e)
        {
            MyLog.e("Error", e);
        }
        return newVenue;
    }
}
