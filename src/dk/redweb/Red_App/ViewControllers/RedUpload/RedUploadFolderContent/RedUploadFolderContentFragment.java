package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolderContent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import dk.redweb.Red_App.DatabaseModel.RedUploadImage;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/23/14
 * Time: 15:25
 */
public class RedUploadFolderContentFragment extends BasePageFragment {

    RedUploadServerFolder _folder;
    RedUploadImage[] _datasource;
    RedUploadFolderContentAdapter _adapter;

    FlexibleButton _flxBack;
    FlexibleButton _flxiNext;
    ImageView _imgNext;
    TextView _lblTitle;
    GridView _grdContent;
    FlexibleButton _flxUpload;
    FlexibleButton _flxEdit;

    public RedUploadFolderContentFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_reduploadfoldercontent);

        fetchControls();
        setValues();
        setControls();
        setAppearance();
        setText();

        return _view;
    }

    private void fetchControls() {
        _flxBack = (FlexibleButton)findViewById(R.id.reduploadfoldercontent_flxBack);
        _flxiNext = (FlexibleButton)findViewById(R.id.reduploadfoldercontent_flxiNext);
        _imgNext = (ImageView)_flxiNext.findViewById(R.id.flexibleButton_imgButton);
        _lblTitle = (TextView)findViewById(R.id.reduploadfoldercontent_lblTitle);
        _grdContent = (GridView)findViewById(R.id.reduploadfoldercontent_grdContent);
        _flxUpload = (FlexibleButton)findViewById(R.id.reduploadfoldercontent_flxUpload);
        _flxEdit = (FlexibleButton)findViewById(R.id.reduploadfoldercontent_flxEdit);
    }

    private void setValues(){
        try {
            int folderId = _page.getIntegerFromNode(PAGE.REDUPLOADFOLDERID);
            _folder = _app.getVolatileDataStores().getRedUpload().getFolder(folderId);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting RedUploadServerFolder", e);
        }

        refreshGrid();
    }

    private void setControls() {
        _flxBack.setOnClickListener(backAction());
        _flxiNext.setOnClickListener(nextAction());

        _grdContent.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        _grdContent.setOnItemClickListener(gridItemSelected());
    }

    private void setAppearance() {
        try{
            AppearanceHelper helper = _appearanceHelper;

            FlexibleButton[] topButtons = {_flxBack, _flxiNext};
            helper.Shape.setViewBackgroundAsShape(topButtons, "topbuttonbackgroundcolor", LOOK.GLOBAL_BACKCOLOR, "borderwidth", "topbuttonbordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            helper.FlexButton.setTextColor(topButtons, "topbuttontextcolor", LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.FlexButton.setTextSize(topButtons, "topbuttontextsize", LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(topButtons, "topbuttontextstyle", LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(topButtons, "topbuttontextshadowcolor", LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, "topbuttontextshadowoffset", LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            helper.TextView.setTitleStyle(_lblTitle);

            helper.FlexButton.setImage(_flxiNext, "camerabuttonicon");
            helper.setViewBackgroundColor(_imgNext, "camerabuttoniconcolor", LOOK.GLOBAL_ALTCOLOR);

            helper.FlexButton.setCustomStyle(_flxUpload, "uploadbutton", LOOK.DEFCOLOR_BACK, LOOK.DEFSIZE_ITEMTITLE);
            helper.Shape.setViewBackgroundAsShapeWithGradiant(_flxUpload, "uploadbuttonbackgroundcolor", LOOK.GLOBAL_BACKCOLOR, "uploadbuttonbackgroundcolor2", LOOK.GLOBAL_BACKCOLOR, "borderwidth", "uploadbuttonbordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            _flxUpload.setMinimumHeight(80);
            _flxUpload.setTextColor(Color.parseColor("#666666"));

            helper.FlexButton.setCustomStyle(_flxEdit, "editbutton", LOOK.DEFCOLOR_BACK, LOOK.DEFSIZE_ITEMTITLE);
        helper.Shape.setViewBackgroundAsShapeWithGradiant(_flxEdit, "editbuttonbackgroundcolor", LOOK.GLOBAL_BACKCOLOR, "editbuttonbackgroundcolor2", LOOK.GLOBAL_BACKCOLOR, "borderwidth", "editbuttonbordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            _flxEdit.setMinimumHeight(80);
            _flxEdit.setTextColor(Color.parseColor("#666666"));

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for RedUploadContentFragment", e);
        }

    }

    private void setText() {
        try{
            TextHelper helper = _textHelper;
            helper.setFlexibleButtonText(_flxBack, TEXT.REDUPLOAD_BACKBUTTON, DEFAULTTEXT.REDUPLOAD_BACKBUTTON);
            helper.setFlexibleButtonText(_flxiNext, TEXT.REDUPLOAD_TOPBUTTON, DEFAULTTEXT.REDUPLOAD_TOPBUTTON);
            helper.setText(_lblTitle, "title", "title");
            helper.setFlexibleButtonText(_flxUpload, TEXT.REDUPLOAD_UPLOADBUTTON, DEFAULTTEXT.REDUPLOAD_UPLOADBUTTON);
            helper.setFlexibleButtonText(_flxEdit, TEXT.REDUPLOAD_EDITBUTTON, DEFAULTTEXT.REDUPLOAD_EDITBUTTON);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting text for RedUplodFolderContentFragment", e);
        }
    }

    private void refreshGrid(){
        _datasource = _db.RedUpload.getFromServerFolder(_folder.getServerFolder());
        _adapter = new RedUploadFolderContentAdapter(getActivity(), _datasource, _page);
        _grdContent.setAdapter(_adapter);
    }

    private AdapterView.OnItemClickListener gridItemSelected(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _adapter.selectItem(position, view);

                _flxUpload.setTextColor(Color.parseColor("#666666"));
                _flxEdit.setTextColor(Color.parseColor("#666666"));
                _flxUpload.setOnClickListener(null);
                _flxEdit.setOnClickListener(null);

                if(_adapter._selectedItems.size() == 1){
                    try{
                        _flxUpload.setTextColor(_appearanceHelper.Getter.getColor("uploadbuttontextcolor",LOOK.GLOBAL_BACKTEXTCOLOR));
                        _flxEdit.setTextColor(_appearanceHelper.Getter.getColor("editbuttontextcolor",LOOK.GLOBAL_BACKTEXTCOLOR));
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when setting appearance for buttons", e);
                    }
                    _flxUpload.setOnClickListener(uploadAction());
                    _flxEdit.setOnClickListener(editAction());
                }
                else { //size greater than 1
                    try{
                        _flxUpload.setTextColor(_appearanceHelper.Getter.getColor("uploadbuttontextcolor",LOOK.GLOBAL_BACKTEXTCOLOR));
                    } catch (NoSuchFieldException e) {
                        MyLog.e("Exception when setting appearance for edit button", e);
                    }
                    _flxUpload.setOnClickListener(uploadAction());
                }

                for (Integer selectedIndex : _adapter._selectedItems){
                    RedUploadImage redUploadImage = _datasource[selectedIndex];
                    if (!redUploadImage.approved)
                    {
                        _flxUpload.setTextColor(Color.parseColor("#666666"));
                        _flxUpload.setOnClickListener(null);
                        break;
                    }
                }
            }
        };
    }

    private View.OnClickListener backAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavController.popPage(getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when going to next view", e);
                }
            }
        };
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

    private View.OnClickListener uploadAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    private View.OnClickListener editAction(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode nextPage = _xml.getPage(_page.getStringFromNode(PAGE.CHILD2)).deepClone();
                    nextPage.addChildToNode(PAGE.FILEPATH, _adapter.getSelectedItem().localImagePath);
                    nextPage.addChildToNode(PAGE.REDUPLOADFOLDERID, _folder.getFolderId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when trying to start up edit page", e);
                }
            }
        };
    }
}
