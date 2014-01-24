package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadPictureView;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.Red_App.DatabaseModel.RedUploadImage;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.Model.RedUploadDataStore;
import dk.redweb.Red_App.Model.RedUploadServerFolder;
import dk.redweb.Red_App.My;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.io.File;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/16/14
 * Time: 9:33
 */
public class RedUploadPictureViewFragment extends BasePageFragment {

    private RedUploadImage _redUploadImageObject;
    private RedUploadServerFolder _folder;

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

    public RedUploadPictureViewFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_reduploadpictureview);

        fetchControls();
        setValues();
        setControls();
        setAppearance();
        setText();

        return _view;
    }

    private void fetchControls(){
        _flxBack = (FlexibleButton)findViewById(R.id.reduploadpictureview_flxBack);
        _flxNext = (FlexibleButton)findViewById(R.id.reduploadpictureview_flxNext);
        _lblTitle = (TextView)findViewById(R.id.reduploadpictureview_lblTitle);
        _imgPicture = (ImageView)findViewById(R.id.reduploadpictureview_imgPicture);
        _lnrTxtPictureText = (LinearLayout)findViewById(R.id.reduploadpictureview_lnrTxtPictureText);
        _txtPictureText = (EditText)findViewById(R.id.reduploadpictureview_txtPictureText);
        _rltApprovalBox = (RelativeLayout)findViewById(R.id.reduploadpictureview_rltApprovalBox);
        _lblApprovalStatement = (TextView)findViewById(R.id.reduploadpictureview_lblApprovalStatement);
        _lblApprovalStatus = (TextView)findViewById(R.id.reduploadpictureview_lblApprovalStatus);
        _swcApproval = (Switch)findViewById(R.id.reduploadpictureview_swcApproved);
        _flxApproval = (FlexibleButton)findViewById(R.id.reduploadpictureview_flxApproval);
        _flxDeletePicture = (FlexibleButton)findViewById(R.id.reduploadpictureview_flxDeletePicture);
    }

    private void setValues(){
        try {
            int folderId = _page.getIntegerFromNode(PAGE.REDUPLOADFOLDERID);
            RedUploadDataStore dataStore = _app.getVolatileDataStores().getRedUpload();
            _folder = dataStore.getFolder(folderId);

            if(_page.hasChild(PAGE.FILEPATH)){
                String imagePath = _page.getStringFromNode(PAGE.FILEPATH);
                _redUploadImageObject = _db.RedUpload.getFromImagePath(imagePath);
                if(_redUploadImageObject == null){
                    _redUploadImageObject = new RedUploadImage();
                    _redUploadImageObject.localImagePath = imagePath;
                    _redUploadImageObject.serverFolder =  _folder.getServerFolder();
                    _redUploadImageObject.text = "";
                    _redUploadImageObject.approved = false;
                    _db.RedUpload.createEntry(_redUploadImageObject);
                }
            }
            else {
                _flxNext.setEnabled(false);
                _flxApproval.setEnabled(false);
                _flxDeletePicture.setEnabled(false);
                MyLog.e("No filepath provided for image");
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting values", e);
        }
    }

    private void setControls(){
        _txtPictureText.setText(_redUploadImageObject.text);

        _swcApproval.setEnabled(_redUploadImageObject.approved);
        _swcApproval.setChecked(_redUploadImageObject.approved);

        _flxBack.setOnClickListener(backAction());
        _flxNext.setOnClickListener(nextAction());
        _txtPictureText.addTextChangedListener(textChangedEvent());
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
            helper.setFlexibleButtonText(_flxBack, TEXT.REDUPLOAD_BACKBUTTON, DEFAULTTEXT.REDUPLOAD_BACKBUTTON);
            helper.setFlexibleButtonText(_flxNext, TEXT.REDUPLOAD_NEXTBUTTON, DEFAULTTEXT.REDUPLOAD_NEXTBUTTON);
            _lblTitle.setText(_folder.getName());
            helper.setEditTextHint(_txtPictureText, TEXT.REDUPLOAD_PICTURETEXTHINT, DEFAULTTEXT.REDUPLOAD_PICTURETEXTHINT);
            helper.setText(_lblApprovalStatement, TEXT.REDUPLOAD_APPROVALSTATEMENT, DEFAULTTEXT.REDUPLOAD_APPROVALSTATEMENT);
            helper.setText(_lblApprovalStatus, TEXT.REDUPLOAD_APPROVALSTATUSNO, DEFAULTTEXT.REDUPLOAD_APPROVALSTATUSNO);
            helper.setFlexibleButtonText(_flxApproval, TEXT.REDUPLOAD_APPROVALBUTTON, DEFAULTTEXT.REDUPLOAD_APPROVALBUTTON);
            helper.setFlexibleButtonText(_flxDeletePicture, TEXT.REDUPLOAD_DELETEPICTUREBUTTON, DEFAULTTEXT.REDUPLOAD_DELETEPICTUREBUTTON);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting static text for RedUploadFragment", e);
        }
    }

    public void onResume(){
        super.onResume();

        if(_page.hasChild(PAGE.FILEPATH)){
            try {
                String filepath = _page.getStringFromNode(PAGE.FILEPATH);

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenwidth = size.x;

                _imgPicture.setImageDrawable(My.getDrawableFromDiskWithFilename(filepath, getActivity(), 0, screenwidth, true));

            } catch (NoSuchFieldException e) {
                MyLog.e("Exception when attempting to get FilePath from xml page", e);
            }
        }
    }

    private View.OnClickListener backAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        };
    }

    private void goBack(){
        try {
            if(_page.getBoolWithNoneAsFalseFromNode(PAGE.POPTHRICE)){
                NavController.popThreePages(getActivity());
            }
            else if(_page.getBoolWithNoneAsFalseFromNode(PAGE.POPTWICE)){
                NavController.popTwoPages(getActivity());
            }
            else{
                NavController.popPage(getActivity());
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when doing pop action", e);
        }
    }

    private View.OnClickListener nextAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode nextPage = _xml.getPage(_childname).deepClone();
                    nextPage.addChildToNode(PAGE.REDUPLOADFOLDERID, _folder.getFolderId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when going to next view", e);
                }
            }
        };
    }

    private TextWatcher textChangedEvent() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _redUploadImageObject.text = s.toString();
                _db.RedUpload.updateEntry(_redUploadImageObject);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private View.OnClickListener approveUploadAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!_redUploadImageObject.approved){
                    _swcApproval.setChecked(true);
                    try{
                        _textHelper.setText(_lblApprovalStatus, TEXT.REDUPLOAD_APPROVALSTATUSYES, DEFAULTTEXT.REDUPLOAD_APPROVALSTATUSYES);
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when setting new approvalstatus", e);
                    }
                    try{
                        _textHelper.setFlexibleButtonText(_flxApproval, TEXT.REDUPLOAD_UPLOADBUTTON, DEFAULTTEXT.REDUPLOAD_UPLOADBUTTON);
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when changing text on button to upload status", e);
                    }

                    _redUploadImageObject.approved = false;
                    _db.RedUpload.updateEntry(_redUploadImageObject);
                }
            }
        };
    }

    private View.OnClickListener deleteAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _db.RedUpload.deleteEntryWithImagePath(_redUploadImageObject.localImagePath);

                    String filePath = _page.getStringFromNode(PAGE.FILEPATH);
                    File imageFile = new File(filePath);
                    imageFile.delete();

                    goBack();
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when getting filepath from page", e);
                }
            }
        };
    }
}
