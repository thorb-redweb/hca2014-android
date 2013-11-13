package dk.redweb.Red_App.ViewControllers.Article.StaticArticle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 1:47 PM
 */
public class StaticArticleFragment extends BasePageFragment {

    public StaticArticleFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_staticarticle);

        ArticleVM article = null;
        try {
            article = _db.Articles.getVMFromId(_page.getIntegerFromNode(PAGE.ARTICLEID));
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in StaticArticleActivity:onCreate on attempt to acquire ArticleVM", e);
        }

        WebView body = (WebView)findViewById(R.id.staticArticle_webBody);

        body.loadDataWithBaseURL(null, article.FullText(), "text/html", "UTF-8", null);

        return _view;
    }
}