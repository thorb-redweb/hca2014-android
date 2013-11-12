package dk.redweb.Red_App.ViewControllers.Article.AdventCal;

import dk.redweb.Red_App.ViewModels.ArticleVM;
import org.joda.time.DateTime;

import java.util.Comparator;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/21/13
 * Time: 9:29
 */
public class AdventComparator implements Comparator<ArticleVM> {
    @Override
    public int compare(ArticleVM lhs, ArticleVM rhs) {

        DateTime leftDateTime = getOpenDateTime(lhs);
        DateTime rightDateTime = getOpenDateTime(rhs);

        if(leftDateTime.isBefore(rightDateTime)){
            return -1;
        } else if(leftDateTime.isAfter(rightDateTime)){
            return +1;
        }
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DateTime getOpenDateTime(ArticleVM window){
        return DateTime.parse(window.Title());
    }
}
