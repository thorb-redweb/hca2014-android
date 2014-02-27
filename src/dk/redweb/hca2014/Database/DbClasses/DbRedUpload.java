package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.DatabaseModel.RedUploadImage;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 11:08
 */
public class DbRedUpload {
    RedEventApplication _app;
    DbInterface _db;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private final String[] ALL_COLUMNS = {DbSchemas.RedUpload.LOCALIMAGEPATH, DbSchemas.RedUpload.SERVERFOLDER, DbSchemas.RedUpload.TEXT, DbSchemas.RedUpload.APPROVED};

    public DbRedUpload(RedEventApplication app, SQLiteDatabase sql, DbInterface db, ServerInterface sv, XmlStore xml) {
        _app = app;
        _sql = sql;
        _db = db;
        _sv = sv;
        _xml = xml;
    }

    public RedUploadImage[] getAll(){
        Cursor c = _sql.query(DbSchemas.RedUpload.TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);

        RedUploadImage[] redUploadImages = new RedUploadImage[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            redUploadImages[i] = MakeRedUploadImageFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbRedUpload:getFromServerFolder: No redUploadImages received");
        }

        c.close();

        return redUploadImages;
    }

    public RedUploadImage getFromImagePath(String imagePath){
        String whereString = DbSchemas.RedUpload.LOCALIMAGEPATH + " = '" + imagePath + "'";

        Cursor c = _sql.query(DbSchemas.RedUpload.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null, "1");
        c.moveToFirst();

        if(c.getCount() > 0)
        {
            return MakeRedUploadImageFromCursor(c);
        }
        return null;
    }

    public RedUploadImage[] getFromServerFolder(String serverFolder){
        String whereString = DbSchemas.RedUpload.SERVERFOLDER + " = '" + serverFolder + "'";

        Cursor c = _sql.query(DbSchemas.RedUpload.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null);

        RedUploadImage[] redUploadImages = new RedUploadImage[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            redUploadImages[i] = MakeRedUploadImageFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbRedUpload:getFromServerFolder: No redUploadImages received");
        }

        c.close();

        return redUploadImages;
    }

    public boolean serverFolderHasEntries(String serverFolder){
        String whereString = DbSchemas.RedUpload.SERVERFOLDER + " = '" + serverFolder + "'";
        Cursor c = _sql.query(DbSchemas.RedUpload.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null);

        if(c.getCount() > 0){
            return true;
        }
        return false;
    }

    public void createEntry(RedUploadImage redUploadImage){
        ContentValues values = new ContentValues();
        values.put(DbSchemas.RedUpload.LOCALIMAGEPATH, redUploadImage.localImagePath);
        values.put(DbSchemas.RedUpload.SERVERFOLDER, redUploadImage.serverFolder);
        values.put(DbSchemas.RedUpload.TEXT, redUploadImage.text);
        values.put(DbSchemas.RedUpload.APPROVED, redUploadImage.approved);

        _sql.insert(DbSchemas.RedUpload.TABLE_NAME, null, values);
        MyLog.v("New redUploadImage with image path:" + redUploadImage.localImagePath + " written to database");
    }

    public void updateEntry(RedUploadImage redUploadImage){
        ContentValues values = new ContentValues();
        values.put(DbSchemas.RedUpload.LOCALIMAGEPATH, redUploadImage.localImagePath);
        values.put(DbSchemas.RedUpload.SERVERFOLDER, redUploadImage.serverFolder);
        values.put(DbSchemas.RedUpload.TEXT, redUploadImage.text);
        values.put(DbSchemas.RedUpload.APPROVED, redUploadImage.approved);

        String whereString = DbSchemas.RedUpload.LOCALIMAGEPATH + " = '" + redUploadImage.localImagePath + "'";
        _sql.update(DbSchemas.RedUpload.TABLE_NAME, values, whereString, null);
        MyLog.v("Updated redUploadImage with image path:" + redUploadImage.localImagePath + " in database");
    }

    public void deleteEntryWithImagePath(String path){
        String whereString = DbSchemas.RedUpload.LOCALIMAGEPATH + " = '" + path + "'";
        _sql.delete(DbSchemas.RedUpload.TABLE_NAME, whereString, null);
    }

    public RedUploadImage MakeRedUploadImageFromCursor(Cursor c)
    {
        RedUploadImage newRedUploadImage = new RedUploadImage();
        try{
            newRedUploadImage.localImagePath = c.getString(c.getColumnIndexOrThrow(DbSchemas.RedUpload.LOCALIMAGEPATH));
            newRedUploadImage.serverFolder = c.getString(c.getColumnIndexOrThrow(DbSchemas.RedUpload.SERVERFOLDER));
            newRedUploadImage.text = c.getString(c.getColumnIndexOrThrow(DbSchemas.RedUpload.TEXT));
            newRedUploadImage.approved = c.getInt(c.getColumnIndexOrThrow(DbSchemas.RedUpload.APPROVED)) != 0;
        }
        catch (Exception e)
        {
            MyLog.e("Exception when inserting cursor data into new RedUploadImage object", e);
        }
        return newRedUploadImage;
    }
}
