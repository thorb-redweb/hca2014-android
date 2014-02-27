package dk.redweb.hca2014.ViewControllers.Venue.VenueDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.VenueVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/10/13
 * Time: 1:20 PM
 */
public class VenueDetailFragment extends BasePageFragment {

    private VenueVM _venue;

    public VenueDetailFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_venuedetail);

        navbarSetup();

        int venueId = 0;
        try {
            venueId = _page.getIntegerFromNode(PAGE.VENUEID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting venueid from page xml for VenueDetail", e);
        }

        setAppearance();
        setText();

        _venue = _db.Venues.getVMFromId(venueId);

        TextView txtTitle = (TextView)findViewById(R.id.venueDetail_lblTitle);
        ImageView imgView = (ImageView)findViewById(R.id.venueDetail_imgPicture);
        TextView txtAddress = (TextView)findViewById(R.id.venueDetail_lblAddressValue);
        RelativeLayout rltMapButton = (RelativeLayout)findViewById(R.id.venueDetail_rltMapButton);
        WebView webBody = (WebView)findViewById(R.id.venueDetail_webBody);
        TextView txtBody = (TextView)findViewById(R.id.venueDetail_lblBody);

        txtTitle.setText(_venue.Name());
        txtAddress.setText(_venue.Street() + ", " + _venue.City());

        _net.fetchImageOnThread(_venue.ImagePath(), imgView);

        rltMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode selectedPage = _xml.getPage(_childname);
                    XmlNode childPage = selectedPage.deepClone();
                    childPage.addChildToNode(EXTRA.VENUEID, String.valueOf(_venue.VenueId()));

                    Fragment pageFragment = NavController.createPageFragmentFromPage(childPage);
                    changePageTo(pageFragment);

//                    Intent detailIntent = new Intent(v.getContext(), VenueMapActivity.class);
//                    detailIntent.putExtra(EXTRA.VENUEID, _venue.VenueId());
//                    XmlNode selectedPage = _xml.getPage(_childname);
//                    detailIntent.putExtra(EXTRA.PAGE, selectedPage);
//                    _activity.startActivity(detailIntent);
                } catch (Exception e) {
                    MyLog.e("Exception in onClick on the map button", e);
                }
            }
        });

        try {
            if(_page.hasChild(PAGE.BODYUSESHTML) && _page.getBoolFromNode(PAGE.BODYUSESHTML)) {
                webBody.loadDataWithBaseURL(null, _venue.DescriptionWithHtml(), "text/html", "UTF-8", null);
                txtBody.setVisibility(View.GONE);
            } else {
                txtBody.setText(_venue.DescriptionWithoutHtml());
                webBody.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            MyLog.e("Exception in SessionDetailActivity:onCreate when attempting to determine body type (web vs. text)", e);
        }

        return _view;
    }

    private void navbarSetup(){
//        NavBarBox navBarBox = (NavBarBox)findViewById(R.id.navbar);
//        navBarBox.setUpButtonTargetForThisPage(_page,null);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            ScrollView box = (ScrollView)findViewById(R.id.venueDetail_scrMainView);
            helper.setViewBackgroundTileImageOrColor(box, LOOK.VENUEDETAIL_BACKGROUNDIMAGE, LOOK.VENUEDETAIL_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)findViewById(R.id.venueDetail_lblTitle);
            helper.TextView.setColor(txtTitle, LOOK.TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.TITLESHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView txtAddressLabel = (TextView)findViewById(R.id.venueDetail_lblAddressLabel);
            helper.TextView.setColor(txtAddressLabel, LOOK.VENUEDETAIL_LABELCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtAddressLabel, LOOK.VENUEDETAIL_LABELSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtAddressLabel, LOOK.VENUEDETAIL_LABELSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtAddressLabel, LOOK.VENUEDETAIL_LABELSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.VENUEDETAIL_LABELSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtAddressValue = (TextView)findViewById(R.id.venueDetail_lblAddressValue);
            helper.TextView.setColor(txtAddressValue, LOOK.VENUEDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtAddressValue, LOOK.VENUEDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtAddressValue, LOOK.VENUEDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtAddressValue, LOOK.VENUEDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.VENUEDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            RelativeLayout lnrButtonMap = (RelativeLayout)findViewById(R.id.venueDetail_rltMapButton);
            helper.setViewBackgroundColor(lnrButtonMap, LOOK.VENUEDETAIL_BUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            TextView txtButtonMap = (TextView)findViewById(R.id.venueDetail_lblButtonMap);
            helper.TextView.setColor(txtButtonMap, LOOK.VENUEDETAIL_BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.TextView.setSize(txtButtonMap, LOOK.VENUEDETAIL_BUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtButtonMap, LOOK.VENUEDETAIL_BUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtButtonMap, LOOK.VENUEDETAIL_BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.VENUEDETAIL_BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            ImageView imgButtonMap = (ImageView)findViewById(R.id.venueDetail_imgButtonMap);
            helper.setImageViewImage(imgButtonMap, LOOK.VENUEDETAIL_BUTTONICON);

            TextView txtBody = (TextView)findViewById(R.id.venueDetail_lblBody);
            helper.TextView.setColor(txtBody, LOOK.VENUEDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.VENUEDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.VENUEDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.VENUEDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.VENUEDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in SessionDetailActivity:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name, _xml);
            helper.setText(R.id.venueDetail_lblAddressLabel, TEXT.VENUEDETAIL_ADDRESS, DEFAULTTEXT.VENUEDETAIL_ADDRESS);
            helper.setText(R.id.venueDetail_lblButtonMap, TEXT.VENUEDETAIL_MAPBUTTON, DEFAULTTEXT.VENUEDETAIL_MAPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }
}