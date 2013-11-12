package dk.redweb.Red_App.ViewControllers;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TYPE;
import dk.redweb.Red_App.Views.NavBarBox;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 3:35 PM
 */
public class BasePageFragment extends Fragment {
    protected RedEventApplication _app;
    protected DbInterface _db;
    protected NetworkInterface _net;
    protected ServerInterface _sv;
    protected XmlStore _xml;

    public String Name;
    protected String _childname;
    protected XmlNode _page;

    protected XmlNode _locallook;
    protected XmlNode _globallook;

    protected View _view;

    protected DateTime DateTimeNow(){
        DateTime now = new DateTime();
        if(_app.isDebugging()){
            now = _app.getDebugCurrentDate();
        }
        return now;
    }

    public BasePageFragment(XmlNode page){
        _page = page;

        try {
            Name = page.getStringFromNode(PAGE.NAME);
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, int resourceId){
        _view = inflater.inflate(resourceId, container, false);

        _app = (RedEventApplication)this.getActivity().getApplicationContext();
        _db = _app.getDbInterface();
        _net = _app.getNetworkInterface();
        _sv = _app.getServerInterface();
        _xml = _app.getXmlStore();

        try {
            if(_xml.appearance.hasChild(Name))
                _locallook = _xml.getAppearanceForPage(Name);
            _globallook = _xml.getAppearanceForPage(LOOK.GLOBAL);
        } catch (Exception e) {
            MyLog.e("Exception in BaseActivity:onCreate getting appearance from xml", e);
        }

        _app.currentPage = _page;

        setLogoBars();

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        boolean parentIsSwipeview = false;
        XmlNode parentPage = null;
        if(_page.hasChild(PAGE.PARENT)){
            try {
                String parentName = _page.getStringFromNode(PAGE.PARENT);
                parentPage =  _xml.getPage(parentName);
                String parentType = parentPage.getStringFromNode(PAGE.TYPE);
                parentIsSwipeview = parentType.equals(TYPE.SWIPEVIEW);
            } catch (Exception e) {
                MyLog.e("Exception when determining whether parentPage is SwipeView", e);
            }
        }

        NavBarBox navbar = _app.getNavbar();
        if(parentIsSwipeview){
            navbar.updateNavigationBar(parentPage);
        }
        else
        {
            navbar.updateNavigationBar(_page);
        }
    }

    private void setLogoBars(){
        try {
            ImageView imgTopLogo = (ImageView)getActivity().findViewById(R.id.fragmentpages_imgTopLogo);
            if(_page.hasChild(PAGE.TOPLOGO)){
                imgTopLogo.setVisibility(View.VISIBLE);
                String toplogoFilename = _page.getStringFromNode(PAGE.TOPLOGO);
                Drawable toplogoDrawable = My.getDrawableFromFilename(toplogoFilename, getActivity());
                imgTopLogo.setImageDrawable(toplogoDrawable);
            } else {
                imgTopLogo.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting filename for toplogo", e);
        }
    }

    public void changePageTo(Fragment newFragment){
        NavController.changePageWithFragment(newFragment, getActivity(), true);
    }

    protected View findViewById(int id){
        return _view.findViewById(id);
    }
}
