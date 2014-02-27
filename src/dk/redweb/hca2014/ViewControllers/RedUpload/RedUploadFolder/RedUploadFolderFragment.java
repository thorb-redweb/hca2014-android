package dk.redweb.hca2014.ViewControllers.RedUpload.RedUploadFolder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.Model.RedUploadServerFolder;
import dk.redweb.hca2014.Model.RedUploadServerOtherFolder;
import dk.redweb.hca2014.Model.RedUploadServerSessionFolder;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.DEFAULTTEXT;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TEXT;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.XmlHandling.XmlNode;

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
