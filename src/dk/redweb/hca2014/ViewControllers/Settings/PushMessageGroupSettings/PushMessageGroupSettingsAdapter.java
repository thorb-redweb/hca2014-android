package dk.redweb.hca2014.ViewControllers.Settings.PushMessageGroupSettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Interfaces.Delegate_PushPMGroupSubscriptionUpdate;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewModels.PushMessageGroupVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 14:04
 */
public class PushMessageGroupSettingsAdapter extends ArrayAdapter<PushMessageGroupVM> implements Delegate_PushPMGroupSubscriptionUpdate {

    DbInterface _db;
    ServerInterface _sv;
    Context _context;

    XmlStore _xml;
    XmlNode _parent;

    public final PushMessageGroupVM[] pmgroups;

    public PushMessageGroupSettingsAdapter(Context context, PushMessageGroupVM[] values, RedEventApplication app, XmlNode parent) {
        super(context, R.layout.listitem_pushmessagegroupsettings, values);
        _db = app.getDbInterface();
        _sv = app.getServerInterface();
        _xml = app.getXmlStore();
        _context = context;

        _parent = parent;
        pmgroups = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_pushmessagegroupsettings, parent, false);
        TextView lblGroupName = (TextView)rowView.findViewById(R.id.pushmessagegroupsettinglistitem_lblGroupName);
        final CheckBox chkBox = (CheckBox)rowView.findViewById(R.id.pushmessagegroupsettinglistitem_chkGroupSubscribed);

        PushMessageGroupVM group = pmgroups[position];
        lblGroupName.setText(group.Name());
        chkBox.setChecked(group.Subscribing());

        setAppearance(rowView);

        final int groupid = pmgroups[position].GroupId();
        final Delegate_PushPMGroupSubscriptionUpdate delegate = this;
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkBox.isChecked())
                    chkBox.setChecked(false);
                else
                    chkBox.setChecked(true);
            }
        });
        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _sv.pushPMGroupSubscriptionUpdate(delegate, groupid, isChecked);
                _db.PushMessageGroups.updateSubscriptionByGroupId(groupid, isChecked);
            }
        });

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_parent.hasChild(PAGE.NAME))
            {
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            }
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            FrameLayout lnrBox = (FrameLayout)lineitem.findViewById(R.id.pushmessagegroupsettinglistitem_rltItemBox);
            helper.setViewBackgroundColor(lnrBox, LOOK.PUSHMESSAGEGROUPSETTINGS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtGroupName = (TextView)lineitem.findViewById(R.id.pushmessagegroupsettinglistitem_lblGroupName);
            helper.TextView.setColor(txtGroupName, LOOK.PUSHMESSAGEGROUPSETTINGS_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtGroupName, LOOK.PUSHMESSAGEGROUPSETTINGS_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtGroupName, LOOK.PUSHMESSAGEGROUPSETTINGS_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtGroupName, LOOK.PUSHMESSAGEGROUPSETTINGS_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.PUSHMESSAGEGROUPSETTINGS_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for a pushmessagegroupsettings listitem", e);
        }
    }

    @Override
    public void errorOccured(String errorMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
