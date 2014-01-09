package dk.redweb.Red_App.ViewControllers.Misc.ImageUploader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

import java.io.File;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/6/13
 * Time: 13:19
 */
public class ImageUploaderFileBrowserAdapter extends ArrayAdapter<File> {

    private final Context _context;
    private final File[] _values;

    RedEventApplication _app;
    XmlStore _xml;
    XmlNode _page;

    public ImageUploaderFileBrowserAdapter(Context context, File[] values, RedEventApplication app, XmlNode parentPage){
        super(context, R.layout.listitem_imageuploaderfilebrowser, values);
        _context = context;
        _values = values;

        _app = app;
        _xml = _app.getXmlStore();
        _page = parentPage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_imageuploaderfilebrowser, parent, false);

        setAppearance(rowView);

        ImageView imgImage = (ImageView)rowView.findViewById(R.id.imageuploadfilebrowser_imgFileImage);
        TextView txtTitle = (TextView)rowView.findViewById(R.id.imageuploadfilebrowser_lblFileName);

        File file = _values[position];

        Drawable image = My.getDrawableFromDiskWithFilename(file.getPath(), _app, 120, 120, true);
        imgImage.setImageDrawable(image);

        String[] nameBits = file.getName().split("-");

        txtTitle.setText(nameBits[0]+"/"+nameBits[1]+"/"+nameBits[2]+"\n"+nameBits[3]+":"+nameBits[4]+":"+nameBits[5]);

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_page.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_page.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.IMAGEARTICLELIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)lineitem.findViewById(R.id.imageuploadfilebrowser_lblFileName);
            helper.setTextColor(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setTextStyle(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setTextShadow(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception in FileBrowserAdapter:setAppearance", e);
        }
    }
}
