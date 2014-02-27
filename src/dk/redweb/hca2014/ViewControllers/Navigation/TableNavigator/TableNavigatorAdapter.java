package dk.redweb.hca2014.ViewControllers.Navigation.TableNavigator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/19/13
 * Time: 14:25
 */
public class TableNavigatorAdapter extends ArrayAdapter<XmlNode> {
    private XmlStore _xml;
    private XmlNode _parent;

    private final Context _context;
    private final XmlNode[] _values;
    private final NetworkInterface _net;

    public TableNavigatorAdapter(Context context, RedEventApplication app, XmlNode[] values, XmlNode parent){
        super(context, R.layout.listitem_tablenavigator, values);
        _context = context;
        _values = values;

        _net = app.getNetworkInterface();
        _xml = app.getXmlStore();
        _parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout rowView = (RelativeLayout)inflater.inflate(R.layout.listitem_tablenavigator, parent, false);

        XmlNode entry = _values[position];

        setAppearance(rowView);

        TextView txtTitle = (TextView)rowView.findViewById(R.id.tablenavigator_lblTitle);
        try {
            txtTitle.setText(entry.getStringFromNode(PAGE.NAME));
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to get entry item from entry node", e);
        }

        try{
            ImageView imgFrontIcon = (ImageView)rowView.findViewById(R.id.tablenavigator_imgFrontIcon);
            if(entry.hasChild(PAGE.FRONTICON)){
                Drawable drawable = My.getDrawableFromResourceWithFilename(entry.getStringFromNode(PAGE.FRONTICON), _context);
                imgFrontIcon.setImageDrawable(drawable);
            } else {
                imgFrontIcon.setVisibility(View.GONE);
            }

            ImageView imgBackIcon = (ImageView)rowView.findViewById(R.id.tablenavigator_imgBackIcon);
            if(entry.hasChild(PAGE.BACKICON)){
                Drawable drawable = My.getDrawableFromResourceWithFilename(entry.getStringFromNode(PAGE.BACKICON), _context);
                imgBackIcon.setImageDrawable(drawable);
            } else {
                imgBackIcon.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when attempting to set icon on table entry", e);
        }


        return rowView;
    }

    private void setAppearance(RelativeLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_parent.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
            TextView txtTitle = (TextView)lineitem.findViewById(R.id.tablenavigator_lblTitle);
            helper.TextView.setColor(txtTitle, LOOK.TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.TEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.TEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in TableNavigatorAdapter:setAppearance", e);
        }
    }
}
