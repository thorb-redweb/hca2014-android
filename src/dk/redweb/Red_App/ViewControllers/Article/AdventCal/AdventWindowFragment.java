package dk.redweb.Red_App.ViewControllers.Article.AdventCal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 9:19 AM
 */
public class AdventWindowFragment extends BasePageFragment {

    private ArticleVM _article;

    public AdventWindowFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_adventwindow);

        int articleId = 0;
        try {
            articleId = _page.getIntegerFromNode(EXTRA.ARTICLEID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting articleId for AdventWindow from page xml", e);
        }

        _article = _db.Articles.getVMFromId(articleId);

        setAppearance();

        try {
            if(_page.hasChild(PAGE.RETURNONTAP) && _page.getBoolFromNode(PAGE.RETURNONTAP)){
                LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.adventwindow_lnrMainView);
                lnrBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to get ReturnOnTap attribute from page xml", e);
        }

        TextView txtTitle = (TextView)findViewById(R.id.adventwindow_lblTitle);
        DateTime windowDateTime = new AdventComparator().getOpenDateTime(_article);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d MMMM").withLocale(_article.locale);
        String titleText = dateTimeFormatter.print(windowDateTime);
        txtTitle.setText(titleText);

        ImageView imgPicture = (ImageView)findViewById(R.id.adventwindow_imgPicture);
        _net.fetchImageOnThread(_article.MainImagePath(), imgPicture);

        WebView webBody = (WebView)findViewById(R.id.adventwindow_webBody);
        TextView txtBody = (TextView)findViewById(R.id.adventwindow_lblBody);
        try {
            if(_page.hasChild(PAGE.BODYUSESHTML) && _page.getBoolFromNode(PAGE.BODYUSESHTML)) {
                webBody.loadDataWithBaseURL(null, _article.IntroTextWithHtml(), "text/html", "UTF-8", null);
                txtBody.setVisibility(View.GONE);
            } else {
                txtBody.setText(_article.IntroTextWithoutHtml());
                webBody.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            MyLog.e("Exception in ArticleDetailActivity:onCreate when attempting to determine body type (web vs. text)", e);
        }

        try {
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.adventwindow_flxBackButton);
            if(_page.hasChild(PAGE.RETURNBUTTON) && _page.getBoolFromNode(PAGE.RETURNBUTTON)){
                flxBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                });
            } else {
                flxBackButton.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting ReturnButton attribute from page xml", e);
        }

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.adventwindow_lnrMainView);
            helper.setViewBackgroundTileImageOrColor(lnrBackground, LOOK.ADVENTWINDOW_BACKGROUNDIMAGE,
                    LOOK.ADVENTWINDOW_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)findViewById(R.id.adventwindow_lblTitle);
            helper.setTextColor(txtTitle,LOOK.ADVENTWINDOW_TITLECOLOR,LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtTitle,LOOK.ADVENTWINDOW_TITLESIZE,LOOK.GLOBAL_TITLESIZE);
            helper.setTextStyle(txtTitle,LOOK.ADVENTWINDOW_TITLESTYLE,LOOK.GLOBAL_TITLESTYLE);
            helper.setTextShadow(txtTitle,LOOK.ADVENTWINDOW_TITLESHADOWCOLOR,LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.ADVENTWINDOW_TITLESHADOWOFFSET,LOOK.GLOBAL_TITLESHADOWOFFSET);

            TextView txtBody = (TextView)findViewById(R.id.adventwindow_lblBody);
            helper.setViewBackgroundImageOrColor(txtBody, LOOK.ADVENTWINDOW_TEXTBACKGROUNDIMAGE,
                    LOOK.ADVENTWINDOW_TEXTBACKGROUNDCOLOR,LOOK.GLOBAL_ALTCOLOR);
            helper.setTextColor(txtBody,LOOK.ADVENTWINDOW_TEXTCOLOR,LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setTextSize(txtBody,LOOK.ADVENTWINDOW_TEXTSIZE,LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtBody,LOOK.ADVENTWINDOW_TEXTSTYLE,LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtBody,LOOK.ADVENTWINDOW_TEXTSHADOWCOLOR,LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.ADVENTWINDOW_TEXTSHADOWOFFSET,LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.adventwindow_flxBackButton);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONBACKGROUNDIMAGE,
                    LOOK.ADVENTWINDOW_BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.setFlexibleButtonImage(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONICON);
            helper.setFlexibleButtonTextColor(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setFlexibleButtonTextSize(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setFlexibleButtonTextStyle(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setFlexibleButtonTextShadow(flxBackButton, LOOK.ADVENTWINDOW_BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.ADVENTWINDOW_BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for AdventWindow page", e);
        }
    }
}