package dk.redweb.hca2014.ViewControllers.PushMessages.PushMessageList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewModels.PushMessageVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/23/13
 * Time: 9:14
 */
public class PushMessageListAdapter extends ArrayAdapter<PushMessageVM> {
    private XmlStore _xml;
    private XmlNode _parent;

    private final Context _context;
    private final PushMessageVM[] _values;

    public PushMessageListAdapter(Context context, PushMessageVM[] values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_pushmessagelist, values);
        _context = context;
        _values = values;

        _xml = xml;
        _parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_pushmessagelist, parent, false);

        setAppearance(rowView);

        TextView txtTitle = (TextView)rowView.findViewById(R.id.pushmessagelistitem_lblTitle);
        TextView txtBody = (TextView)rowView.findViewById(R.id.pushmessagelistitem_lblMessage);

        PushMessageVM pushMessage = _values[position];
        txtTitle.setText(pushMessage.Intro());
        txtBody.setText(pushMessage.SendDateWithPattern("yyyy-MM-dd"));

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_parent.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.PUSHMESSAGELIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)lineitem.findViewById(R.id.pushmessagelistitem_lblTitle);
            helper.TextView.setColor(txtTitle, LOOK.PUSHMESSAGELIST_ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.PUSHMESSAGELIST_ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.PUSHMESSAGELIST_ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.PUSHMESSAGELIST_ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGELIST_ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtBody = (TextView)lineitem.findViewById(R.id.pushmessagelistitem_lblMessage);
            helper.TextView.setColor(txtBody, LOOK.PUSHMESSAGELIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.PUSHMESSAGELIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.PUSHMESSAGELIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.PUSHMESSAGELIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGELIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageListItem", e);
        }
    }
}
