package dk.redweb.hca2014.ViewControllers.Session.DailySessionList;

import android.content.Context;
import android.graphics.Color;
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

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/20/13
 * Time: 11:40 AM
 */
public class DailySessionListAdapter  extends ArrayAdapter<SessionVM> {

    private Context _context;
    private XmlStore _xml;
    private XmlNode _parent;

    public final SessionVM[] sessions;

    public DailySessionListAdapter(Context context, SessionVM[] values, XmlStore xml, XmlNode parent){
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
        TextView txtTimeAndPlace = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblTimeAndPlace);
        TextView txtEvent = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblEvent);
        TextView txtType = (TextView)rowView.findViewById(R.id.dailysessionlistitem_lblType);
        ImageView imgType = (ImageView)rowView.findViewById(R.id.dailysessionlistitem_imgType);
        View vwRightBorder = (View)rowView.findViewById(R.id.dailysessionlistitem_vwRightBorder);


        SessionVM session = sessions[position];

        String timeAndPlace = session.StartTimeAsString() + "-" + session.EndTimeAsString() + " - " + session.Venue();
        txtTimeAndPlace.setText(timeAndPlace);
        txtEvent.setText(session.Title());
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

            TextView txtTimeAndPlace = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblTimeAndPlace);
            TextView txtEvent = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblEvent);
            TextView txtType = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblType);
            TextView[] textViews = new TextView[]{txtTimeAndPlace,txtEvent,txtType};
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
