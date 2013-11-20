package dk.redweb.Red_App.ViewControllers.Session.SessionDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/20/13
 * Time: 11:50 AM
 */
public class SessionDetailFragment extends BasePageFragment {

    SessionVM _session;

    public SessionDetailFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_sessiondetail);


        int sessionId = 0;
        try {
            sessionId = _page.getIntegerFromNode(PAGE.SESSIONID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting sessionid for page from pagenode", e);
        }

        setAppearance();
        setText();

        _session = _db.Sessions.getVMFromId(sessionId);

        TextView txtTitle = (TextView)findViewById(R.id.sessionDetail_lblTitle);
        ImageView imgView = (ImageView)findViewById(R.id.sessionDetail_imgPicture);
        TextView txtVenue = (TextView)findViewById(R.id.sessionDetail_lblVenueValue);
        TextView txtTime = (TextView)findViewById(R.id.sessionDetail_lblTimeValue);
        TextView txtDate = (TextView)findViewById(R.id.sessionDetail_lblDateValue);
        RelativeLayout rltMapButton = (RelativeLayout)findViewById(R.id.sessionDetail_rltMapButton);
        RelativeLayout rltTicketButton = (RelativeLayout)findViewById(R.id.sessionDetail_rltTicketButton);
        WebView webBody = (WebView)findViewById(R.id.sessionDetail_webBody);
        TextView txtBody = (TextView)findViewById(R.id.sessionDetail_lblBody);

        txtTitle.setText(_session.Title());
        txtDate.setText(_session.StartDateWithPattern("EEEE, dd.MM.yyyy"));
        txtTime.setText(_session.StartTimeAsString() + "-" +_session.EndTimeAsString());
        txtVenue.setText(_session.Venue());

        _net.fetchImageOnThread(_session.ImagePath(), imgView);

        rltMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode nextPage = _xml.getPage(_childname).deepClone();
                    nextPage.addChildToNode(PAGE.SESSIONID, _session.SessionId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception in SessionDetailActivity:onClickListener on rltMapButton", e);
                }
            }
        });
        rltTicketButton.setVisibility(View.GONE);

        try {
            if(_page.hasChild(PAGE.BODYUSESHTML) && _page.getBoolFromNode(PAGE.BODYUSESHTML)) {
                webBody.loadDataWithBaseURL(null, _session.DetailsWithHtml(), "text/html", "UTF-8", null);
                txtBody.setVisibility(View.GONE);
            } else {
                txtBody.setText(_session.DetailsWithoutHtml());
                webBody.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            MyLog.e("Exception in SessionDetailActivity:onCreate when attempting to determine body type (web vs. text)", e);
        }

        setupBackButton();

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

//        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
//        navBarBox.setUpButtonTargetForThisPage(_page, null);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            ScrollView box = (ScrollView)findViewById(R.id.sessionDetail_scrMainView);
            helper.setViewBackgroundColor(box, LOOK.SESSIONDETAIL_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)findViewById(R.id.sessionDetail_lblTitle);
            helper.setTextColor(txtTitle, LOOK.SESSIONDETAIL_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtTitle,LOOK.SESSIONDETAIL_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.setTextStyle(txtTitle, LOOK.SESSIONDETAIL_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.setTextShadow(txtTitle, LOOK.SESSIONDETAIL_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TITLESHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView txtDateLabel = (TextView)findViewById(R.id.sessionDetail_lblDateLabel);
            TextView txtVenueLabel = (TextView)findViewById(R.id.sessionDetail_lblVenueLabel);
            TextView txtTimeLabel = (TextView)findViewById(R.id.sessionDetail_lblTimeLabel);
            TextView[] txtLabels = new TextView[]{txtDateLabel,txtVenueLabel,txtTimeLabel};
            helper.setTextColor(txtLabels, LOOK.SESSIONDETAIL_LABELCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtLabels,LOOK.SESSIONDETAIL_LABELSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setTextStyle(txtLabels, LOOK.SESSIONDETAIL_LABELSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setTextShadow(txtLabels, LOOK.SESSIONDETAIL_LABELSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_LABELSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtDateValue = (TextView)findViewById(R.id.sessionDetail_lblDateValue);
            TextView txtVenueValue = (TextView)findViewById(R.id.sessionDetail_lblVenueValue);
            TextView txtTimeValue = (TextView)findViewById(R.id.sessionDetail_lblTimeValue);
            TextView[] txtValues = new TextView[]{txtDateValue,txtVenueValue,txtTimeValue};
            helper.setTextColor(txtValues, LOOK.SESSIONDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtValues,LOOK.SESSIONDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtValues, LOOK.SESSIONDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtValues, LOOK.SESSIONDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            RelativeLayout lnrButtonMap = (RelativeLayout)findViewById(R.id.sessionDetail_rltMapButton);
            helper.setViewBackgroundColor(lnrButtonMap, LOOK.SESSIONDETAIL_BUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            TextView txtButtonMap = (TextView)findViewById(R.id.sessionDetail_lblButtonMap);
            helper.setTextColor(txtButtonMap, LOOK.SESSIONDETAIL_BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setTextSize(txtButtonMap, LOOK.SESSIONDETAIL_BUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtButtonMap, LOOK.SESSIONDETAIL_BUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtButtonMap, LOOK.SESSIONDETAIL_BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            ImageView imgButtonMap = (ImageView)findViewById(R.id.sessionDetail_imgButtonMap);
            helper.setImageViewImage(imgButtonMap, LOOK.SESSIONDETAIL_MAPBUTTONIMAGE);

            TextView txtBody = (TextView)findViewById(R.id.sessionDetail_lblBody);
            helper.setTextColor(txtBody, LOOK.SESSIONDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtBody, LOOK.SESSIONDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtBody, LOOK.SESSIONDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtBody, LOOK.SESSIONDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

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
            MyLog.e("Exception in SessionDetailActivity:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name,_xml);
            helper.setText(R.id.sessionDetail_lblDateLabel, TEXT.SESSIONDETAIL_DATE, DEFAULTTEXT.SESSIONDETAIL_DATE);
            helper.setText(R.id.sessionDetail_lblVenueLabel, TEXT.SESSIONDETAIL_PLACE, DEFAULTTEXT.SESSIONDETAIL_PLACE);
            helper.setText(R.id.sessionDetail_lblTimeLabel, TEXT.SESSIONDETAIL_TIME, DEFAULTTEXT.SESSIONDETAIL_TIME);
            helper.setText(R.id.sessionDetail_lblButtonMap, TEXT.SESSIONDETAIL_MAPBUTTON, DEFAULTTEXT.SESSIONDETAIL_MAPBUTTON);

            helper.setFlexibleButtonText(R.id.flxBackButton, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }
}