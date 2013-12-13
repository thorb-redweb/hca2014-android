package dk.redweb.Red_App.Database;

import android.provider.BaseColumns;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:49 AM
 */
public class DbSchemas {
    public DbSchemas(){}

    public static abstract class Art implements BaseColumns{
        public static final String TABLE_NAME = "articles";
        public static final String ARTICLE_ID = "articleid";
        public static final String CATID = "catid";
        public static final String TITLE = "title";
        public static final String ALIAS = "alias";
        public static final String INTROTEXT = "introtext";
        public static final String FULLTEXT = "fulltext";
        public static final String INTROIMAGEPATH = "introimagepath";
        public static final String MAINIMAGEPATH = "mainimagepath";
        public static final String PUBLISHDATE = "publishdate";
    }

    public static abstract class Bike implements BaseColumns {
        public static final String TABLE_NAME = "bikecontest";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String TIME = "time";
    }

    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String EVENT_ID = "eventid";
        public static final String TITLE = "title";
        public static final String SUMMARY = "summary";
        public static final String DETAILS = "details";
        public static final String SUBMISSION = "submission";
        public static final String IMAGEPATH = "imagepath";
    }

    public static abstract class Push implements BaseColumns {
        public static final String TABLE_NAME = "pushmessages";
        public static final String PUSHMESSAGE_ID = "pushmessageid";
        public static final String GROUP_ID = "groupid";
        public static final String INTRO = "intro";
        public static final String MESSAGE = "message";
        public static final String AUTHOR = "author";
        public static final String SENDDATE = "senddate";
    }

    public static abstract class PushGroup implements BaseColumns {
        public static final String TABLE_NAME = "pushmessagegroups";
        public static final String GROUP_ID = "groupid";
        public static final String NAME = "name";
        public static final String SUBSCRIBED = "subscribed";
    }

    public static abstract class Ses implements BaseColumns {
        public static final String TABLE_NAME = "sessions";
        public static final String SESSION_ID = "sessionid";
        public static final String EVENT_ID = "eventid";
        public static final String VENUE_ID = "venueid";
        public static final String TITLE = "title";
        public static final String DETAILS = "details";
        public static final String STARTDATETIME = "startdatetime";
        public static final String ENDDATETIME = "enddatetime";
    }

    public static abstract class Venue implements BaseColumns{
        public static final String TABLE_NAME = "venues";
        public static final String VENUE_ID = "venueid";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String STREET = "street";
        public static final String CITY = "city";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String IMAGEPATH = "imagepath";
    }
}
