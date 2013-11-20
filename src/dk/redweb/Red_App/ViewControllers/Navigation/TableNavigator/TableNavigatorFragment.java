package dk.redweb.Red_App.ViewControllers.Navigation.TableNavigator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.Red_App.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/19/13
 * Time: 14:25
 */
public class TableNavigatorFragment extends BasePageFragment{
    ListView lstEntries;

    public TableNavigatorFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_tablenavigator);
        setAppearance();

        lstEntries = (ListView)findViewById(R.id.tablenavigator_lstEntries);

        setupNewsList();
        reloadListView();

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            RelativeLayout mainViewLayout = (RelativeLayout)findViewById(R.id.tablenavigator_rltMainView);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
        } catch (Exception e) {
            MyLog.e("Exception in TableNavigatorFragment:setAppearance", e);
        }
    }

    private void setupNewsList(){
        lstEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = lstEntries.getAdapter();

                XmlNode tableEntry = (XmlNode) adapter.getItem(position);
                try {
                    XmlNode page = _xml.getPage(tableEntry.getStringFromNode(PAGE.NAME));
                    NavController.changePageWithXmlNode(page, getActivity());
                } catch (Exception e) {
                    MyLog.e("Executing TableNavigatorFragment:onClickListener", e);
                }
            }
        });
    }

    private void reloadListView()
    {
        XmlNode[] tableEntries = new XmlNode[0];

        tableEntries = _page.getAllChildNodesWithName(PAGE.ENTRY);

        TableNavigatorAdapter lstArticlesAdapter = new TableNavigatorAdapter(_view.getContext(), _app, tableEntries, _page);
        lstEntries.setAdapter(lstArticlesAdapter);
    }
}
