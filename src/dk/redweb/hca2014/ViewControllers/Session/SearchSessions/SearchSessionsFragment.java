package dk.redweb.hca2014.ViewControllers.Session.SearchSessions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.My;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewControllers.Session.DailySessionList.DailySessionListAdapter;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;

import java.util.ArrayList;

/**
 * package: dk.redweb.hca2014.ViewControllers.Session.SearchSessions
 * copyright: Copyright (C) 2005 - 2014 redweb ApS. All rights reserved.
 * license: GNU General Public License version 2 or later.
 */
public class SearchSessionsFragment extends BasePageFragment {

    EditText _txtSearchField;
    ListView _lstSessions;

    SearchSessionsAdapter _adapter;

    ArrayList<SessionVM> _sessions;

    public SearchSessionsFragment(){
        super(null);
    }

    public SearchSessionsFragment(XmlNode page) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_searchsessions);

        getControls();
        setAppearance();
        setupControls();

        return _view;
    }

    private void getControls(){
        _txtSearchField = (EditText)findViewById(R.id.searchsessions_txtSearchField);
        _lstSessions = (ListView)findViewById(R.id.searchsessions_lstSessions);
    }

    private void setAppearance(){
        try{
        AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);
        helper.setViewBackgroundColor(_view, LOOK.GLOBAL_BACKCOLOR, LOOK.GLOBAL_BACKCOLOR);
        }
        catch (Exception e){
            MyLog.e("Exception in SearchSessions:setAppearance", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        _app.getTabbar().setVisibility(View.VISIBLE);
    }

    private void setupControls(){
        _txtSearchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    _app.getTabbar().setVisibility(View.GONE);
                }
                else{
                    _app.getTabbar().setVisibility(View.VISIBLE);
                }
            }
        });

        _txtSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                _sessions.clear();
                _sessions.addAll(_db.Sessions.searchSessions(_txtSearchField.getText().toString()));
                _adapter.notifyDataSetChanged();
            }
        });

        _sessions = new ArrayList<SessionVM>();
        _adapter = new SearchSessionsAdapter(getActivity(), _sessions, _xml, _page);
        _lstSessions.setAdapter(_adapter);

        _lstSessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = _lstSessions.getAdapter();

                SessionVM selectedSession = (SessionVM) adapter.getItem(position);
                try {
                    XmlNode nextPage = _xml.getPage(_childname).deepClone();
                    nextPage.addChildToNode(PAGE.SESSIONID, selectedSession.SessionId());
                    NavController.changePageWithXmlNode(nextPage, getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception in SearchSessionsFragment:onClickListener", e);
                }
            }
        });
    }
}
