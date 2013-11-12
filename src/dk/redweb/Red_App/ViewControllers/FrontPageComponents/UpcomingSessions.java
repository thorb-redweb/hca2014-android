package dk.redweb.Red_App.ViewControllers.FrontPageComponents;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.Session.SessionDetail.SessionDetailActivity;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 2:25 PM
 */
public class UpcomingSessions extends LinearLayout{

    DbInterface _db;
    XmlStore _xml;
    XmlNode _page;

    String _name;
    String _childname;
    Activity _frontpageactivity;

    ListView _listview;

    public UpcomingSessions(Activity frontpageactivity, XmlNode page) {
        super(frontpageactivity);
        _frontpageactivity = frontpageactivity;
        _page = page;
        try {
            _name = _page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in UpcommingSessionsAdapter:Constructor when getting component name from xml", e);
        }
        try {
            _childname = _page.getStringFromNode(PAGE.CHILD);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception in UpcomingSessions:Constructor when getting child name from xmlPage", e);
        }

        RedEventApplication app = (RedEventApplication)frontpageactivity.getApplicationContext();
        _db = app.getDbInterface();
        _xml = app.getXmlStore();

        DateTime currentDateTime = new DateTime();
        if(app.isDebugging())
            currentDateTime = app.getDebugCurrentDate();

        LayoutInflater inflater = LayoutInflater.from(frontpageactivity);
        inflater.inflate(R.layout.fp_upcomingsessions, this);

        _listview = (ListView)findViewById(R.id.upcomingsessions_lstArrangements);


        SessionVM[] sessions = _db.Sessions.getNextThreeVM(currentDateTime);
        UpcommingSessionsAdapter lstSessionsAdapter = new UpcommingSessionsAdapter(_frontpageactivity, sessions, _xml, _page);
        _listview.setAdapter(lstSessionsAdapter);

        setAppearance();
        setText();

        _listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = _listview.getAdapter();

                SessionVM selectedSession = (SessionVM) adapter.getItem(position);
                try {
                    XmlNode selectedPage = _xml.getPage(_childname);
                    Intent detailIntent = new Intent(view.getContext(), SessionDetailActivity.class);
                    detailIntent.putExtra(EXTRA.SESSIONID, selectedSession.SessionId());
                    detailIntent.putExtra(EXTRA.PAGE, selectedPage);
                    _frontpageactivity.startActivity(detailIntent);
                } catch (Exception e) {
                    MyLog.e("Exception in UpcomingSessions:onClickListener", e);
                }
            }
        });
    }

    private void setAppearance(){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_name))
                localLook = _xml.getAppearanceForPage(_name);
            AppearanceHelper helper = new AppearanceHelper(localLook, globalLook);

            LinearLayout box = (LinearLayout)findViewById(R.id.upcomingsessions_box);
            helper.setViewBackgroundColor(box, LOOK.UPCOMINGSESSIONS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
            helper.setListViewBackgroundColor(_listview, LOOK.UPCOMINGSESSIONS_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            LinearLayout titleUnderline = (LinearLayout)findViewById(R.id.upcomingsessions_lnrTxtTitle);
            helper.setViewBackgroundColor(titleUnderline, LOOK.UPCOMINGSESSIONS_TITLEUNDERLINECOLOR, LOOK.GLOBAL_ALTCOLOR);

            TextView title = (TextView)findViewById(R.id.upcomingsessions_lblTitle);
            helper.setTextColor(title, LOOK.UPCOMINGSESSIONS_TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(title, LOOK.UPCOMINGSESSIONS_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.setTextStyle(title, LOOK.UPCOMINGSESSIONS_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.setTextShadow(title, LOOK.UPCOMINGSESSIONS_TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.UPCOMINGSESSIONS_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in UpcomingSessions:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(this, _name,_xml);
            helper.setText(R.id.upcomingsessions_lblTitle, TEXT.UPCOMINGSESSIONS_TITLE, DEFAULTTEXT.UPCOMINGSESSIONS_TITLE);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }
}
