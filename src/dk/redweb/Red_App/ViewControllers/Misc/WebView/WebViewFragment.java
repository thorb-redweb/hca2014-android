package dk.redweb.Red_App.ViewControllers.Misc.WebView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/28/13
 * Time: 10:04
 */
public class WebViewFragment extends BasePageFragment {
    public WebViewFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_webview);

        setAppearance();
        setText();

        WebView webView = (WebView)findViewById(R.id.webview_webBody);
        webView.setWebViewClient(new WebViewClient());
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return checkBackKey(keyCode, event);
            }
        });

        try {
            String pageUrl = _page.getStringFromNode(PAGE.URL);
            webView.loadUrl(pageUrl);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to get url for page", e);
        }

        setupBackButton();

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            RelativeLayout rltBackground = (RelativeLayout)findViewById(R.id.webview_rltMainView);
            helper.setViewBackgroundTileImageOrColor(rltBackground, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.flxBackButton);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.BACKBUTTONBACKGROUNDIMAGE,
                    LOOK.BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.setFlexibleButtonImage(flxBackButton, LOOK.BACKBUTTONICON);
            helper.setFlexibleButtonTextColor(flxBackButton, LOOK.BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setFlexibleButtonTextSize(flxBackButton, LOOK.BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setFlexibleButtonTextStyle(flxBackButton, LOOK.BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setFlexibleButtonTextShadow(flxBackButton, LOOK.BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception in StaticArticle:setAppearance", e);
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

    private boolean checkBackKey(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            goBack();
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }

    @Override
    protected void setupBackButton(){
        try {
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.flxBackButton);
            if(_page.hasChild(PAGE.RETURNBUTTON) && _page.getBoolFromNode(PAGE.RETURNBUTTON)){
                flxBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        WebView webView = (WebView)findViewById(R.id.webview_webBody);
//                        WebBackForwardList list = webView.copyBackForwardList();
//                        MyLog.v(String.valueOf(list.getSize()) + " " + webView.canGoBack());

                        goBack();
                    }
                });
            } else {
                flxBackButton.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting ReturnButton attribute from page xml", e);
        }
    }

    private void goBack(){
        MyLog.v("Back button pressed");
        WebView webView = (WebView)findViewById(R.id.webview_webBody);
        if (webView.canGoBack()){
            webView.goBack();
        }
        else{
            NavController.popPage(getActivity());
        }
    }
}
