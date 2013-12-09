package dk.redweb.Red_App.ViewControllers.Misc.ImageUploader;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/6/13
 * Time: 12:24
 */
public class ImageUploaderFragment extends BasePageFragment {

    private static final int FILE_SELECT_CODE = 0;

    public ImageUploaderFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_imageuploader);
        _page = _page.deepClone();

        setAppearance();
        setText();

        FlexibleButton flxOpenBrowser = (FlexibleButton)findViewById(R.id.imageuploader_flxOpenBrowser);
        flxOpenBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser();
            }
        });

        return _view;
    }

    public void onResume(){
        super.onResume();

        if(_page.hasChild(PAGE.FILEPATH)){
            try {
                ImageView imgToUpload = (ImageView)findViewById(R.id.imageuploader_imgImageToUpload);

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
        else{
            openBrowser();
        }
    }

    private void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;
            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            FlexibleButton flxBrowser = (FlexibleButton)findViewById(R.id.imageuploader_flxOpenBrowser);
            FlexibleButton flxUpload = (FlexibleButton)findViewById(R.id.imageuploader_flxUploadImage);
            FlexibleButton[] flxButtons = {flxBrowser,flxUpload};
            helper.setViewBackgroundImageOrColor(flxButtons, LOOK.BUTTONBACKGROUNDIMAGE, LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.setFlexibleButtonImage(flxButtons, LOOK.BUTTONICON);
            helper.setFlexibleButtonTextColor(flxButtons, LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setFlexibleButtonTextSize(flxButtons, LOOK.BUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setFlexibleButtonTextStyle(flxButtons, LOOK.BUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setFlexibleButtonTextShadow(flxButtons, LOOK.BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for ImageUploaderFragment", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = _textHelper;
            helper.setFlexibleButtonText(R.id.imageuploader_flxOpenBrowser, TEXT.IMAGEUPLOADER_FILEBROWSERBUTTON, DEFAULTTEXT.IMAGEUPLOADER_FILEBROWSERBUTTON);
            helper.setFlexibleButtonText(R.id.imageuploader_flxUploadImage, TEXT.IMAGEUPLOADER_UPLOADIMAGEBUTTON, DEFAULTTEXT.IMAGEUPLOADER_UPLOADIMAGEBUTTON);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting static text for ImageUploaderFragment", e);
        }
    }

    private void openBrowser(){
        List<XmlNode> nodes = new ArrayList<XmlNode>();
        nodes.add(new XmlNode(PAGE.NAME, _name));
        nodes.add(new XmlNode(PAGE.TYPE, TYPE.FILEBROWSER));
        nodes.add(new XmlNode(PAGE.PARENTPAGE, _page));
        try {
            nodes.add(new XmlNode(PAGE.FOLDER, _page.getStringFromNode(PAGE.FOLDER)));
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting folder from xmlpage", e);
        }

        XmlNode fileBrowser = new XmlNode(PAGE.PAGE,nodes);

        try {
            NavController.changePageWithXmlNode(fileBrowser, getActivity());
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to push filebrowser", e);
        }
    }
}
