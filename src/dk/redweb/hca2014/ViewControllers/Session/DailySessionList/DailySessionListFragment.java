package dk.redweb.hca2014.ViewControllers.Session.DailySessionList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.DatabaseModel.Venue;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 3:13 PM
 */
public class DailySessionListFragment extends BasePageFragment {
    private Spinner _spnType;
    private Spinner _spnVenue;
    private ListView _lstSession;

    private LocalDate _dateOfListContent;
    private LocalDate _earliestDateWithSession;
    private LocalDate _latestDateWithSession;

    String typeSpinnerTitle = "Sorter på type";
    String venueSpinnerTitle = "Sorter på sted";

    private String _filterType;
    private Venue _filterVenue;

    private int getFilterVenueId(){
        if(_filterVenue != null)
            return _filterVenue.VenueId;
        return -1;
    }

    public DailySessionListFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_dailysessionlist);

        _spnType = (Spinner)findViewById(R.id.dailysessionlist_spnType);
        _spnVenue = (Spinner)findViewById(R.id.dailysessionlist_spnVenue);
        _lstSession = (ListView)findViewById(R.id.dailysessionlist_lstSessions);

        setupDateArrows();
        setupSpinner();
        setupListView();

        setAppearance();
        setText();

        _dateOfListContent = new LocalDate();
        if(_app.isDebugging()){
            _dateOfListContent = _app.getDebugCurrentDate().toLocalDate();
        }

        initializeDate();
        reloadListView();

        return _view;
    }

    private void setupDateArrows(){
        final ImageView backArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateBack);
        final ImageView forwardArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateForward);

        backArrow.setOnClickListener(LastDateOnClickListener());

        forwardArrow.setOnClickListener(NextDateOnClickListener());
    }

    private View.OnClickListener LastDateOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dateOfListContent = _db.Sessions.getDateForLastFromDateAndVenueId(_dateOfListContent, getFilterVenueId());
                reloadListView();
            }
        };
    }

    private View.OnClickListener NextDateOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dateOfListContent = _db.Sessions.getDateForNextFromDateAndVenueId(_dateOfListContent, getFilterVenueId());
                reloadListView();
            }
        };
    }

    private void setupSpinner(){
        Spinner typeSpinner = (Spinner)findViewById(R.id.dailysessionlist_spnType);
        final String[] types = new String[]{"Underholdning og teater", "Leg og læring", "Musik", "Kulturformidling", "Kunst og kultur", "Spoken word"};
        loadSpinnerData(typeSpinner, types, typeSpinnerTitle);
        Spinner venueSpinner = (Spinner)findViewById(R.id.dailysessionlist_spnVenue);
        final String[] venues = _db.Venues.getAllActiveNames();
        loadSpinnerData(venueSpinner, venues, venueSpinnerTitle);

        typeSpinner.setOnItemSelectedListener(new TypeSpinnerListener());

        venueSpinner.setOnItemSelectedListener(new VenueSpinnerListener());
    }

    private void loadSpinnerData(Spinner spinner, String[] rawData, String title)
    {
        String[] data = new String[rawData.length+1];
        System.arraycopy(rawData, 0, data, 1, rawData.length);
        data[0] = title;

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(_view.getContext(), android.R.layout.simple_spinner_item, data);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
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
                    NavController.changePageWithXmlNode(nextPage,getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception in DailySessionListActivity:onClickListener", e);
                }
            }
        });
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout selectorLayout = (LinearLayout)findViewById(R.id.dailysessionlist_lnrSelectorLayout);
            helper.setViewBackgroundColor(selectorLayout, LOOK.DAILYSESSIONLIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            FrameLayout listboxLayout = (FrameLayout)findViewById(R.id.dailysessionlist_frmSessionlistlayout);
            helper.setViewBackgroundColor(listboxLayout, LOOK.DAILYSESSIONLIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            helper.setListViewBackgroundColor(_lstSession, LOOK.DAILYSESSIONLIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            LinearLayout titleUnderline = (LinearLayout)findViewById(R.id.dailysessionlist_lnrDateUnderline);
            helper.setViewBackgroundColor(titleUnderline, LOOK.DAILYSESSIONLIST_TITLEUNDERLINECOLOR, LOOK.GLOBAL_ALTCOLOR);

            final ImageView backArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateBack);
            helper.setImageViewImage(backArrow, LOOK.DAILYSESSIONLIST_LEFTARROW);
            final ImageView forwardArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateForward);
            helper.setImageViewImage(forwardArrow, LOOK.DAILYSESSIONLIST_RIGHTARROW);

            TextView title = (TextView)findViewById(R.id.dailysessionlist_lblDate);
            helper.TextView.setColor(title, LOOK.DAILYSESSIONLIST_DATETEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(title, LOOK.DAILYSESSIONLIST_DATETEXTSIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setStyle(title, LOOK.DAILYSESSIONLIST_DATETEXTSTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setShadow(title, LOOK.DAILYSESSIONLIST_DATETEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.DAILYSESSIONLIST_DATETEXTSHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);
        } catch (Exception e) {
            MyLog.e("Exception in UpcomingSessions:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name, _xml);
            helper.setText(R.id.dailysessionlist_lblEmptyList, TEXT.DAILYSESSIONLIST_EMPTYLIST, DEFAULTTEXT.DAILYSESSIONLIST_EMPTYLIST);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    private void initializeDate() {
        LocalDate earliestDateWithSession = _db.Sessions.getDateForEarliestFromVenueId(getFilterVenueId());
        LocalDate latestDateWithSession = _db.Sessions.getDateForLatestFromVenueId(getFilterVenueId());

        if(earliestDateWithSession == null || latestDateWithSession == null){
            _earliestDateWithSession = _dateOfListContent;
            _latestDateWithSession =  _dateOfListContent;
            return;
        }

        _earliestDateWithSession = earliestDateWithSession;
        _latestDateWithSession = latestDateWithSession;

        if(_dateOfListContent.isBefore(_earliestDateWithSession))
            _dateOfListContent = _earliestDateWithSession;
        else if(_dateOfListContent.isAfter(_latestDateWithSession))
            _dateOfListContent = _latestDateWithSession;
        else if(_db.Sessions.isDateSessionless(_dateOfListContent, getFilterVenueId())){
            _dateOfListContent = _db.Sessions.getDateForNextFromDateAndVenueId(_dateOfListContent, getFilterVenueId());
        }
    }

    private void reloadListView(){
        setDateLabel();
        SessionVM[] sessions = _db.Sessions.getVMListFromDayAndVenueId(_dateOfListContent,getFilterVenueId(),_filterType);
        DailySessionListAdapter lstSessionsAdapter = new DailySessionListAdapter(_view.getContext(), sessions, _xml, _page);
        _lstSession.setAdapter(lstSessionsAdapter);
    }

    private void setDateLabel()
    {
        Locale locale = new Locale("da_DK", "da_DK");
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("EEE.' D. 'dd MMM").withLocale(locale);
        String labelDate = dateFormatter.print(_dateOfListContent);

        ((TextView) findViewById(R.id.dailysessionlist_lblDate)).setText(labelDate);

        final ImageView backArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateBack);
        final ImageView forwardArrow = (ImageView)findViewById(R.id.dailysessionlist_imgDateForward);

        //Check whether forwardbutton should be shown
        LocalDate previousDay = _dateOfListContent.minusDays(1);
        if (previousDay.isBefore(_earliestDateWithSession)){
            backArrow.setVisibility(View.INVISIBLE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }

        //Check whether backwardsbutton should be shown
        LocalDate forwardDay = _dateOfListContent.plusDays(1);
        if (forwardDay.isAfter(_latestDateWithSession)){
            forwardArrow.setVisibility(View.INVISIBLE);
        } else {
            forwardArrow.setVisibility(View.VISIBLE);
        }
    }

    class VenueSpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String venueName = _spnVenue.getSelectedItem().toString();
            if(venueName.equals(venueSpinnerTitle))
                _filterVenue = null;
            else
                _filterVenue = _db.Venues.getFromName(venueName);

            initializeDate();
            reloadListView();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            MyLog.v("DailySessionListActivity's onNothingSelected method was called");
        }
    }

    class TypeSpinnerListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String typeName = _spnType.getSelectedItem().toString();
            if(typeName.equals(typeSpinnerTitle))
                _filterType = null;
            else
                _filterType = typeName;

            initializeDate();
            reloadListView();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            MyLog.v("DailySessionListActivity's onNothingSelected method was called");
        }
    }
}