package dk.redweb.hca2014.Database.DbClasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:59 AM
 */
public class DbCommon {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    public DbCommon(RedEventApplication app, SQLiteDatabase db, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = db;
        _sv = sv;
        _xml = xml;
    }

    public static boolean DatabaseIsEmpty(SQLiteDatabase db){
        String queryString = "SELECT COUNT(*) FROM " + DbSchemas.Art.TABLE_NAME;
        Cursor c = db.rawQuery(queryString, null);
        c.moveToFirst();
        int rows = c.getInt(0);
        c.close();

        queryString = "SELECT COUNT(*) FROM " + DbSchemas.Ses.TABLE_NAME;
        c = db.rawQuery(queryString, null);
        c.moveToFirst();
        rows += c.getInt(0);
        c.close();

        return rows == 0;
    }
}
