package dk.redweb.Red_App.ViewControllers.Misc.RedUpload;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.My;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.io.File;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/16/14
 * Time: 9:33
 */
public class RedUploadFragment extends BasePageFragment {

    private SessionVM _session;
    private boolean _approved;

    private FlexibleButton _flxBack;
    private FlexibleButton _flxNext;
    private TextView _lblTitle;
    private ImageView _imgPicture;
    private LinearLayout _lnrTxtPictureText;
    private EditText _txtPictureText;
    private RelativeLayout _rltApprovalBox;
    private TextView _lblApprovalStatement;
    private TextView _lblApprovalStatus;
    private Switch _swcApproval;
    private FlexibleButton _flxApproval;
    private FlexibleButton _flxDeletePicture;

    public RedUploadFragment(XmlNode page) {
        super(page);

        _approved = false;
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_redupload);

        try {
            int sessionId = _page.getIntegerFromNode(PAGE.SESSIONID);
            _session = _db.Sessions.getVMFromId(sessionId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        fetchControls();
        setControls();
        setAppearance();
        setText();

        return _view;
    }

    private void fetchControls(){
        _flxBack = (FlexibleButton)findViewById(R.id.redupload_flxBack);
        _flxNext = (FlexibleButton)findViewById(R.id.redupload_flxNext);
        _lblTitle = (TextView)findViewById(R.id.redupload_lblTitle);
        _imgPicture = (ImageView)findViewById(R.id.redupload_imgPicture);
        _lnrTxtPictureText = (LinearLayout)findViewById(R.id.redupload_lnrTxtPictureText);
        _txtPictureText = (EditText)findViewById(R.id.redupload_txtPictureText);
        _rltApprovalBox = (RelativeLayout)findViewById(R.id.redupload_rltApprovalBox);
        _lblApprovalStatement = (TextView)findViewById(R.id.redupload_lblApprovalStatement);
        _lblApprovalStatus = (TextView)findViewById(R.id.redupload_lblApprovalStatus);
        _swcApproval = (Switch)findViewById(R.id.redupload_swcApproved);
        _flxApproval = (FlexibleButton)findViewById(R.id.redupload_flxApproval);
        _flxDeletePicture = (FlexibleButton)findViewById(R.id.redupload_flxDeletePicture);
    }

    private void setControls(){
        _swcApproval.setEnabled(false);
        _swcApproval.setChecked(false);

        _flxBack.setOnClickListener(backAction());
        _flxNext.setOnClickListener(nextAction());
        _flxApproval.setOnClickListener(approveUploadAction());
        _flxDeletePicture.setOnClickListener(deleteAction());
    }

    private void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;
            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            helper.FlexButton.setCustomStyle(_flxBack, "topbutton", LOOK.DEFCOLOR_ALT, LOOK.DEFSIZE_ITEMTITLE);
            helper.FlexButton.setCustomStyle(_flxNext, "topbutton", LOOK.DEFCOLOR_ALT, LOOK.DEFSIZE_ITEMTITLE);

            helper.TextView.setTitleStyle(_lblTitle);

            helper.setViewBackgroundColor(_rltApprovalBox, LOOK.REDUPLOAD_APPROVALBOXCOLOR, LOOK.GLOBAL_ALTCOLOR);

            helper.setViewBackgroundAsShape(_lnrTxtPictureText, "", LOOK.WHITE, 3, "", LOOK.BLACK,10);

            helper.TextView.setAltTextStyle(_lblApprovalStatement);
            helper.TextView.setAltTextStyle(_lblApprovalStatus);

            helper.FlexButton.setCustomStyle(_flxApproval, "bottombutton", LOOK.DEFCOLOR_BACK, LOOK.DEFSIZE_ITEMTITLE);
            helper.FlexButton.setCustomStyle(_flxDeletePicture, "bottombutton", LOOK.DEFCOLOR_BACK, LOOK.DEFSIZE_ITEMTITLE);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for RedUploadFragment", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = _textHelper;
            helper.setFlexibleButtonText(R.id.redupload_flxBack, TEXT.REDUPLOAD_BACKBUTTON, DEFAULTTEXT.REDUPLOAD_BACKBUTTON);
            helper.setFlexibleButtonText(R.id.redupload_flxNext, TEXT.REDUPLOAD_NEXTBUTTON, DEFAULTTEXT.REDUPLOAD_NEXTBUTTON);
            _lblTitle.setText(_session.Title());
            helper.setEditTextHint(R.id.redupload_txtPictureText, TEXT.REDUPLOAD_PICTURETEXTHINT, DEFAULTTEXT.REDUPLOAD_PICTURETEXTHINT);
            helper.setText(R.id.redupload_lblApprovalStatement, TEXT.REDUPLOAD_APPROVALSTATEMENT, DEFAULTTEXT.REDUPLOAD_APPROVALSTATEMENT);
            helper.setText(R.id.redupload_lblApprovalStatus, TEXT.REDUPLOAD_APPROVALSTATUSNO, DEFAULTTEXT.REDUPLOAD_APPROVALSTATUSNO);
            helper.setFlexibleButtonText(R.id.redupload_flxApproval, TEXT.REDUPLOAD_APPROVALBUTTON, DEFAULTTEXT.REDUPLOAD_APPROVALBUTTON);
            helper.setFlexibleButtonText(R.id.redupload_flxDeletePicture, TEXT.REDUPLOAD_DELETEPICTUREBUTTON, DEFAULTTEXT.REDUPLOAD_DELETEPICTUREBUTTON);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting static text for RedUploadFragment", e);
        }
    }

    public void onResume(){
        super.onResume();

        if(_page.hasChild(PAGE.FILEPATH)){
            try {
                ImageView imgToUpload = (ImageView)findViewById(R.id.redupload_imgPicture);

                String filepath = _page.getStringFromNode(PAGE.FILEPATH);

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenwidth = size.x;

                imgToUpload.setImageDrawable(My.getDrawableFromDiskWithFilename(filepath, getActivity(), 0, screenwidth, true));

            } catch (NoSuchFieldException e) {
                MyLog.e("Exception when attempting to get FilePath from xml page", e);
            }
        }
    }

    private View.OnClickListener backAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController.popPage(getActivity());
            }
        };
    }

    private View.OnClickListener nextAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XmlNode nextPage = null;
                try {
                    nextPage = _xml.getPage(_childname).deepClone();
                    nextPage.addChildToNode(PAGE.SESSIONID, _session.SessionId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when ", e);
                }
            }
        };
    }

    private View.OnClickListener approveUploadAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_approved){
                    _approved = true;
                    _swcApproval.setChecked(true);
                    try{
                        _textHelper.setText(R.id.redupload_lblApprovalStatus, TEXT.REDUPLOAD_APPROVALSTATUSYES, DEFAULTTEXT.REDUPLOAD_APPROVALSTATUSYES);
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when setting new approvalstatus", e);
                    }
                    try{
                        _textHelper.setFlexibleButtonText(R.id.redupload_flxApproval, TEXT.REDUPLOAD_UPLOADBUTTON, DEFAULTTEXT.REDUPLOAD_UPLOADBUTTON);
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when changing text on button to upload status", e);
                    }
                }
            }
        };
    }

    private View.OnClickListener deleteAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String filePath = _page.getStringFromNode(PAGE.FILEPATH);
                    File imageFile = new File(filePath);
                    imageFile.delete();
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when getting filepath from page", e);
                }
            }
        };
    }
}
