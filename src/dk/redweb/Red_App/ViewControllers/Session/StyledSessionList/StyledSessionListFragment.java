package dk.redweb.Red_App.ViewControllers.Session.StyledSessionList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import org.joda.time.LocalDate;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/14/14
 * Time: 14:42
 */
public class StyledSessionListFragment extends BasePageFragment {

    private ListView _lstSession;
    private LocalDate _dateOfListContent;

    public StyledSessionListFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_styledsessionlist);

        _lstSession = (ListView)findViewById(R.id.styledSessionList_lstSessionTable);

        setupListView();

        setAppearance();
        setText();

        _dateOfListContent = new LocalDate();
        initializeDate();
        reloadListView();

        return _view;
    }

    private void setupListView(){
        _lstSession.setEmptyView(findViewById(R.id.imageArticleList_lnrEmptyList));

        _lstSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = _lstSession.getAdapter();

                SessionVM selectedSession = (SessionVM) adapter.getItem(position);
                try {
                    XmlNode nextPage = _xml.getPage(_childname).deepClone();
                    nextPage.addChildToNode(PAGE.SESSIONID, selectedSession.SessionId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception in StyledSessionListFragment:onClickListener", e);
                }
            }
        });
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = _appearanceHelper;

            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            helper.Shape.setViewBackgroundAsShape(_lstSession, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR, 3, LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR, 7);
        } catch (Exception e) {
            MyLog.e("Exception in UpcomingSessions:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = _textHelper;
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    private void initializeDate() {
        LocalDate earliestDateWithSession = _db.Sessions.getDateForEarliestFromVenueId(-1);
        LocalDate latestDateWithSession = _db.Sessions.getDateForLatestFromVenueId(-1);

        if(_dateOfListContent.isBefore(earliestDateWithSession))
            _dateOfListContent = earliestDateWithSession;
        else if(_dateOfListContent.isAfter(latestDateWithSession))
            _dateOfListContent = latestDateWithSession;
        else {
            _dateOfListContent = _db.Sessions.getDateForNextFromDateAndVenueId(_dateOfListContent, -1);
        }
    }

    private void reloadListView(){
        SessionVM[] sessions = _db.Sessions.getVMListFromDayAndVenueId(_dateOfListContent,-1);
        StyledSessionListAdapter lstSessionsAdapter = new StyledSessionListAdapter(_view.getContext(), sessions, _xml, _page);
        _lstSession.setAdapter(lstSessionsAdapter);
    }
}
