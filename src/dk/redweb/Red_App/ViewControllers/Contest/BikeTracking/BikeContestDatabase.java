package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import dk.redweb.Red_App.Database.DbSchemas;
import dk.redweb.Red_App.MyLog;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/12/13
 * Time: 13:39
 */
public class BikeContestDatabase extends SQLiteOpenHelper{
    private static String[] ALL_COLUMNS = {DbSchemas.Bike.LATITUDE, DbSchemas.Bike.LONGITUDE, DbSchemas.Bike.TIME};
    public static final String DATABASE_NAME = "RedApp.db";

    public BikeContestDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addLocation(Location location) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbSchemas.Bike.LATITUDE, location.getLatitude());
        values.put(DbSchemas.Bike.LONGITUDE, location.getLongitude());
        values.put(DbSchemas.Bike.TIME, location.getTime());

        db.insert(DbSchemas.Bike.TABLE_NAME, null, values);
        MyLog.v("New location with latitude " + location.getLatitude() + " longitude " + location.getLongitude() +
                " time " + new DateTime(location.getTime()).toString("HH:mm:ss") + " written to database");

        db.close();
    }
}
