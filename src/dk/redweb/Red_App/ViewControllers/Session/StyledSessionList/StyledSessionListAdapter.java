package dk.redweb.Red_App.ViewControllers.Session.StyledSessionList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/14/14
 * Time: 15:02
 */
public class StyledSessionListAdapter extends ArrayAdapter<SessionVM> {
    private Context _context;
    private XmlStore _xml;
    private XmlNode _parent;

    public final SessionVM[] sessions;

    public StyledSessionListAdapter(Context context, SessionVM[] values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_styledsessionlist, values);
        _context = context;
        this.sessions = values;
        _xml = xml;
        _parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_styledsessionlist, parent, false);
        TextView lblTime = (TextView)rowView.findViewById(R.id.styledSessionList_lblTime);
        TextView lblBody = (TextView)rowView.findViewById(R.id.styledSessionList_lblBody);

        SessionVM session = sessions[position];
        String time = "Kl. " + session.StartTimeAsString() + " - " + session.EndTimeAsString();
        lblTime.setText(time);
        lblBody.setText(session.Title());

        setAppearance(rowView);

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_parent.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            TextView lblTime = (TextView)lineitem.findViewById(R.id.styledSessionList_lblTime);
            helper.TextView.setCustomTextStyle(lblTime,"itemtitle",LOOK.DEFCOLOR_ALT, LOOK.DEFSIZE_ITEMTITLE);
            TextView lblBody = (TextView)lineitem.findViewById(R.id.styledSessionList_lblBody);
            helper.TextView.setCustomTextStyle(lblBody,"itembody",LOOK.DEFCOLOR_ALT, LOOK.DEFSIZE_TEXT);
        } catch (Exception e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:setAppearance", e);
        }
    }
}
