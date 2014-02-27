package dk.redweb.hca2014.ViewControllers.RedUpload.RedUploadFolderContent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import dk.redweb.hca2014.DatabaseModel.RedUploadImage;
import dk.redweb.hca2014.My;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.XmlHandling.XmlNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/23/14
 * Time: 16:00
 */
public class RedUploadFolderContentAdapter extends ArrayAdapter<RedUploadImage> {
    private XmlNode _parentPage;
    Map<Integer, Drawable> _thumbnails;

    private final FragmentActivity _activity;
    private final RedUploadImage[] _values;
    public ArrayList<Integer> _selectedItems;

    public RedUploadFolderContentAdapter(FragmentActivity activity, RedUploadImage[] values, XmlNode parentPage){
        super(activity, R.layout.listitem_reduploadfoldercontent, values);
        _activity = activity;
        _values = values;
        _selectedItems = new ArrayList<Integer>();

        _parentPage = parentPage;

        _thumbnails = new HashMap<Integer, Drawable>();
    }

    private Drawable getThumbnailForRow(int row){
        if(_thumbnails.containsKey(row)){
            return _thumbnails.get(row);
        }

        RedUploadImage redUploadImage = _values[row];

        Drawable image = My.getDrawableFromDiskWithFilename(redUploadImage.localImagePath, _activity, 150, 0, true);

        _thumbnails.put(row,image);
        return image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RedUploadFolderContentItem item = (RedUploadFolderContentItem)inflater.inflate(R.layout.listitem_reduploadfoldercontent, parent, false);

        item.initializeItem(_values[position], getThumbnailForRow(position), _parentPage, _activity);

        return item;
    }

    @Override
    public int getCount(){
        return _values.length;
    }

    public void selectItem(Integer position, View view){
        if(_selectedItems.contains(position)){
            ((RedUploadFolderContentItem)view).setDeselected();
            _selectedItems.remove(position);
        }
        else {
            ((RedUploadFolderContentItem)view).setSelected();
            _selectedItems.add(position);
        }
    }

    public RedUploadImage getSelectedItem(){
        if(_selectedItems.size() == 1){
            return getItem(_selectedItems.get(0));
        }
        return null;
    }
}
