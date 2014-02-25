package dk.redweb.Red_App.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import dk.redweb.Red_App.DatabaseModel.Article;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.ViewModels.ArticleVM;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 2/25/14
 * Time: 9:06
 */
public class NewsTicker extends ViewFlipper implements GestureDetector.OnGestureListener {
    private Context context;
    private Activity activity;

    ArticleVM[] newsTickerSources;
    LinearLayout[] newsPages;

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
        this.context = context;
        newsTickerSources = new ArticleVM[3];
    }

    public void SetupNewsticker()
    {
        LinearLayout newsItem1 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news1);
        LinearLayout newsItem2 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news2);
        LinearLayout newsItem3 = (LinearLayout)(this).findViewById(R.id.hcasplitview_news3);
        newsPages = new LinearLayout[]{newsItem1, newsItem2, newsItem3};
    }

    public void changeContent(ArticleVM[] newsData, NetworkInterface net)
    {
        for (int i = 0; i < 3; i++){
            LinearLayout thisPage = newsPages[i];
            ArticleVM thisData = newsData[i];
            newsTickerSources[i] = thisData;

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
        Uri newsLink;

        LinearLayout view = newsPages[this.getDisplayedChild()];

        if (view.getId() == R.id.hcasplitview_news1){
            newsLink = newsTickerSources[0].ExternalLink();
        } else if (view.getId() == R.id.hcasplitview_news2){
            newsLink = newsTickerSources[1].ExternalLink();
        } else {
            newsLink = newsTickerSources[2].ExternalLink();
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, newsLink);
        activity.startActivity(browserIntent);

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
            thisTicker.setInAnimation(context, R.anim.newsticker_infromleft);
            thisTicker.setOutAnimation(context, R.anim.newsticker_outtoright);
            thisTicker.showPrevious();
        }
        else if(velocityX < 0){
            thisTicker.setInAnimation(context, R.anim.newsticker_infromright);
            thisTicker.setOutAnimation(context, R.anim.newsticker_outtoleft);
            thisTicker.showNext();
        }
        return true;
    }
}
