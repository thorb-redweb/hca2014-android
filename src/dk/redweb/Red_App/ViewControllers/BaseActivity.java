package dk.redweb.Red_App.ViewControllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:48 PM
 */
public class BaseActivity extends FragmentActivity {
    protected RedEventApplication _app;
    protected DbInterface _db;
    protected NetworkInterface _net;
    protected ServerInterface _sv;
    protected XmlStore _xml;

    protected Context _context;
    protected String _name;
    protected String _childname;
    protected XmlNode _page;

    protected XmlNode _locallook;
    protected XmlNode _globallook;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _context = this;
        Bundle extras = getIntent().getExtras();
        XmlNode page = (XmlNode)extras.getSerializable(EXTRA.PAGE);
        _page = page;

        try {
            _name = page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            Log.e("RedEvent", "NoSuchFieldException for 'name' in BaseActivity:onCreate getting name from xml", e);
        }
        if(page.hasChild(PAGE.CHILD)){
            try {
                _childname = page.getStringFromNode(PAGE.CHILD);
            } catch (NoSuchFieldException e) {
                Log.e("RedEvent", "NoSuchFieldException for 'child' in BaseActivity:onCreate getting childname from xml", e);
            }
        }

        //super.onCreate(savedInstanceState);
        _app = (RedEventApplication)getApplication();
        _db = _app.getDbInterface();
        _net = _app.getNetworkInterface();
        _sv = _app.getServerInterface();
        _xml = _app.getXmlStore();

        try {
            if(_xml.appearance.hasChild(_name))
                _locallook = _xml.getAppearanceForPage(_name);
            _globallook = _xml.getAppearanceForPage(LOOK.GLOBAL);
        } catch (Exception e) {
            MyLog.e("Exception in BaseActivity:onCreate getting appearance from xml", e);
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected DateTime DateTimeNow(){
        DateTime now = new DateTime();
        if(_app.isDebugging()){
            now = _app.getDebugCurrentDate();
        }
        return now;
    }
}
