package dk.redweb.Red_App.ViewControllers.Misc.CameraIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/6/13
 * Time: 9:33
 */
public class CameraIntentFragment extends BasePageFragment {
    private static final int CAMERA_PIC_REQUEST = 1337;
    File folder;
    static Uri capturedImageUri=null;

    boolean firstVisit;

    public CameraIntentFragment(XmlNode page) {
        super(page);
        firstVisit = true;
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_cameraintent);

        if(firstVisit){
            Calendar cal = Calendar.getInstance();

            String directoryName = "redApp";
            try {
                if(_page.hasChild(PAGE.STORAGEDIRECTORY)){
                    directoryName = _page.getStringFromNode(PAGE.STORAGEDIRECTORY);
                }
            } catch (NoSuchFieldException e) {
                MyLog.e("Exception when attempting to create file object from Page.StorageDirectory", e);
            }

            folder = new File(Environment.getExternalStorageDirectory()+File.separator+directoryName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder.getAbsolutePath(),  (cal.getTimeInMillis()+".jpg"));

            if(file.exists()){
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                MyLog.e("Exception when attempting to create file in memory", e);
            }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pushToNextPage();
    }

    private void pushToNextPage(){
        try {
            if(firstVisit){
                XmlNode nextpage = _xml.getPage(_childname);
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
