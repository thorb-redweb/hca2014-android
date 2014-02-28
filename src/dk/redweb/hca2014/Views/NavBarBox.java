package dk.redweb.hca2014.Views;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TEXT;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/25/13
 * Time: 11:49 AM
 */
public class NavBarBox extends LinearLayout {

    RedEventApplication _app;
    DbInterface _db;
    XmlStore _xml;

    FragmentActivity _parentActivity;

    LinearLayout _navbarBox;
    ImageView _homeButton;
    LinearLayout _upButton;

    public NavBarBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        _app = (RedEventApplication)context.getApplicationContext();
        _db = _app.getDbInterface();
        _xml = _app.getXmlStore();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_navbarbox, this);

        _navbarBox = (LinearLayout)findViewById(R.id.navbarbox);
        _upButton = (LinearLayout)findViewById(R.id.navbar_lnrUpButton);
        _homeButton = (ImageView)findViewById(R.id.navbar_imgHomeButton);

        setAppearance();
        setText();

        setupHomeButton();
    }

    private void setAppearance(){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.pageHasAppearance(LOOK.NAVIGATIONBAR)){
                localLook = _xml.getAppearanceForPage(LOOK.NAVIGATIONBAR);
                if(localLook.hasChild(LOOK.NAVBAR_VISIBLE) && !localLook.getBoolFromNode(LOOK.NAVBAR_VISIBLE)){
                    LinearLayout navbarbox = (LinearLayout)findViewById(R.id.navbarbox);
                    navbarbox.setVisibility(GONE);
                }
            }
            AppearanceHelper helper = new AppearanceHelper(getContext(),localLook,globalLook);

            //The Box
            helper.setViewBackgroundImageOrColor(_navbarBox, LOOK.NAVBAR_BACKGROUNDIMAGE, LOOK.NAVBAR_BACKGROUNDCOLOR, LOOK.GLOBAL_BARCOLOR);

            //Title

            if(localLook != null && localLook.getBoolWithNoneAsFalseFromNode(LOOK.NAVBAR_HASTITLE)){
                TextView txtTitle = (TextView) findViewById(R.id.navbar_lblTitle);
                txtTitle.setVisibility(VISIBLE);
                helper.TextView.setColor(txtTitle, LOOK.NAVBAR_TITLECOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
                helper.TextView.setSize(txtTitle, LOOK.NAVBAR_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
                helper.TextView.setStyle(txtTitle, LOOK.NAVBAR_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
                helper.TextView.setShadow(txtTitle, LOOK.NAVBAR_TITLESHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                        LOOK.NAVBAR_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);
            }
        } catch (Exception e) {
            MyLog.e("Exception in NavBarBox:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(this,LOOK.NAVIGATIONBAR,_xml);


        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    private void setupHomeButton(){
        _homeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode frontPage = _xml.getFrontPage();
                    NavController.changePageWithXmlNode(frontPage, _parentActivity);
                } catch (Exception e) {
                    MyLog.e("Exception in NavBarBox:onClick on navbar_btnHome", e);
                }
            }
        });
    }

    public void setParentActivity(FragmentActivity parent){
        _parentActivity = parent;
    }

    public void updateNavigationBar(XmlNode page){
        updateHomeButton(page);
        updateUpButton(page);
        updateTitle(page);
    }

    private void updateHomeButton(XmlNode page){

    }

    private void updateUpButton(XmlNode page){
        try {
            if(page.hasChild(PAGE.FRONTPAGE) && page.getBoolFromNode(PAGE.FRONTPAGE)){
                _upButton.setVisibility(GONE);
            }
            else{
                _upButton.setVisibility(VISIBLE);
                setUpButtonTargetForThisPage(page);
            }
        } catch (Exception e) {
            MyLog.e("Exception when updating the visibility of the Up Button", e);
        }
    }

    private void updateTitle(XmlNode page){
        try {
            String pageTitle = page.getStringFromNode(PAGE.NAME);
            TextView txtTitle = (TextView)findViewById(R.id.navbar_lblTitle);
            txtTitle.setText(pageTitle);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting pageTitle", e);
        }
    }

    public void setUpButtonTargetForThisPage(final XmlNode thisPage){
        _upButton.setVisibility(VISIBLE);

        TextView txtUpButton = (TextView)findViewById(R.id.navbar_txtUpButton);

        XmlNode parentPage = null;
        try {
            parentPage = _xml.getOfficialParentOf(thisPage);

            if(parentPage.hasChild(PAGE.NAVNAME)){
                txtUpButton.setText(determineNavname(parentPage, thisPage));
            } else {
                txtUpButton.setText(parentPage.getStringFromNode(PAGE.NAME));
            }
        } catch (Exception e) {
            MyLog.e("Exception in NavBarBox:setUpButtonTargetForThisPage", e);
        }

        final XmlNode nextPage = parentPage.deepClone();
        _upButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(thisPage.hasChild(PAGE.PARENTPARAMETERS)){
                        for(XmlNode child : thisPage.getChildFromNode(PAGE.PARENTPARAMETERS)){
                            nextPage.addXmlNodeToNode(child);
                        }
                    }
                    NavController.changePageWithXmlNode(nextPage, _parentActivity);
                } catch (Exception e) {
                    MyLog.e("Exception in NavBarBox:onClick on navbar_btnUp", e);
                }
            }
        });
    }

    private String determineNavname(XmlNode parentPage, XmlNode childPage) throws NoSuchFieldException {
        String navname = parentPage.getStringFromNode(PAGE.NAVNAME);
        if(navname.equals(PAGE.TAG_SESSIONTITLE)){
            int sessionId = childPage.getIntegerFromNode(PAGE.SESSIONID);
            SessionVM session = _db.Sessions.getVMFromId(sessionId);
            String sessionName = session.Title();
            if(sessionName.length() > 20){
                sessionName = sessionName.substring(0, 20);
            }
            return sessionName;
        }

        return navname;
    }

}
