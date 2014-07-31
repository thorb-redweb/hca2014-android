package dk.redweb.hca2014.Database.DbClasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.maps.model.LatLng;
import dk.redweb.hca2014.Database.Converters;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.DatabaseModel.Session;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.ViewControllers.Map.MapMarker;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 3:01 PM
 */
public class DbMapMarkers {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    DbInterface _db;
    ServerInterface _sv;
    XmlStore _xml;

    public DbMapMarkers(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public MapMarker[] getAll(){

        DateTime currentDateTime = new DateTime();
        if(_app.isDebugging()){currentDateTime = _app.getDebugCurrentDate();}

        String whereString = "datetime(" + DbSchemas.Ses.STARTDATETIME + ") >= datetime('" + Converters.JodaDateTimeToSQLDateTime(currentDateTime) + "')";
        String sortString = DbSchemas.Ses.STARTDATETIME + " DESC";

        String queryString = "SELECT s.* " +
                "FROM " + DbSchemas.Ses.TABLE_NAME + " s " +
                "INNER JOIN (" +
                "SELECT " + DbSchemas.Ses.STARTDATETIME + " " +
                "FROM " + DbSchemas.Ses.TABLE_NAME + " s " +
                "WHERE " + whereString + " " +
                "ORDER BY " + sortString +
                ") ij " +
                "on s." + DbSchemas.Ses.STARTDATETIME + " = ij." + DbSchemas.Ses.STARTDATETIME + " " +
                "GROUP BY " + DbSchemas.Ses.VENUE_ID;

        Cursor c = _sql.rawQuery(queryString, null);

        MapMarker[] mapMarkers = new MapMarker[c.getCount()];

        int i = 0;
        while(c.moveToNext()){
            Session session = _db.Sessions.MakeSessionFromCursor(c);
            SessionVM sessionVM = new SessionVM(session);

            if(sessionVM.Latitude() == null || sessionVM.Longitude() == null){
                continue;
            }

            String datetime;
            if (currentDateTime.getDayOfYear() == session.StartDate.getDayOfYear()
                    || currentDateTime.getYear() == session.StartDate.getYear()){
                datetime = "kl. " + sessionVM.StartTimeAsString();
            }
            else
            {
                datetime = sessionVM.StartDateWithPattern("EEEE dd.") + " kl. " + sessionVM.StartTimeAsString();
            }
            MapMarker mapMarker = new MapMarker();
            mapMarker.Name = sessionVM.Venue();
            mapMarker.SessionId = sessionVM.SessionId();
            mapMarker.Text = "Næste event: " + datetime + "\r\n" + sessionVM.Title();
            mapMarker.Location = new LatLng(sessionVM.Latitude(), sessionVM.Longitude());
            mapMarker.IconResource = sessionVM.TypeImage();

            mapMarkers[i] = mapMarker;
            i++;
        }

        c.close();

        return mapMarkers;
    }
}