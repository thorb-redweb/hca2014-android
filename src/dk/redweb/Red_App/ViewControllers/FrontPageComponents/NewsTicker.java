package dk.redweb.Red_App.ViewControllers.FrontPageComponents;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.Article.ArticleDetail.ArticleDetailFragment;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 3:32 PM
 */
public class NewsTicker extends LinearLayout implements GestureDetector.OnGestureListener {

    private DbInterface _db;
    private NetworkInterface _net;
    private XmlStore _xml;

    private String _name;
    private XmlNode _page;
    private String _childname;
    private Activity _frontpageactivity;
    private ViewFlipper _flipper;

    ArticleVM[] newsTickerSources;
    LinearLayout[] newsPages;

    public NewsTicker(Activity frontpageActivity, XmlNode page){
        super(frontpageActivity);
        _frontpageactivity = frontpageActivity;
        try {
            _childname = page.getStringFromNode(PAGE.CHILD);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in NewsTicker:Constructor when getting child name from xml", e);
        }

        RedEventApplication app = (RedEventApplication) _frontpageactivity.getApplicationContext();
        _db = app.getDbInterface();
        _net = app.getNetworkInterface();
        _xml = app.getXmlStore();

        _page = page;
        try {
            _name = _page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting name from Xml", e);
        }

        LayoutInflater inflater = LayoutInflater.from(frontpageActivity);
        inflater.inflate(R.layout.fp_newsticker, this);

        _flipper = (ViewFlipper)findViewById(R.id.newsticker_theflipper);

        GetSources();
        CreateLeafs();
        FillLeafs();

        setAppearance();
        setText();
    }

    private void setAppearance(){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_name))
                localLook = _xml.getAppearanceForPage(_name);
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            LinearLayout box = (LinearLayout)findViewById(R.id.newsticker_box);
            helper.setViewBackgroundColor(box, LOOK.NEWSTICKER_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView boxTitle = (TextView)findViewById(R.id.newsticker_lblTitle);
            helper.setTextColor(boxTitle, LOOK.NEWSTICKER_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(boxTitle, LOOK.NEWSTICKER_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.setTextStyle(boxTitle, LOOK.NEWSTICKER_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.setTextShadow(boxTitle, LOOK.NEWSTICKER_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.NEWSTICKER_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

            for(LinearLayout leaf : newsPages){
                setLeafAppearance(leaf);
            }

        } catch (Exception e) {
            MyLog.e("Exception in TabBarBox:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(this, _name,_xml);
            helper.setText(R.id.newsticker_lblTitle, TEXT.NEWSTICKER_TITLE, DEFAULTTEXT.NEWSTICKER_TITLE);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }

    }

    private void setLeafAppearance(LinearLayout leaf){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.pageHasAppearance(_page))
                localLook = _xml.getAppearanceForPage(_page.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            TextView txtTitle = (TextView)leaf.findViewById(R.id.newstickerleaf_lblTitle);
            helper.setTextColor(txtTitle, LOOK.NEWSTICKER_ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtTitle, LOOK.NEWSTICKER_ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setTextStyle(txtTitle, LOOK.NEWSTICKER_ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setTextShadow(txtTitle, LOOK.NEWSTICKER_ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.NEWSTICKER_ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtBody = (TextView)leaf.findViewById(R.id.newstickerleaf_lblBody);
            helper.setTextColor(txtBody, LOOK.NEWSTICKER_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtBody, LOOK.NEWSTICKER_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtBody, LOOK.NEWSTICKER_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtBody, LOOK.NEWSTICKER_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.NEWSTICKER_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception in TabBarBox:setLeafAppearance", e);
        }

    }

    private void GetSources(){
        int catid = 0;
        try {
            catid = _page.getIntegerFromNode(PAGE.CATID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in NewsTicker:Constructor when getting catid from xml", e);
        }
        newsTickerSources = _db.Articles.getListOfLastThree(catid);
    }

    private void CreateLeafs()
    {
        LayoutInflater inflater = LayoutInflater.from(_frontpageactivity);
        newsPages = new LinearLayout[newsTickerSources.length];
        for(int i = 0; i < newsTickerSources.length; i++)
        {
            LinearLayout leaf = (LinearLayout)inflater.inflate(R.layout.view_newstickerleaf, _flipper, false);
            newsPages[i] = leaf;
            _flipper.addView(leaf);
        }
    }

    private void FillLeafs(){
        int i = 0;
        for(LinearLayout leaf : newsPages){
            ArticleVM thisData = newsTickerSources[i];

            ((TextView)leaf.findViewById(R.id.newstickerleaf_lblTitle)).setText(thisData.Title());
            ((TextView)leaf.findViewById(R.id.newstickerleaf_lblBody)).setText(thisData.IntroTextWithoutHtml());
            _net.fetchImageOnThread(thisData.IntroImagePath(), (ImageView) leaf.findViewById(R.id.newstickerleaf_img));
            i++;
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        int index = _flipper.getDisplayedChild();
        ArticleVM selectedArticle = newsTickerSources[index];

        try {
            XmlNode selectedPage = _xml.getPage(_childname);
            Intent detailIntent = new Intent(_frontpageactivity, ArticleDetailFragment.class);
            detailIntent.putExtra(EXTRA.ARTICLEID, selectedArticle.ArticleId());
            detailIntent.putExtra(EXTRA.PAGE, selectedPage);
            _frontpageactivity.startActivity(detailIntent);
        } catch (Exception e) {
            MyLog.e("Exception in NewsTicker:onClickListener", e);
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
        _flipper.stopFlipping();
        if (velocityX > 0){
            _flipper.setInAnimation(_frontpageactivity, R.anim.newsticker_infromleft);
            _flipper.setOutAnimation(_frontpageactivity, R.anim.newsticker_outtoright);
            _flipper.showPrevious();
        }
        else if(velocityX < 0){
            _flipper.setInAnimation(_frontpageactivity, R.anim.newsticker_infromright);
            _flipper.setOutAnimation(_frontpageactivity, R.anim.newsticker_outtoleft);
            _flipper.showNext();
        }
        return true;
    }
}
