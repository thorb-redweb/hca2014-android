package dk.redweb.hca2014.Views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 2/25/14
 * Time: 9:06
 */
public class NewsTicker extends ViewFlipper implements GestureDetector.OnGestureListener {
    private Context _context;
    private FragmentActivity _activity;
    RedEventApplication _app;
    XmlNode _childPage;

    ArticleVM[] _newsTickerSources;
    LinearLayout[] _newsPages;

    public NewsTicker(Context context) {
        super(context);
        Constructor(context);
    }

    public NewsTicker(Context context, AttributeSet attrs){
        super(context, attrs);
        Constructor(context);
    }

    private void Constructor(final Context context)
    {
        _context = context;
        _newsTickerSources = new ArticleVM[3];
    }

    public void SetupNewsticker(FragmentActivity activity, XmlNode childPage)
    {
        LinearLayout newsItem1 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news1);
        LinearLayout newsItem2 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news2);
        LinearLayout newsItem3 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news3);
        _newsPages = new LinearLayout[]{newsItem1, newsItem2, newsItem3};
        _childPage = childPage;
        _activity = activity;
        _app = (RedEventApplication)activity.getApplication();
    }

    public void changeContent(ArticleVM[] newsData, NetworkInterface net)
    {
        int newsAmount = newsData.length > 3 ? 3 : newsData.length;
        for (int i = 0; i < newsAmount; i++){
            LinearLayout thisPage = _newsPages[i];
            ArticleVM thisData = newsData[i];
            _newsTickerSources[i] = thisData;

            ((TextView)thisPage.findViewById(R.id.newsticker_txtNewsTitle)).setText(thisData.Title());
            ((TextView)thisPage.findViewById(R.id.newsticker_txtNewsBody)).setText(Html.fromHtml(thisData.IntroTextWithoutHtml()));
            net.fetchImageOnThread(thisData.IntroImagePath(), (ImageView) thisPage.findViewById(R.id.newsticker_imgNews));
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        ArticleVM article;

        LinearLayout view = _newsPages[this.getDisplayedChild()];

        if (view.getId() == R.id.hcasplitview_news1){
            article = _newsTickerSources[0];
        } else if (view.getId() == R.id.hcasplitview_news2){
            article = _newsTickerSources[1];
        } else {
            article = _newsTickerSources[2];
        }
        XmlNode articlePage = _childPage.deepClone();
        try {
            articlePage.addChildToNode(PAGE.ARTICLEID, article.ArticleId());
        } catch (InvalidPropertiesFormatException e) {
            MyLog.e("Exception when adding articleId to child page", e);
        }
        try {
            NavController.changePageWithXmlNode(articlePage,_activity);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when changing page with childpage", e);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
        NewsTicker thisTicker = this;
        thisTicker.stopFlipping();
        if (velocityX > 0){
            thisTicker.setInAnimation(_context, R.anim.newsticker_infromleft);
            thisTicker.setOutAnimation(_context, R.anim.newsticker_outtoright);
            thisTicker.showPrevious();
        }
        else if(velocityX < 0){
            thisTicker.setInAnimation(_context, R.anim.newsticker_infromright);
            thisTicker.setOutAnimation(_context, R.anim.newsticker_outtoleft);
            thisTicker.showNext();
        }
        return true;
    }
}
