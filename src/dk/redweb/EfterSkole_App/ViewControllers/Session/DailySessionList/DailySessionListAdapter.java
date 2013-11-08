package dk.redweb.EfterSkole_App.ViewControllers.Session.DailySessionList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.EfterSkole_App.AppearanceHelper;
import dk.redweb.EfterSkole_App.MyLog;
import dk.redweb.EfterSkole_App.R;
import dk.redweb.EfterSkole_App.StaticNames.LOOK;
import dk.redweb.EfterSkole_App.StaticNames.PAGE;
import dk.redweb.EfterSkole_App.ViewModels.SessionVM;
import dk.redweb.EfterSkole_App.XmlHandling.XmlNode;
import dk.redweb.EfterSkole_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/20/13
 * Time: 11:40 AM
 */
public class DailySessionListAdapter  extends ArrayAdapter<SessionVM> {

    private XmlStore _xml;
    private XmlNode _parent;

    public final SessionVM[] sessions;

    public DailySessionListAdapter(Context context, SessionVM[] values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_dailysessionlist, values);
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

        SessionVM session = sessions[position];
        String timeAndPlace = session.StartTimeAsString() + "-" + session.EndTimeAsString() + " - " + session.Venue();
        txtTimeAndPlace.setText(timeAndPlace);
        txtEvent.setText(session.Title());

        setAppearance(rowView);

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.DAILYSESSIONLIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTimeAndPlace = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblTimeAndPlace);
            helper.setTextColor(txtTimeAndPlace, LOOK.DAILYSESSIONLIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtTimeAndPlace, LOOK.DAILYSESSIONLIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtTimeAndPlace, LOOK.DAILYSESSIONLIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtTimeAndPlace, LOOK.DAILYSESSIONLIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.DAILYSESSIONLIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView txtEvent = (TextView)lineitem.findViewById(R.id.dailysessionlistitem_lblEvent);
            helper.setTextColor(txtEvent, LOOK.DAILYSESSIONLIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(txtEvent, LOOK.DAILYSESSIONLIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(txtEvent, LOOK.DAILYSESSIONLIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(txtEvent, LOOK.DAILYSESSIONLIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.DAILYSESSIONLIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:setAppearance", e);
        }
    }
}
