package dk.redweb.Red_App.ViewControllers.PushMessages.PushMessageList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.PushMessageVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/23/13
 * Time: 9:14
 */
public class PushMessageListFragment  extends BasePageFragment {

    ListView lstMessages;

    public PushMessageListFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_pushmessagelist);
        setAppearance();
        setText();

        lstMessages = (ListView)findViewById(R.id.pushmessagelist_lstPushMessages);

        setupList();
        setupListDatasourceAndAdapter();

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            FrameLayout mainViewLayout = (FrameLayout)findViewById(R.id.pushmessagelist_frmList);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.PUSHMESSAGELIST_BACKGROUNDIMAGE, LOOK.PUSHMESSAGELIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageList fragment", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name,_xml);
            helper.setText(R.id.pushmessagelist_lblEmptyList, TEXT.PUSHMESSAGELIST_EMPTYLIST, DEFAULTTEXT.PUSHMESSAGELIST_EMPTYLIST);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    private void setupList(){
        lstMessages.setEmptyView(findViewById(R.id.pushmessagelist_lnrEmptyList));

        lstMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = lstMessages.getAdapter();

                PushMessageVM selectedMessage = (PushMessageVM) adapter.getItem(position);
                try {
                    XmlNode selectedPage = _xml.getPage(_childname);
                    XmlNode childPage = selectedPage.deepClone();
                    childPage.addChildToNode(PAGE.PUSHMESSAGEID, String.valueOf(selectedMessage.PushMessageId()));

                    Fragment pageFragment = NavController.createPageFragmentFromPage(childPage);
                    changePageTo(pageFragment);
                } catch (Exception e) {
                    MyLog.e("Exception on clicking PushMessageList Item, to change page", e);
                }
            }
        });
    }

    private void setupListDatasourceAndAdapter()
    {
        PushMessageVM[] pushMessages = new PushMessageVM[0];
        try {
            if(_page.hasChild(PAGE.PUSHGROUPIDS) && _page.hasChild(PAGE.SUBSCRIPTIONS) && _page.getBoolFromNode(PAGE.SUBSCRIPTIONS))
            {
                pushMessages = _db.PushMessages.getVMListFromGroupIds(_page.getIntegerArrayFromNode(PAGE.PUSHGROUPIDS));
            } else if(_page.hasChild(PAGE.PUSHGROUPIDS))
            {
                pushMessages = _db.PushMessages.getVMListFromGroupIds(_page.getIntegerArrayFromNode(PAGE.PUSHGROUPIDS));
            } else {
                pushMessages = _db.PushMessages.getVMListFromSubscribedGroups();
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting pushmessage list from pushgroupid", e);
        }
        PushMessageListAdapter lstPushMessageAdapter = new PushMessageListAdapter(_view.getContext(), pushMessages, _xml, _page);
        lstMessages.setAdapter(lstPushMessageAdapter);
    }
}
