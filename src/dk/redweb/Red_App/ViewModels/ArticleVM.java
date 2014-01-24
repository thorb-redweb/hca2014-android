package dk.redweb.Red_App.ViewModels;

import dk.redweb.Red_App.DatabaseModel.Article;
import dk.redweb.Red_App.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 9:08 AM
 */
public class ArticleVM {
    public Locale locale = new Locale("da_DK", "da_DK");
    private Article _article;

    public int ArticleId(){
        return _article.ArticleId;
    }

    public String Title(){
        return _article.Title;
    }

    public String Alias(){
        return _article.Alias;
    }

    public int CatId(){
        return _article.Catid;
    }

    public DateTime PublishDate(){
        return _article.PublishDate;
    }

    public String PublishDateWithPattern(String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dateTimeFormatter.print(_article.PublishDate);
    }

    public boolean IsActive(){
        DateTime now = new DateTime();
        return now.isAfter(_article.PublishDate);
    }

    public String IntroText(){
        return _article.Introtext;
    }

    public String IntroTextWithHtml(){
        return StringUtils.stripJoomlaTags(_article.Introtext);
    }

    public String IntroTextWithoutHtml(){
        return StringUtils.stripHtmlAndJoomla(_article.Introtext);
    }

    public String FullText(){
        return _article.Fulltext;
    }

    public String FullTextWithHtml(){
        return StringUtils.stripJoomlaTags(_article.Fulltext);
    }

    public String FullTextWithoutHtml(){
        return StringUtils.stripHtmlAndJoomla(_article.Fulltext);
    }

    public String IntroImagePath(){
        return _article.IntroImagePath;
    }

    public String MainImagePath(){
        return _article.MainImagePath;
    }

    public ArticleVM(Article article){
        this._article = article;
    }
}
