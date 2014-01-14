package dk.redweb.Red_App.ViewControllers.Misc.CameraIntent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.io.File;
import java.io.IOException;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/6/13
 * Time: 9:33
 */
public class CameraIntentFragment extends BasePageFragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    File folder;
    static Uri capturedImageUri=null;
    String filePath;

    boolean firstVisit;

    public CameraIntentFragment(XmlNode page) {
        super(page);
        firstVisit = true;
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_cameraintent);

        setAppearance();

        if(firstVisit){
            String directoryName = "redApp";
            try {
                if(_page.hasChild(PAGE.FOLDER)){
                    directoryName = _page.getStringFromNode(PAGE.FOLDER);
                }
            } catch (NoSuchFieldException e) {
                MyLog.e("Exception when attempting to create file object from Page.StorageDirectory", e);
            }

            folder = new File(Environment.getExternalStorageDirectory()+File.separator+directoryName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = DateTimeNow().toString("yyyy-MM-dd-hh-mm-ss-SSS");
            File file = new File(folder.getAbsolutePath(), fileName + ".jpg");

            if(file.exists()){
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                MyLog.e("Exception when attempting to create file in memory", e);
            }
            filePath = file.getPath();

            capturedImageUri = Uri.fromFile(file);
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
            startActivityForResult(i, CAMERA_PIC_REQUEST);
        }
        else{
            pushToNextPage();
        }

        return _view;
    }

    private void setAppearance(){
        AppearanceHelper helper = _appearanceHelper;
        try {
            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView lblRedirectionMessage = (TextView)findViewById(R.id.cameraIntent_lblRedirectionMessage);
            helper.TextView.setTextColor(lblRedirectionMessage, LOOK.TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(lblRedirectionMessage, LOOK.TEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setTextStyle(lblRedirectionMessage, LOOK.TEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setTextShadow(lblRedirectionMessage, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to set appearance for CameraIntent", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_CANCELED)
        {
            NavController.popPage(getActivity());
        }
        else {
            pushToNextPage();
        }
    }

    private void pushToNextPage(){
        try {
            if(firstVisit){
                XmlNode nextpage = _xml.getPage(_childname);
                nextpage = nextpage.deepClone();
                nextpage.addChildToNode(PAGE.FILEPATH, filePath);
                NavController.changePageWithXmlNode(nextpage, getActivity());
                firstVisit = false;
            }
            else{
                NavController.popPage(getActivity());
            }
        } catch (Exception e) {
            MyLog.e("Exception when changing page to childpage", e);
        }
    }


}
