package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import dk.redweb.Red_App.Model.RedUploadServerSessionFolder;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 12:45
 */
public class RedUploadSessionFolderAdapter extends ArrayAdapter<RedUploadServerSessionFolder> {
    private XmlNode _parentPage;

    private final FragmentActivity _activity;
    private final RedUploadServerSessionFolder[] _values;

    public RedUploadSessionFolderAdapter(FragmentActivity activity, RedUploadServerSessionFolder[] values, XmlNode parentPage){
        super(activity, R.layout.listitem_reduploadsessionfolder, values);
        _activity = activity;
        _values = values;
        _parentPage = parentPage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RedUploadSessionFolderItem item = (RedUploadSessionFolderItem)inflater.inflate(R.layout.listitem_reduploadsessionfolder, parent, false);

        item.initializeItem(_values[position], _parentPage, _activity);

        return item;
    }

    @Override
    public int getCount(){
        return _values.length;
    }
}
