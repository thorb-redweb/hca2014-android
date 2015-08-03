package dk.redweb.hca2014.ViewControllers.Article.ArticleDetail;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.My;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.cssfile;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 9:53 AM
 */
public class ArticleDetailFragment extends BasePageFragment {

    public ArticleDetailFragment(){
        super(null);
    }

    public ArticleDetailFragment(XmlNode page) {
        super(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        My.saveXmlPageInBundle(_page, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(_page == null){
            _page = My.loadXmlPageFromBundle(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_articledetail);

        int articleId = 0;
        try {
            articleId = _page.getIntegerFromNode(EXTRA.ARTICLEID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting articleId for new ArticleDetail fragment from page xml", e);
        }

        setAppearance();
        setText();

        ArticleVM article = _db.Articles.getVMFromId(articleId);

        try {
            if(_page.hasChild(PAGE.RETURNONTAP) && _page.getBoolFromNode(PAGE.RETURNONTAP)){
                LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.articleDetail_lnrMainView);
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

        TextView title = (TextView)findViewById(R.id.articleDetail_lblTitle);
        if(article.ArticleId() != -1){
            title.setText(article.Title());

            ImageView imgMain = (ImageView)findViewById(R.id.articledetail_imgMain);
            WebView webBody = (WebView)findViewById(R.id.articleDetail_webBody);
            TextView txtBody = (TextView)findViewById(R.id.articleDetail_lblBody);

            _net.fetchImageOnThread(article.MainImagePath(), imgMain);
            try {
                if(_page.hasChild(PAGE.BODYUSESHTML) && _page.getBoolFromNode(PAGE.BODYUSESHTML)) {
                    String htmlString = _xml.css + article.FullTextWithHtml();
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                        htmlString = "<html><head><style>img {max-width: 100%; width:auto; height: auto;}</style></head><body>"+htmlString+"</body></html>";
                    }
                    else {
                        webBody.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    }
                    webBody.loadDataWithBaseURL(_xml.joomlaPath, htmlString, "text/html", "UTF-8", null);
                    txtBody.setVisibility(View.GONE);
                } else {
                    txtBody.setText(article.FullTextWithoutHtml());
                    webBody.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                MyLog.e("Exception in ArticleDetailActivity:onCreate when attempting to determine body type (web vs. text)", e);
            }
        } else {
            title.setText("Sorry.\r\nThis article has been removed.");
        }

        setupBackButton();

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        navBarBox.setUpButtonTargetForThisPage(_page);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);


            ScrollView mainViewLayout = (ScrollView)findViewById(R.id.sessionDetail_scrMainView);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.ARTICLEDETAIL_BACKGROUNDIMAGE, LOOK.ARTICLEDETAIL_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)findViewById(R.id.articleDetail_lblTitle);
            helper.TextView.setColor(txtTitle, LOOK.ARTICLEDETAIL_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.ARTICLEDETAIL_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.ARTICLEDETAIL_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.ARTICLEDETAIL_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.ARTICLEDETAIL_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

            TextView txtBody = (TextView)findViewById(R.id.articleDetail_lblBody);
            helper.TextView.setColor(txtBody, LOOK.ARTICLEDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.ARTICLEDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.ARTICLEDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.ARTICLEDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.ARTICLEDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.flxBackButton);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.BACKBUTTONBACKGROUNDIMAGE,
                    LOOK.BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxBackButton, LOOK.BACKBUTTONICON);
            helper.FlexButton.setTextColor(flxBackButton, LOOK.BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxBackButton, LOOK.BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxBackButton, LOOK.BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxBackButton, LOOK.BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in ArticleDetailActivity:setAppearance", e);
        }
    }

    private void setText() {
        try{
            TextHelper helper = new TextHelper(_view, _name, _xml);

            helper.setFlexibleButtonText(R.id.flxBackButton, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting text for BaseMapFragment", e);
        }
    }
}
