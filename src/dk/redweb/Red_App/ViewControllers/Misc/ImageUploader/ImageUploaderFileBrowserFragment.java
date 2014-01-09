package dk.redweb.Red_App.ViewControllers.Misc.ImageUploader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.Red_App.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/6/13
 * Time: 13:13
 */
public class ImageUploaderFileBrowserFragment extends BasePageFragment {

    ListView lstFiles;

    public ImageUploaderFileBrowserFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.fragment_imageuploaderfilebrowser);

        lstFiles = (ListView)findViewById(R.id.imageuploadfilebrowser_lstFiles);

        setupList();
        reloadListView();

        setAppearance();

        return _view;
    }

    private void setupList(){
        lstFiles.setEmptyView(findViewById(R.id.imageArticleList_lnrEmptyList));

        lstFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = lstFiles.getAdapter();

                File selectedItem = (File) adapter.getItem(position);

                try {
                    XmlNode parentPage = (XmlNode)_page.getChildFromNode(PAGE.PARENTPAGE).value();
                    if(parentPage.hasChild(PAGE.FILEPATH)){
                        parentPage.getChildFromNode(PAGE.FILEPATH).setValue(selectedItem.getPath());
                    }
                    else {
                        parentPage.addChildToNode(PAGE.FILEPATH, selectedItem.getPath());
                    }
                    NavController.popPage(getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when attempting to inject file path into parent xml page, and return to parent", e);
                }
            }
        });
    }

    private void reloadListView()
    {
        String foldername = "redApp";
        try {
            foldername = _page.getStringFromNode(PAGE.FOLDER);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to get folder name from page", e);
        }
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername);
        File[] allFiles = folder.listFiles();

        ArrayList<File> imageFiles = new ArrayList<File>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        for (File file : allFiles){
            Bitmap testMap = BitmapFactory.decodeFile(file.getPath(), options);
            if(options.outWidth != -1 && options.outHeight != -1){
                imageFiles.add(file);
            }
        }

        File[] files = new File[imageFiles.size()];
        int i = 0;
        for(int j = imageFiles.size()-1; j >= 0; j--){
            files[i] = imageFiles.get(j);
            i++;
        }

        ImageUploaderFileBrowserAdapter lstFileAdapter = new ImageUploaderFileBrowserAdapter(_view.getContext(), files, _app, _page);
        lstFiles.setAdapter(lstFileAdapter);
    }

    private void setAppearance(){
        AppearanceHelper helper = _appearanceHelper;

        try {
            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting appearance on ImageUploaderFileBrowser", e);
        }


    }
}
