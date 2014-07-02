package dk.redweb.hca2014.ViewControllers.Session.SearchSessions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

import java.util.ArrayList;

/**
 * package: dk.redweb.hca2014.ViewControllers.Session.SearchSessions
 * copyright: Copyright (C) 2005 - 2014 redweb ApS. All rights reserved.
 * license: GNU General Public License version 2 or later.
 */
public class SearchSessionsAdapter extends ArrayAdapter<SessionVM> {

    private Context _context;
    private XmlStore _xml;
    private XmlNode _parent;

    public ArrayList<SessionVM> sessions;

    public SearchSessionsAdapter(Context context, ArrayList<SessionVM> values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_dailysessionlist, values);
        _context = context;
        this.sessions = values;
        _xml = xml;
        _parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_dailysessionlist, parent, false);
        TextView txtTimeAndDate = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblTimeAndPlace);
        TextView txtVenueAndEvent = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblEvent);
        TextView txtType = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblType);
        ImageView imgType = (ImageView)rowView.findViewById(R.id.dailysessionlistitem_imgType);
        View vwRightBorder = (View)rowView.findViewById(R.id.dailysessionlistitem_vwRightBorder);


        SessionVM session = sessions.get(position);

        String dateComponent = session.StartDateWithPattern("EEEE dd.");
        dateComponent = dateComponent.substring(0,1).toUpperCase() + dateComponent.substring(1);
        txtTimeAndDate.setText(dateComponent + " kl. " + session.StartTimeAsString());
        txtVenueAndEvent.setText(session.Venue() + "\n" + session.Title());
        txtType.setText(session.Type());

        setAppearance(rowView);

        txtType.setTextColor(session.TypeColor());
        imgType.setImageResource(session.TypeImage());
        vwRightBorder.setBackgroundColor(session.TypeColor());

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_parent.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.DAILYSESSIONLIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTimeAndDate = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblTimeAndPlace);
            TextView txtVenueAndEvent = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblEvent);
            TextView txtType = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblType);
            TextView[] textViews = new TextView[]{txtTimeAndDate,txtVenueAndEvent,txtType};
            helper.TextView.setColor(textViews, LOOK.DAILYSESSIONLIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(textViews, LOOK.DAILYSESSIONLIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(textViews, LOOK.DAILYSESSIONLIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(textViews, LOOK.DAILYSESSIONLIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.DAILYSESSIONLIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:setAppearance", e);
        }
    }
}
