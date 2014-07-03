package dk.redweb.hca2014.Database.DbClasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dk.redweb.hca2014.Database.Converters;
import dk.redweb.hca2014.Database.DbSchemas;
import dk.redweb.hca2014.Database.JsonSchemas;
import dk.redweb.hca2014.DatabaseModel.Article;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
* Created by Redweb with IntelliJ IDEA.
* Date: 9/18/13
* Time: 10:58 AM
*/
public class DbArticles {
    RedEventApplication _app;
    SQLiteDatabase _sql;
    ServerInterface _sv;
    XmlStore _xml;

    private static String[] ALL_COLUMNS = {DbSchemas.Art.ARTICLE_ID, DbSchemas.Art.CATID,
            DbSchemas.Art.TITLE, DbSchemas.Art.ALIAS, DbSchemas.Art.INTROTEXT, DbSchemas.Art.FULLTEXT,
        DbSchemas.Art.INTROIMAGEPATH, DbSchemas.Art.MAINIMAGEPATH, DbSchemas.Art.PUBLISHDATE};

    public DbArticles(RedEventApplication app, SQLiteDatabase sql, ServerInterface sv, XmlStore xml){
        _app = app;
        _sql = sql;
        _sv = sv;
        _xml = xml;
    }

    public Article[] getAll(){
        String sortString = DbSchemas.Art.PUBLISHDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Art.TABLE_NAME, ALL_COLUMNS, null, null, null, null, sortString);

        Article[] articles = new Article[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakeFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbArticles:getPublishedListFromCatid: No articles received");
        }

        c.close();

