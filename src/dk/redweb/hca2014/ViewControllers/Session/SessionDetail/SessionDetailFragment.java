package dk.redweb.hca2014.ViewControllers.Session.SessionDetail;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.XmlHandling.XmlNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/20/13
 * Time: 11:50 AM
 */
public class SessionDetailFragment extends BasePageFragment {

    SessionVM _session;

    public SessionDetailFragment(){
        super(null);
    }

    public SessionDetailFragment(XmlNode page) {
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
        TextView txtType = (TextView)findViewById(R.id.sessionDetail_lblTypeValue);
        TextView txtVenue = (TextView)findViewById(R.id.sessionDetail_lblVenueValue);
        TextView txtTime = (TextView)findViewById(R.id.sessionDetail_lblTimeValue);
        TextView txtDate = (TextView)findViewById(R.id.sessionDetail_lblDateValue);
        RelativeLayout rltMapButton = (RelativeLayout)findViewById(R.id.sessionDetail_rltMapButton);
        RelativeLayout rltTicketButton = (RelativeLayout)findViewById(R.id.sessionDetail_rltTicketButton);
        LinearLayout lnrCalendarButton = (LinearLayout)findViewById(R.id.sessionDetail_lnrButtonCalendar);
        WebView webBody = (WebView)findViewById(R.id.sessionDetail_webBody);
        TextView txtBody = (TextView)findViewById(R.id.sessionDetail_lblBody);

        txtTitle.setText(_session.Title());
        txtType.setText(_session.Type());
        txtType.setTextColor(_session.TypeColor());
        txtDate.setText(_session.StartDateWithPattern("EEEE' d. 'dd MMM"));
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
        String urlRegex = "\\(?\\bhttp://[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        if(!_session.SubmissionPath().matches(urlRegex)){
            rltTicketButton.setVisibility(View.GONE);
        }
        else{
            rltTicketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_session.SubmissionPath()));
                    startActivity(browserIntent);
                }
            });
        }
        lnrCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, _session.Title());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, _session.StartDateTimeAsCalendar().getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, _session.EndDateTimeAsCalendar().getTimeInMillis());
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, _session.Venue());
                startActivity(calIntent);
            }
        });

        try {
            if(_page.hasChild(PAGE.BODYUSESHTML) && _page.getBoolFromNode(PAGE.BODYUSESHTML)) {
                String htmlString = _xml.css + _session.DetailsWithHtml();
                webBody.loadDataWithBaseURL(_xml.joomlaPath, htmlString, "text/html", "UTF-8", null);
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
            helper.TextView.setColor(txtTitle, LOOK.SESSIONDETAIL_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.SESSIONDETAIL_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.SESSIONDETAIL_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.SESSIONDETAIL_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TITLESHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView txtTypeLabel = (TextView)findViewById(R.id.sessionDetail_lblTypeLabel);
            TextView txtDateLabel = (TextView)findViewById(R.id.sessionDetail_lblDateLabel);
            TextView txtVenueLabel = (TextView)findViewById(R.id.sessionDetail_lblVenueLabel);
            TextView txtTimeLabel = (TextView)findViewById(R.id.sessionDetail_lblTimeLabel);
            TextView[] txtLabels = new TextView[]{txtTypeLabel, txtDateLabel,txtVenueLabel,txtTimeLabel};
            helper.TextView.setColor(txtLabels, LOOK.SESSIONDETAIL_LABELCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtLabels, LOOK.SESSIONDETAIL_LABELSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtLabels, LOOK.SESSIONDETAIL_LABELSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtLabels, LOOK.SESSIONDETAIL_LABELSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_LABELSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtTypeValue = (TextView)findViewById(R.id.sessionDetail_lblTypeValue);
            TextView txtDateValue = (TextView)findViewById(R.id.sessionDetail_lblDateValue);
            TextView txtVenueValue = (TextView)findViewById(R.id.sessionDetail_lblVenueValue);
            TextView txtTimeValue = (TextView)findViewById(R.id.sessionDetail_lblTimeValue);
            TextView[] txtValues = new TextView[]{txtTypeValue,txtDateValue,txtVenueValue,txtTimeValue};
            helper.TextView.setColor(txtValues, LOOK.SESSIONDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtValues, LOOK.SESSIONDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            for(TextView textView : txtValues){
                textView.setTypeface(null, Typeface.BOLD);
            }
            helper.TextView.setShadow(txtValues, LOOK.SESSIONDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            RelativeLayout lnrButtonMap = (RelativeLayout)findViewById(R.id.sessionDetail_rltMapButton);
            helper.setViewBackgroundColor(lnrButtonMap, LOOK.SESSIONDETAIL_BUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            LinearLayout lnrButtonCalendar = (LinearLayout)findViewById(R.id.sessionDetail_lnrButtonCalendar);
            helper.setViewBackgroundColor(lnrButtonCalendar, LOOK.SESSIONDETAIL_BUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            TextView txtButtonMap = (TextView)findViewById(R.id.sessionDetail_lblButtonMap);
            TextView txtTicketMap = (TextView)findViewById(R.id.sessionDetail_lblButtonTicket);
            TextView txtButtonCalendar = (TextView)findViewById(R.id.sessionDetail_lblButtonCalendar);
            TextView[] buttonLabels = new TextView[]{txtButtonMap,txtTicketMap, txtButtonCalendar};
            helper.TextView.setColor(buttonLabels, LOOK.SESSIONDETAIL_BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.TextView.setSize(buttonLabels, LOOK.SESSIONDETAIL_BUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(buttonLabels, LOOK.SESSIONDETAIL_BUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(buttonLabels, LOOK.SESSIONDETAIL_BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            ImageView imgButtonMap = (ImageView)findViewById(R.id.sessionDetail_imgButtonMap);
            helper.setImageViewImage(imgButtonMap, LOOK.SESSIONDETAIL_MAPBUTTONIMAGE);

            TextView txtBody = (TextView)findViewById(R.id.sessionDetail_lblBody);
            helper.TextView.setColor(txtBody, LOOK.SESSIONDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.SESSIONDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.SESSIONDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.SESSIONDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.SESSIONDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

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