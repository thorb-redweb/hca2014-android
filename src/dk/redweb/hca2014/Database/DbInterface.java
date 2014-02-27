package dk.redweb.hca2014.Database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import dk.redweb.hca2014.Database.DbClasses.*;
import dk.redweb.hca2014.Interfaces.Delegate_dumpToDatabase;
import dk.redweb.hca2014.Interfaces.Delegate_updateToDatabase;
import dk.redweb.hca2014.Interfaces.IDbInterface;
import dk.redweb.hca2014.DatabaseModel.Event;
import dk.redweb.hca2014.DatabaseModel.Session;
import dk.redweb.hca2014.DatabaseModel.Venue;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 9:21 AM
 */
public class DbInterface extends SQLiteOpenHelper implements IDbInterface {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    NetworkInterface _net;
    ServerInterface _sv;
    XmlStore _xml;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RedApp.db";

    public final DbArticles Articles;
    public final DbBikeContest BikeContest;
    public final DbCommon Common;
    public final DbEvents Events;
    public final DbMapMarkers MapMarkers;
    public final DbPushMessages PushMessages;
    public final DbPushMessageGroups PushMessageGroups;
    public final DbRedUpload RedUpload;
    public final DbSessions Sessions;
    public final DbVenues Venues;

    private static final String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + DbSchemas.Art.TABLE_NAME +
            " (" + DbSchemas.Art._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Art.ARTICLE_ID + " INTEGER, " +
            DbSchemas.Art.CATID + " INTEGER, " +
            DbSchemas.Art.TITLE + " TEXT, " +
            DbSchemas.Art.ALIAS + " TEXT, " +
            DbSchemas.Art.INTROTEXT + " TEXT, " +
            DbSchemas.Art.FULLTEXT + " TEXT, " +
            DbSchemas.Art.INTROIMAGEPATH + " TEXT, " +
            DbSchemas.Art.MAINIMAGEPATH + " TEXT, " +
            DbSchemas.Art.PUBLISHDATE + " TEXT)";
    private static final String SQL_CREATE_BIKECONTEST_TABLE = "CREATE TABLE " + DbSchemas.Bike.TABLE_NAME +
            " (" + DbSchemas.Bike._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Bike.LATITUDE + " REAL, " +
            DbSchemas.Bike.LONGITUDE + " REAL, " +
            DbSchemas.Bike.TIME + " BIGINT)";
    private static final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + DbSchemas.Event.TABLE_NAME +
            " (" + DbSchemas.Event._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Event.EVENT_ID + " INTEGER, " +
            DbSchemas.Event.TITLE + " TEXT, " +
            DbSchemas.Event.SUMMARY + " TEXT, " +
            DbSchemas.Event.DETAILS + " TEXT, " +
            DbSchemas.Event.SUBMISSION + " TEXT, " +
            DbSchemas.Event.IMAGEPATH + " TEXT)";
    private static final String SQL_CREATE_PUSHMESSAGE_TABLE = "CREATE TABLE " + DbSchemas.Push.TABLE_NAME +
            " (" + DbSchemas.Push._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Push.PUSHMESSAGE_ID + " INTEGER, " +
            DbSchemas.Push.GROUP_ID + " INTEGER, " +
            DbSchemas.Push.INTRO + " TEXT, " +
            DbSchemas.Push.MESSAGE + " TEXT, " +
            DbSchemas.Push.SENDDATE + " TEXT, " +
            DbSchemas.Push.AUTHOR + " TEXT)";
    private static final String SQL_CREATE_PUSHMESSAGEGROUP_TABLE = "CREATE TABLE " + DbSchemas.PushGroup.TABLE_NAME +
            " (" + DbSchemas.PushGroup._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.PushGroup.GROUP_ID + " INTEGER, " +
            DbSchemas.PushGroup.NAME + " TEXT, " +
            DbSchemas.PushGroup.SUBSCRIBED + " INTEGER)";
    private static final String SQL_CREATE_REDUPLOAD_TABLE = "CREATE TABLE " + DbSchemas.RedUpload.TABLE_NAME +
            " (" + DbSchemas.RedUpload._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.RedUpload.LOCALIMAGEPATH + " TEXT, " +
            DbSchemas.RedUpload.SERVERFOLDER + " TEXT, " +
            DbSchemas.RedUpload.TEXT + " TEXT, " +
            DbSchemas.RedUpload.APPROVED + " INTEGER)";
    private static final String SQL_CREATE_SESSION_TABLE = "CREATE TABLE " + DbSchemas.Ses.TABLE_NAME +
            " (" + DbSchemas.Ses._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Ses.SESSION_ID + " INTEGER, " +
            DbSchemas.Ses.EVENT_ID + " INTEGER, " +
            DbSchemas.Ses.VENUE_ID + " INTEGER, " +
            DbSchemas.Ses.TITLE + " TEXT, " +
            DbSchemas.Ses.DETAILS + " TEXT, " +
            DbSchemas.Ses.EVENTTYPE + " TEXT, " +
            DbSchemas.Ses.STARTDATETIME + " TEXT, " +
            DbSchemas.Ses.ENDDATETIME + " TEXT)";
    private static final String SQL_CREATE_VENUE_TABLE = "CREATE TABLE " + DbSchemas.Venue.TABLE_NAME +
            " (" + DbSchemas.Venue._ID + " INTEGER PRIMARY KEY," +
            DbSchemas.Venue.VENUE_ID + " INTEGER, " +
            DbSchemas.Venue.TITLE + " TEXT, " +
            DbSchemas.Venue.DESCRIPTION + " TEXT, " +
            DbSchemas.Venue.STREET + " TEXT, " +
            DbSchemas.Venue.CITY + " TEXT, " +
            DbSchemas.Venue.LATITUDE + " REAL, " +
            DbSchemas.Venue.LONGITUDE + " REAL, " +
            DbSchemas.Venue.IMAGEPATH + " TEXT)";

