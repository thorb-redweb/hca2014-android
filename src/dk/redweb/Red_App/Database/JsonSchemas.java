package dk.redweb.Red_App.Database;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 12:45 PM
 */
public class JsonSchemas {
    public static abstract class Art {
        public static final String ARTICLE_ID = "articleid";
        public static final String CATID = "catid";
        public static final String TITLE = "title";
        public static final String ALIAS = "alias";
        public static final String INTROTEXT = "introtext";
        public static final String FULLTEXT = "fulltext";
        public static final String INTROIMAGEPATH = "introimagepath";
        public static final String MAINIMAGEPATH = "mainimagepath";
        public static final String PUBLISHDATE = "publishdate";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }

    public static abstract class Event {
        public static final String EVENT_ID = "eventid";
        public static final String TITLE = "title";
        public static final String SUMMARY = "summary";
        public static final String DETAILS = "details";
        public static final String SUBMISSION = "submission";
        public static final String IMAGEPATH = "imagepath";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }

    public static abstract class Push {
        public static final String PUSHMESSAGE_ID = "pushmessageid";
        public static final String GROUP_ID = "groupid";
        public static final String INTRO = "intro";
        public static final String MESSAGE = "message";
        public static final String AUTHOR = "author";
        public static final String SENDDATE = "senddate";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }

    public static abstract class PushGroup {
        public static final String TABLE_NAME = "pushmessages";
        public static final String GROUP_ID = "groupid";
        public static final String NAME = "name";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }

    public static abstract class Ses {
        public static final String SESSION_ID = "sessionid";
        public static final String EVENT_ID = "eventid";
        public static final String VENUE_ID = "venueid";
        public static final String TITLE = "title";
        public static final String DETAILS = "details";
        public static final String STARTDATE = "startdate";
        public static final String ENDDATE = "enddate";
        public static final String STARTTIME = "starttime";
        public static final String ENDTIME = "endtime";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }

    public static abstract class Venue {
        public static final String VENUE_ID = "venueid";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String STREET = "street";
        public static final String CITY = "city";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String IMAGEPATH = "imagepath";
        public static final String ITEMTYPE = "itemtype";
        public static final String ACTIONTYPE = "actiontype";
    }
}
