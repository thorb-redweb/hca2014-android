package dk.redweb.EfterSkole_App.Model;

import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:00 AM
 */
public class Article {
    public int ArticleId;
    public String Title;
    public String Alias;
    public String Introtext;
    public String Fulltext;
    public int Catid;
    public String IntroImagePath;
    public String MainImagePath;
    public DateTime PublishDate;

    public Article(){
        ArticleId = -1;
    }
}
