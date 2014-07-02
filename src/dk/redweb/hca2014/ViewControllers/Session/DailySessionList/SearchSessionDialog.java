package dk.redweb.hca2014.ViewControllers.Session.DailySessionList;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.ViewControllers.BaseDialog;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;

/**
 * package: dk.redweb.hca2014.ViewControllers.Session.DailySessionList
 * copyright: Copyright (C) 2005 - 2014 redweb ApS. All rights reserved.
 * license: GNU General Public License version 2 or later.
 */
public class SearchSessionDialog extends BaseDialog {

    DailySessionListFragment _parentFragment;

    EditText _txtSearchField;
    Button _btnSearch;

    String _previousSearch;

    public SearchSessionDialog(DailySessionListFragment fragment, String searchString) {
        super(fragment, R.layout.diag_searchbox);
        _parentFragment = fragment;
        _previousSearch = searchString;

        getControls();
        setContent();
        setAppearance();
        setupControls();
    }

    @Override
    protected void getControls() {
        _txtSearchField = (EditText)findViewById(R.id.diag_txtSearchField);
        _btnSearch = (Button)findViewById(R.id.diag_btnSearch);
    }

    @Override
    protected void setContent() {
        _txtSearchField.setText(_previousSearch);
    }

    @Override
    protected void setAppearance() {
        _btnSearch.setBackgroundColor(getColor(R.color.accent));
        _btnSearch.setTextColor(getColor(R.color.white));
    }

    @Override
    protected void setupControls() {
        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = _txtSearchField.getText().toString();
                _parentFragment.searchForSessions(searchString);
                close();
            }
        });
    }

    public int getColor(int id){
        return getActivity().getResources().getColor(id);
    }
}
