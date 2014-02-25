package dk.redweb.Red_App.ViewControllers.SplitView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.Red_App.DatabaseModel.Session;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.Views.NewsTicker;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import org.joda.time.DateTime;

import java.util.Calendar;

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

    public HcaSplitViewFragment(XmlNode page) {
        super(page);
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

                try {
                    NavController.changePageWithXmlNode(_page, context);
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when attempting to change page", e);
                }

            }
        });
    }

    private void setupNewsTicker(){
        newsTicker.SetupNewsticker();
        newsTicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        newsTicker.setClickable(true);

        newsTicker.setFlipInterval(6000);
        newsTicker.setInAnimation(context, R.anim.newsticker_infromright);
        newsTicker.setOutAnimation(context, R.anim.newsticker_outtoleft);
    }

    public void changeSessionList(){
        MyLog.d("Running changeSessionList");
        SessionVM[] sessions = _db.Sessions.getNextThreeVM(new DateTime());
        HcaListViewAdapter lstSessionsAdapter = new HcaListViewAdapter(context, sessions);
        lstSessions.setAdapter(lstSessionsAdapter);
    }

    public void changeNewsTicker(){
        MyLog.d("Running changeEventTicker");
        //todo add real article id
        ArticleVM[] newsData = _db.Articles.getListOfLastThree(12);
        newsTicker.changeContent(newsData, _net);
    }
}
