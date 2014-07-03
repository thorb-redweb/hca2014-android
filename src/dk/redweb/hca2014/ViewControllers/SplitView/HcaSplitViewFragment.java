package dk.redweb.hca2014.ViewControllers.SplitView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.hca2014.My;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.Views.NewsTicker;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import org.joda.time.DateTime;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 2/25/14
 * Time: 12:59
 */
public class HcaSplitViewFragment extends BasePageFragment {

    FragmentActivity context;
    GestureDetector gestureDetector;

    ListView lstSessions;
    NewsTicker newsTicker;

    public HcaSplitViewFragment(){
        super(null);
    }

    public HcaSplitViewFragment(XmlNode page) {
        super(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        My.saveXmlPageInBundle(_page, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(_page == null){
            _page = My.loadXmlPageFromBundle(savedInstanceState);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_hcasplitview);

        lstSessions = (ListView)findViewById(R.id.hcasplitview_lstArrangements);
        newsTicker = (NewsTicker)findViewById(R.id.hcasplitview_nwsTicker);

        context = this.getActivity();

        gestureDetector = new GestureDetector(context, newsTicker);

        setupSessionList();
        setupNewsTicker();
        changeSessionList();
        changeNewsTicker();

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();
        newsTicker.startFlipping();
    }

    private void setupSessionList(){
        lstSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.d("Executing lstSessions.setOnItemClickListener");
                ListAdapter adapter = lstSessions.getAdapter();
                SessionVM selectedSession = (SessionVM) adapter.getItem(position);
                XmlNode childPage = null;
                try {
                    childPage = _xml.getPage(_childname).deepClone();
                } catch (Exception e) {
                    MyLog.e("Exception when getting childpage from xmlStore", e);
                }
                try {
                    childPage.addChildToNode(PAGE.SESSIONID, selectedSession.SessionId());
                } catch (InvalidPropertiesFormatException e) {
                    MyLog.e("Exception when setting sessionid on childpage", e);
                }
                try {
                    NavController.changePageWithXmlNode(childPage, getActivity());
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when pushing to childpage with navcontroller", e);
                }

                try {
                    NavController.changePageWithXmlNode(childPage, context);
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when attempting to change page", e);
                }

            }
        });
    }

    private void setupNewsTicker(){
        XmlNode newsChildPage = null;
        try {
            String child2name = _page.getStringFromNode(PAGE.CHILD2);
            newsChildPage = _xml.getPage(child2name);
        } catch (Exception e) {
            MyLog.e("Exception when getting news child page", e);
        }
        newsTicker.SetupNewsticker(getActivity(), newsChildPage);
        newsTicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        newsTicker.setClickable(true);

        newsTicker.setFlipInterval(12000);
        newsTicker.setInAnimation(context, R.anim.newsticker_infromright);
        newsTicker.setOutAnimation(context, R.anim.newsticker_outtoleft);
    }

    public void changeSessionList(){
        MyLog.d("Running changeSessionList");
        DateTime now = new DateTime();
        if(_app.isDebugging()) now = _app.getDebugCurrentDate();
        SessionVM[] sessions = _db.Sessions.getNextThreeVM(now);
        HcaListViewAdapter lstSessionsAdapter = new HcaListViewAdapter(context, sessions);
        lstSessions.setAdapter(lstSessionsAdapter);
    }

    public void changeNewsTicker(){
        MyLog.d("Running changeEventTicker");
        //todo add real article id
        ArticleVM[] newsData = _db.Articles.getListOfLastThree();
        newsTicker.changeContent(newsData, _net);
    }
}
