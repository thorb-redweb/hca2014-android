package dk.redweb.hca2014.ViewControllers.PushMessages.PushMessageDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.PushMessageVM;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/22/13
 * Time: 16:09
 */
public class PushMessageDetailFragment extends BasePageFragment {

    public PushMessageDetailFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_pushmessagedetail);

        int messageId = 0;
        try {
            messageId = _page.getIntegerFromNode(PAGE.PUSHMESSAGEID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting pushmessageId for new PushMessageDetail fragment from page xml", e);
        }

        setAppearance();

        PushMessageVM messageVM = _db.PushMessages.getVMFromId(messageId);

        try {
            if(_page.hasChild(PAGE.RETURNONTAP) && _page.getBoolFromNode(PAGE.RETURNONTAP)){
                LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.pushmessagedetail_lnrMainView);
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

        TextView txtTitle = (TextView)findViewById(R.id.pushmessagedetail_lblTitle);
        txtTitle.setText(messageVM.Intro());

        TextView txtAuthor = (TextView)findViewById(R.id.pushmessagedetail_lblAuthor);
        txtAuthor.setText(messageVM.Author());

        TextView txtDate = (TextView)findViewById(R.id.pushmessagedetail_lblDate);
        txtDate.setText(messageVM.SendDateWithPattern("yyyy-MM-dd"));

        TextView txtBody = (TextView)findViewById(R.id.pushmessagedetail_lblBody);
        txtBody.setText(messageVM.Message());

        try {
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.pushmessagedetail_flxBackButton);
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

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        navBarBox.setUpButtonTargetForThisPage(_page);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);


            ScrollView mainViewLayout = (ScrollView)findViewById(R.id.pushmessagedetail_scrMainView);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.PUSHMESSAGEDETAIL_BACKGROUNDIMAGE, LOOK.PUSHMESSAGEDETAIL_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)findViewById(R.id.pushmessagedetail_lblTitle);
            helper.TextView.setColor(txtTitle, LOOK.PUSHMESSAGEDETAIL_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.PUSHMESSAGEDETAIL_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.PUSHMESSAGEDETAIL_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.PUSHMESSAGEDETAIL_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEDETAIL_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

            TextView txtAuthor = (TextView)findViewById(R.id.pushmessagedetail_lblAuthor);
            helper.TextView.setColor(txtAuthor, LOOK.PUSHMESSAGEDETAIL_AUTHORTEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtAuthor, LOOK.PUSHMESSAGEDETAIL_AUTHORTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtAuthor, LOOK.PUSHMESSAGEDETAIL_AUTHORTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtAuthor, LOOK.PUSHMESSAGEDETAIL_AUTHORTEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEDETAIL_AUTHORTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtDate = (TextView)findViewById(R.id.pushmessagedetail_lblDate);
            helper.TextView.setColor(txtDate, LOOK.PUSHMESSAGEDETAIL_SENTTEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtDate, LOOK.PUSHMESSAGEDETAIL_SENTTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtDate, LOOK.PUSHMESSAGEDETAIL_SENTTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtDate, LOOK.PUSHMESSAGEDETAIL_SENTTEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEDETAIL_SENTTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtBody = (TextView)findViewById(R.id.pushmessagedetail_lblBody);
            helper.TextView.setColor(txtBody, LOOK.PUSHMESSAGEDETAIL_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.PUSHMESSAGEDETAIL_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.PUSHMESSAGEDETAIL_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.PUSHMESSAGEDETAIL_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEDETAIL_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.pushmessagedetail_flxBackButton);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONBACKGROUNDIMAGE,
                    LOOK.PUSHMESSAGEDETAIL_BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONICON);
            helper.FlexButton.setTextColor(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxBackButton, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEDETAIL_BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageDetail", e);
        }
    }
}