    private static final String SQL_DELETE_ARTICLE_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Art.TABLE_NAME;
    private static final String SQL_DELETE_BIKECONTEST_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Bike.TABLE_NAME;
    private static final String SQL_DELETE_EVENT_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Event.TABLE_NAME;
    private static final String SQL_DELETE_PUSHMESSAGE_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Push.TABLE_NAME;
    private static final String SQL_DELETE_PUSHMESSAGEGROUP_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.PushGroup.TABLE_NAME;
    private static final String SQL_DELETE_REDUPLOAD_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.RedUpload.TABLE_NAME;
    private static final String SQL_DELETE_SESSION_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Ses.TABLE_NAME;
    private static final String SQL_DELETE_VENUE_TABLE = "DROP TABLE IF EXISTS " + DbSchemas.Venue.TABLE_NAME;

    public DbInterface(RedEventApplication app, NetworkInterface net, ServerInterface sv, XmlStore xml){
        super(app, DATABASE_NAME, null, DATABASE_VERSION);
        this._app = app;
        _sql = getWritableDatabase();
        _net = net;
        _sv = sv;
        _xml = xml;

        Articles = new DbArticles(_app, _sql, _sv, _xml);
        BikeContest = new DbBikeContest(_app,_sql,_sv,_xml);
        Common = new DbCommon(_app, _sql, _sv, _xml);
        Events = new DbEvents(_app, _sql, this, _sv, _xml);
        MapMarkers = new DbMapMarkers(_app, _sql, this, _sv, _xml);
        PushMessages = new DbPushMessages(_app, _sql, this, _sv, _xml);
        PushMessageGroups = new DbPushMessageGroups(_app, _sql, this, _sv, _xml);
        RedUpload = new DbRedUpload(_app, _sql, this, _sv, _xml);
        Sessions = new DbSessions(_app, _sql, this, _sv, _xml);
        Venues = new DbVenues(_app, _sql, this, _sv, _xml);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
        db.execSQL(SQL_CREATE_BIKECONTEST_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_PUSHMESSAGE_TABLE);
        db.execSQL(SQL_CREATE_PUSHMESSAGEGROUP_TABLE);
        db.execSQL(SQL_CREATE_REDUPLOAD_TABLE);
        db.execSQL(SQL_CREATE_SESSION_TABLE);
        db.execSQL(SQL_CREATE_VENUE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ARTICLE_TABLE);
        db.execSQL(SQL_DELETE_BIKECONTEST_TABLE);
        db.execSQL(SQL_DELETE_EVENT_TABLE);
        db.execSQL(SQL_DELETE_PUSHMESSAGE_TABLE);
        db.execSQL(SQL_DELETE_PUSHMESSAGEGROUP_TABLE);
        db.execSQL(SQL_DELETE_REDUPLOAD_TABLE);
        db.execSQL(SQL_DELETE_SESSION_TABLE);
        db.execSQL(SQL_DELETE_VENUE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
    public void onClose(){
        _sql.close();
    }

    //Common
    public boolean DatabaseIsEmpty() {
        return DbCommon.DatabaseIsEmpty(_sql);
    }

    public void dumpServer(String result, Delegate_dumpToDatabase delegate){
        new Handler_DumpToDatabase(_app, this, delegate).execute(result);
    }

    public void updateFromServer(String result, Delegate_updateToDatabase delegate) {
        new Handler_UpdateToDatabase(_app, this, delegate).execute(result);
    }

    @Override
    public <T extends Object> T GetLazyLoadedObject(int wantedObjectsId, Class<T> wantedType, Type askingType) {
        T returnObject = null;
        if(wantedType == Event.class && askingType == Session.class){
            returnObject = wantedType.cast(Events.getFromId(wantedObjectsId));
        } else if (wantedType == Venue.class && askingType == Session.class){
            returnObject = wantedType.cast(Venues.getFromId(wantedObjectsId));
        }
        return returnObject;
    }

    @Override
    public <T extends Object> ArrayList<T> GetLazyLoadedObjects(int askingObjectsId, Class<T> wantedType, Type askingType) {
        throw new UnsupportedOperationException();

//        ArrayList<T> returnObjects = new ArrayList<T>();
//        if(wantedType == Session.class && askingType == Event.class){
//            returnObjects = Sessions.getAllFromEventId(askingObjectsId);
//        } else if (wantedType == Session.class && askingType == Venue.class){
//            returnObjects = Sessions.getAllFromVenueId(askingObjectsId);
//        }
//        return returnObjects;
    }
}
