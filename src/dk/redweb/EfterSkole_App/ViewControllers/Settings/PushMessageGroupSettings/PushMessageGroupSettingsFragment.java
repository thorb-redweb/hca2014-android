package dk.redweb.EfterSkole_App.ViewControllers.Settings.PushMessageGroupSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import dk.redweb.EfterSkole_App.AppearanceHelper;
import dk.redweb.EfterSkole_App.MyLog;
import dk.redweb.EfterSkole_App.R;
import dk.redweb.EfterSkole_App.StaticNames.DEFAULTTEXT;
import dk.redweb.EfterSkole_App.StaticNames.LOOK;
import dk.redweb.EfterSkole_App.StaticNames.TEXT;
import dk.redweb.EfterSkole_App.TextHelper;
import dk.redweb.EfterSkole_App.ViewControllers.BasePageFragment;
import dk.redweb.EfterSkole_App.ViewModels.PushMessageGroupVM;
import dk.redweb.EfterSkole_App.Views.NavBarBox;
import dk.redweb.EfterSkole_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 11:57
 */
public class PushMessageGroupSettingsFragment extends BasePageFragment {

    public PushMessageGroupSettingsFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.frag_pushmessagegroupsettings);

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
        navBarBox.setUpButtonTargetForThisPage(_page, null);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.pushmessagegroupsettings_lnrMainView);
            helper.setViewBackgroundTileImageOrColor(lnrBackground, LOOK.PUSHMESSAGEGROUPSETTINGS_BACKGROUNDIMAGE, LOOK.PUSHMESSAGEGROUPSETTINGS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView lblTitle = (TextView)findViewById(R.id.pushmessagegroupsettings_lblTitle);
            helper.setTextColor(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setTextStyle(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setTextShadow(lblTitle, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.PUSHMESSAGEGROUPSETTINGS_TITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageDetail", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = new TextHelper(_view, Name, _xml);

            helper.setText(R.id.pushmessagegroupsettings_lblTitle, TEXT.PUSHMESSAGEGROUPSETTINGS_TITLE, DEFAULTTEXT.PUSHMESSAGEGROUPSETTINGS_TITLE);


        } catch (Exception e) {
            MyLog.e("Exception when setting page text", e);
        }
    }
}
