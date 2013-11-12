package dk.redweb.Red_App.ViewControllers.FrontPageComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StringUtils;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 2:40 PM
 */
public class UpcommingSessionsAdapter extends ArrayAdapter<SessionVM> {

    private XmlStore _xml;
    private XmlNode _parent;

    private String _name;

    public final SessionVM[] sessions;

    public UpcommingSessionsAdapter(Context context, SessionVM[] values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_upcomingsessions, values);
        this.sessions = values;
        _xml = xml;
        _parent = parent;
        try {
            _name = _parent.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:Constructor when getting component name from xml", e);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_upcomingsessions, parent, false);
        TextView txtDateAndTime = (TextView)rowView.findViewById(R.id.upcomingsessionsitem_lblDateAndTime);
        TextView txtPlaceAndEvent = (TextView)rowView.findViewById(R.id.upcomingsessionsitem_lblPlaceAndEvent);

        setAppearance(rowView);

        SessionVM session = sessions[position];
        String dateString = StringUtils.capitalizeAll(session.StartDateWithPattern("MMMM dd,"));
        String dateAndTime = dateString + " " + session.StartTimeAsString() + "-" + session.EndTimeAsString();
        if(!(session.StartDate().isEqual(session.EndDate()))){
            String startDate = StringUtils.capitalizeAll(session.StartDateWithPattern("MMM dd,"));
            String endDate = StringUtils.capitalizeAll(session.EndDateWithPattern("MMM dd,"));
            dateAndTime = startDate + " " + session.StartTimeAsString() + "-" + endDate + " " + session.EndTimeAsString();
        }
        txtDateAndTime.setText(dateAndTime);
        txtPlaceAndEvent.setText(session.Title() + " v/ " + session.Venue());

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_name))
                localLook = _xml.getAppearanceForPage(_name);
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.UPCOMINGSESSIONS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView placeAndEvent = (TextView)lineitem.findViewById(R.id.upcomingsessionsitem_lblPlaceAndEvent);
            helper.setTextColor(placeAndEvent, LOOK.UPCOMINGSESSIONS_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(placeAndEvent, LOOK.UPCOMINGSESSIONS_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(placeAndEvent, LOOK.UPCOMINGSESSIONS_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(placeAndEvent, LOOK.UPCOMINGSESSIONS_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.UPCOMINGSESSIONS_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView dateAndTime = (TextView)lineitem.findViewById(R.id.upcomingsessionsitem_lblDateAndTime);
            helper.setTextColor(dateAndTime, LOOK.UPCOMINGSESSIONS_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(dateAndTime, LOOK.UPCOMINGSESSIONS_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(dateAndTime, LOOK.UPCOMINGSESSIONS_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(dateAndTime, LOOK.UPCOMINGSESSIONS_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.UPCOMINGSESSIONS_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:setAppearance", e);
        }
    }
}
