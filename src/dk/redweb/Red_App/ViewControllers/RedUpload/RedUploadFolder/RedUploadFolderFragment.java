package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.Model.RedUploadServerFolder;
import dk.redweb.Red_App.Model.RedUploadServerOtherFolder;
import dk.redweb.Red_App.Model.RedUploadServerSessionFolder;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:40
 */
public class RedUploadFolderFragment extends BasePageFragment {
    private TextView _lblTitle;
    private ListView _lstTable;

    private RedUploadServerFolder[] _datasource;
    private String _folderstype;

    public RedUploadFolderFragment(XmlNode page) {
        super(page);

        try{
            _folderstype = _page.getStringFromNode(PAGE.REDUPLOADFOLDERTYPE);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting redupload type", e);
        }
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_reduploadfolder);



        fetchControls();
        setValues();
        setControls();
        setAppearance();
        setText();

        return _view;
    }

    private void fetchControls() {
        _lblTitle = (TextView)findViewById(R.id.reduploadfolder_lblTitle);
        _lstTable = (ListView)findViewById(R.id.reduploadfolder_lstTable);

        _lstTable.setDivider(null);
        _lstTable.setDividerHeight(0);
    }

    private void setValues(){
        refreshList();
    }

    private void setControls() {

    }

    private void setAppearance() {
        try{
            AppearanceHelper helper = _appearanceHelper;

            helper.setViewBackgroundImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            helper.TextView.setTitleStyle(_lblTitle);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting appearance for RedUploadFolderFragment", e);
        }
    }

    private void setText() {
        try{
            TextHelper helper = _textHelper;
            helper.setText(_lblTitle, TEXT.REDUPLOADFOLDER_TITLE, DEFAULTTEXT.REDUPLOADFOLDER_TITLE);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting text for RedUploadFolderFragment", e);
        }
    }

    private void refreshList(){
        ArrayAdapter adapter = null;
        if(_folderstype.equals(PAGE.REDUPLOADFOLDERTYPESESSION)){
            _datasource = _app.getVolatileDataStores().getRedUpload().getSessionFolders();
            adapter = new RedUploadSessionFolderAdapter(getActivity(), (RedUploadServerSessionFolder[])_datasource, _page);
        }
        else if(_folderstype.equals(PAGE.REDUPLOADFOLDERTYPEOTHER)){
            _datasource = _app.getVolatileDataStores().getRedUpload().getOtherFolders();
            adapter = new RedUploadOtherFolderAdapter(getActivity(), (RedUploadServerOtherFolder[])_datasource, _page);
        }
        _lstTable.setAdapter(adapter);
    }
}
