package dk.redweb.Red_App.ViewControllers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolder.RedUploadSessionFolderItem;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 15:23
 */
public class BaseViewItem extends LinearLayout {

    protected FragmentActivity _activity;
    protected RedEventApplication _app;
    protected DbInterface _db;
    protected NetworkInterface _net;
    protected ServerInterface _sv;
    protected XmlStore _xml;

    public String _name;
    protected String _childname;
    protected XmlNode _page;

    protected AppearanceHelper _appearanceHelper;
    protected TextHelper _textHelper;
    protected XmlNode _locallook;
    protected XmlNode _globallook;

    public BaseViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initWithPage(XmlNode page, FragmentActivity activity){
        _page = page;

        try {
            _name = page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("NoSuchFieldException for 'name' in BaseListViewItem:initWithPage getting name from xml", e);
        }
        if(page.hasChild(PAGE.CHILD)){
            try {
                _childname = page.getStringFromNode(PAGE.CHILD);
            } catch (NoSuchFieldException e) {
                MyLog.e("NoSuchFieldException for 'child' in BaseListViewItem:initWithPage getting childname from xml", e);
            }
        }

        _activity = activity;
        _app = (RedEventApplication)activity.getApplicationContext();
        _db = _app.getDbInterface();
        _net = _app.getNetworkInterface();
        _sv = _app.getServerInterface();
        _xml = _app.getXmlStore();

        try {
            if(_xml.appearance.hasChild(_name)){
                _locallook = _xml.getAppearanceForPage(_name);
            }
            _globallook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            _appearanceHelper = new AppearanceHelper(activity, _locallook, _globallook);
        } catch (Exception e) {
            MyLog.e("Exception in BaseActivity:onCreate getting appearance from xml", e);
        }

        try {
            _textHelper = new TextHelper(this, _name, _xml);
        } catch (Exception e) {
            MyLog.e("Exception when setting up TextHelper", e);
        }
    }
}
