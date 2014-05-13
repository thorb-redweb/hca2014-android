package dk.redweb.hca2014.ViewControllers;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.Network.UpdateService;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;
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

    public String _name;
    protected String _childname;
    protected XmlNode _page;

    protected AppearanceHelper _appearanceHelper;
    protected TextHelper _textHelper;
    protected XmlNode _locallook;
    protected XmlNode _globallook;

    protected View _view;
    protected NavBarBox _navbarbox;

    private UpdateService _updateService;

    protected DateTime DateTimeNow(){
        DateTime now = new DateTime();
        if(_app.isDebugging()){
            now = _app.getDebugCurrentDate();
        }
        return now;
    }

    public BasePageFragment(XmlNode page){
        _page = page;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, int resourceId){
        _view = inflater.inflate(resourceId, container, false);

        _app = (RedEventApplication)this.getActivity().getApplicationContext();
        _db = _app.getDbInterface();
        _net = _app.getNetworkInterface();
        _sv = _app.getServerInterface();
        _xml = _app.getXmlStore();

        try {
            _name = _page.getStringFromNode(PAGE.NAME);
        } catch (NoSuchFieldException e) {
            MyLog.e("NoSuchFieldException for 'name' in BasePageFragment:constructor getting name from xml", e);
        }
        if(_page.hasChild(PAGE.CHILD)){
            try {
                _childname = _page.getStringFromNode(PAGE.CHILD);
            } catch (NoSuchFieldException e) {
                MyLog.e("NoSuchFieldException for 'child' in BasePageFragment:constructor getting childname from xml", e);
            }
        }

        try {
            if(_xml.appearance.hasChild(_name)){
                _locallook = _xml.getAppearanceForPage(_name);
            }
            _globallook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            _appearanceHelper = new AppearanceHelper(getActivity(), _locallook, _globallook);
        } catch (Exception e) {
            MyLog.e("Exception in BaseActivity:onCreate getting appearance from xml", e);
        }

        try {
            _textHelper = new TextHelper(_view, _name, _xml);
        } catch (Exception e) {
            MyLog.e("Exception when setting up TextHelper", e);
        }
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

        _updateService = new UpdateService(_app);
        _updateService.start();
    }

    @Override
    public void onPause(){
        _updateService.stop();
        super.onPause();
    }

    private void setLogoBars(){
        try {
            ImageView imgTopLogo = (ImageView)getActivity().findViewById(R.id.fragmentpages_imgTopLogo);
            if(_page.hasChild(PAGE.TOPLOGO)){
                imgTopLogo.setVisibility(View.VISIBLE);
                String toplogoFilename = _page.getStringFromNode(PAGE.TOPLOGO);
                Drawable toplogoDrawable = My.getDrawableFromResourceWithFilename(toplogoFilename, getActivity());
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

    protected void setupBackButton(){
        try {
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.flxBackButton);
            if(_page.hasChild(PAGE.RETURNBUTTON) && _page.getBoolFromNode(PAGE.RETURNBUTTON)){
                flxBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                });
            } else {
                flxBackButton.setVisibility(View.GONE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting ReturnButton attribute from page xml", e);
        }
    }
}