        return articles;
    }

    public Article[] getListFromCatid(int catid){
        String whereString = DbSchemas.Art.CATID + " = " + catid;
        String sortString = DbSchemas.Art.PUBLISHDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Art.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, sortString);

        Article[] articles = new Article[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakeFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbArticles:getPublishedListFromCatid: No articles received");
        }

        c.close();

        return articles;
    }

    public ArticleVM[] getVMListFromCatid(int catid) {
        Article[] articles = getListFromCatid(catid);
        ArticleVM[] articleVMs = new ArticleVM[articles.length];
        for(int i = 0; i < articles.length; i++){
            articleVMs[i] = new ArticleVM(articles[i]);
        }
        return articleVMs;
    }

    public Article[] getPublishedListFromCatids(int[] catids){
        DateTime currentDateTime;
        if(_app.isDebugging()){
            currentDateTime = _app.getDebugCurrentDate();
        } else {
            currentDateTime = new DateTime();
        }

        String dateTimeString = "'" + Converters.JodaDateTimeToSQLDateTime(currentDateTime) + "'";

        String whereCatidString = DbSchemas.Art.CATID + " = " + catids[0];
        for(int i = 1; i < catids.length; i++){
            int catid = catids[i];
            whereCatidString += " OR " + DbSchemas.Art.CATID + " = " + catid;
        }
        String whereDateString = " AND datetime(" + DbSchemas.Art.PUBLISHDATE + ") <= datetime(" + dateTimeString + ")";
        String whereString = whereCatidString + whereDateString;
        String sortString = DbSchemas.Art.PUBLISHDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Art.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, sortString);

        Article[] articles = new Article[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            articles[i] = MakeFromCursor(c);
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbArticles:getPublishedListFromCatid: No articles received");
        }

        c.close();

        return articles;
    }

    public ArticleVM[] getPublishedVMListFromCatid(int catid) {
        Article[] articles = getPublishedListFromCatids(new int[]{catid});
        ArticleVM[] articleVMs = new ArticleVM[articles.length];
        for(int i = 0; i < articles.length; i++){
            articleVMs[i] = new ArticleVM(articles[i]);
        }
        return articleVMs;
    }

    public ArticleVM[] getPublishedVMListFromCatids(int[] catids) {
        Article[] articles = getPublishedListFromCatids(catids);

        ArticleVM[] articleVMs = new ArticleVM[articles.length];
        for(int i = 0; i < articles.length; i++){
            articleVMs[i] = new ArticleVM(articles[i]);
        }
        return articleVMs;
    }

    public Article getFromId(int articleid){
        String whereString = DbSchemas.Art.ARTICLE_ID + " = " + articleid;
         Cursor c = _sql.query(DbSchemas.Art.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, null);

        Article selectedArticle = new Article();

        if (c.moveToNext())
        {
            selectedArticle = MakeFromCursor(c);
        }
        else{
            MyLog.d("DbArticles.getFromId: No article selected!");
        }

        c.close();

        return selectedArticle;
    }

    public ArticleVM getVMFromId(int articleid){
        Article article = getFromId(articleid);
        ArticleVM articleVM = new ArticleVM(article);
        return articleVM;
    }

    public ArticleVM[] getListOfLastThree(){
        DateTime currentDateTime;
        if(_app.isDebugging()){
            currentDateTime = _app.getDebugCurrentDate();
        } else {
            currentDateTime = new DateTime();
        }

        String dateTimeString = "'" + Converters.JodaDateTimeToSQLDateTime(currentDateTime) + "'";

        int[] catids = new int[]{9,30,31,34};
        String whereString = DbSchemas.Art.CATID + " = " + catids[0];
        for(int i = 1; i < catids.length; i++){
            int catid = catids[i];
            whereString += " OR " + DbSchemas.Art.CATID + " = " + catid;
        }
        String sortString = DbSchemas.Art.PUBLISHDATE + " DESC";

        Cursor c = _sql.query(DbSchemas.Art.TABLE_NAME, ALL_COLUMNS, whereString, null, null, null, sortString, "0, 3");

        ArticleVM[] news = new ArticleVM[c.getCount()];

        int i = 0;
        while(c.moveToNext())
        {
            news[i] = new ArticleVM(MakeFromCursor(c));
            i++;
        }

        if(c.getCount() == 0){
            MyLog.d("DbArticles.getLastThreeNews: No news selected!");
        }

        c.close();

        return news;
    }

    public void importSingleFromJSON(JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();
        int articleId = Integer.parseInt(jsonObject.getString(JsonSchemas.Art.ARTICLE_ID));
        values.put(DbSchemas.Art.ARTICLE_ID, articleId);
        int catId = Integer.parseInt(jsonObject.getString(JsonSchemas.Art.CATID));
        values.put(DbSchemas.Art.CATID, catId);
        values.put(DbSchemas.Art.TITLE, jsonObject.getString(JsonSchemas.Art.TITLE));
        values.put(DbSchemas.Art.ALIAS, jsonObject.getString(JsonSchemas.Art.ALIAS));
        values.put(DbSchemas.Art.INTROTEXT, jsonObject.getString(JsonSchemas.Art.INTROTEXT));
        values.put(DbSchemas.Art.FULLTEXT, jsonObject.getString(JsonSchemas.Art.FULLTEXT));
        values.put(DbSchemas.Art.PUBLISHDATE, jsonObject.getString(JsonSchemas.Art.PUBLISHDATE));

        String introimagePath = jsonObject.getString(JsonSchemas.Art.INTROIMAGEPATH);
        if (introimagePath != "" && !introimagePath.startsWith(_xml.joomlaPath)) {
            introimagePath = _xml.joomlaPath + introimagePath;
        }
        values.put(DbSchemas.Art.INTROIMAGEPATH, introimagePath);

        String mainimagePath = jsonObject.getString(JsonSchemas.Art.MAINIMAGEPATH);
        if (mainimagePath != "" && !mainimagePath.startsWith(_xml.joomlaPath)) {
            mainimagePath = _xml.joomlaPath + mainimagePath;
        }
        values.put(DbSchemas.Art.MAINIMAGEPATH, mainimagePath);


        String artExistsQuery = "SELECT EXISTS(SELECT 1 FROM " + DbSchemas.Art.TABLE_NAME +
                " WHERE " + DbSchemas.Art.ARTICLE_ID + " = " + articleId + " LIMIT 1)";
        Cursor c = _sql.rawQuery(artExistsQuery, null);
        c.moveToFirst();
        Boolean articleExists = c.getInt(0) > 0;

        if(articleExists){
            String whereString = DbSchemas.Art.ARTICLE_ID + " = " + articleId;
            _sql.update(DbSchemas.Art.TABLE_NAME, values, whereString, null);
            MyLog.v("Updated article data for id:" + articleId + " written to database");
        } else {
            _sql.insert(DbSchemas.Art.TABLE_NAME, null, values);
            MyLog.v("New article with id:" + articleId + " written to database");
        }
    }

    public void deleteSingleFromJSON(JSONObject jsonObject) throws JSONException {
        String whereString = DbSchemas.Art.ARTICLE_ID + " = " + Integer.parseInt(jsonObject.getString(JsonSchemas.Art.ARTICLE_ID));
        _sql.delete(DbSchemas.Art.TABLE_NAME, whereString, null);
    }

    public static Article MakeFromCursor(Cursor c)
    {
        Article newArticle = new Article();
        try {
            newArticle.ArticleId = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Art.ARTICLE_ID));
            newArticle.Catid = c.getInt(c.getColumnIndexOrThrow(DbSchemas.Art.CATID));
            newArticle.Title = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.TITLE));
            newArticle.Alias = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.ALIAS));
            newArticle.Introtext = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.INTROTEXT));
            newArticle.Fulltext = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.FULLTEXT));
            newArticle.IntroImagePath = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.INTROIMAGEPATH));
            newArticle.MainImagePath = c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.MAINIMAGEPATH));
            newArticle.PublishDate = Converters.SQLDateTimeToJodaDateTime(c.getString(c.getColumnIndexOrThrow(DbSchemas.Art.PUBLISHDATE)));
        } catch (Exception e) {
            MyLog.e("Exception in DbArticles:MakeFromCursor", e);
        }
        return newArticle;
    }
}
