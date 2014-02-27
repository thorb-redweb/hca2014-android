package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/12/13
 * Time: 12:56
 */
public class DbBikeContest {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private static String[] ALL_COLUMNS = {DbSchemas.Bike.LATITUDE, DbSchemas.Bike.LONGITUDE, DbSchemas.Bike.TIME};

    public DbBikeContest(RedEventApplication app, SQLiteDatabase sql, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _sv = sv;
        _xml = xml;
    }

    public Location[] getAll(){
        Cursor c = _sql.query(DbSchemas.Bike.TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);

        Location[] locations = new Location[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            Location location = new Location("location");
            location.setLatitude(c.getFloat(c.getColumnIndexOrThrow("latitude")));
            location.setLongitude(c.getFloat(c.getColumnIndexOrThrow("longitude")));
            location.setTime(c.getLong(c.getColumnIndexOrThrow("time")));
            locations[i] = location;
            i++;
        }

        return locations;
    }

    public void dropAllEntries(){
        _sql.delete(DbSchemas.Bike.TABLE_NAME,null,null);
    }

    public void addLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put(DbSchemas.Bike.LATITUDE, location.getLatitude());
        values.put(DbSchemas.Bike.LONGITUDE, location.getLongitude());
        values.put(DbSchemas.Bike.TIME, location.getTime());

        _sql.insert(DbSchemas.Bike.TABLE_NAME, null, values);
        MyLog.v("New location with latitude " + location.getLatitude() + " longitude " + location.getLongitude() +
                " time " + new DateTime(location.getTime()).toString("HH:mm:ss") + " written to database");
    }
}
