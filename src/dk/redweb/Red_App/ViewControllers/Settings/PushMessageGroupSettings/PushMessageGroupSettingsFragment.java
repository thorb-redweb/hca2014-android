package dk.redweb.Red_App.ViewControllers.Settings.PushMessageGroupSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.PushMessageGroupVM;
import dk.redweb.Red_App.Views.NavBarBox;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 11:57
 */
public class PushMessageGroupSettingsFragment extends BasePageFragment {

    public PushMessageGroupSettingsFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_pushmessagegroupsettings);

        setAppearance();
        setText();

        ListView lstSubscriptions = (ListView)findViewById(R.id.pushmessagegroupsettings_lstpmgs);

        PushMessageGroupVM[] pmgVMs = _db.PushMessageGroups.getAllVMs();
        PushMessageGroupSettingsAdapter lstSessionsAdapter = new PushMessageGroupSettingsAdapter(getActivity(), pmgVMs, _app, _page);
        lstSubscriptions.setAdapter(lstSessionsAdapter);

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        navBarBox.setUpButtonTargetForThisPage(_page);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.pushmessagegroupsettings_lnrMainView);
            helper.setViewBackgroundTileImageOrColor(lnrBackground, LOOK.PUSHMESSAGEGROUPSETTINGS_BACKGROUNDIMAGE, LOOK.PUSHMESSAGEGROUPSETTINGS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView lblTitle = (TextView)findViewById(R.id.pushmessagegroupsettings_lblTitle);
            helper.TextView.setColor(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageDetail", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = new TextHelper(_view, _name, _xml);

            helper.setText(R.id.pushmessagegroupsettings_lblTitle, TEXT.PUSHMESSAGEGROUPSETTINGS_TITLE, DEFAULTTEXT.PUSHMESSAGEGROUPSETTINGS_TITLE);


        } catch (Exception e) {
            MyLog.e("Exception when setting page text", e);
        }
    }
}
