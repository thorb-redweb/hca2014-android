package dk.redweb.Red_App.Views;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

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
    RelativeLayout _homeButton;
    RelativeLayout _upButton;

    public NavBarBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        _app = (RedEventApplication)context.getApplicationContext();
        _db = _app.getDbInterface();
        _xml = _app.getXmlStore();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_navbarbox, this);

        _navbarBox = (LinearLayout)findViewById(R.id.navbarbox);
        _upButton = (RelativeLayout)findViewById(R.id.navbar_rltUpButton);
        _homeButton = (RelativeLayout)findViewById(R.id.navbar_rltHomeButton);

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

            if(localLook.getBoolWithNoneAsFalseFromNode(LOOK.NAVBAR_HASTITLE)){
                TextView txtTitle = (TextView) findViewById(R.id.navbar_lblTitle);
                txtTitle.setVisibility(VISIBLE);
                helper.TextView.setColor(txtTitle, LOOK.NAVBAR_TITLECOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
                helper.TextView.setSize(txtTitle, LOOK.NAVBAR_TITLESIZE, LOOK.GLOBAL_TITLESIZE);
                helper.TextView.setStyle(txtTitle, LOOK.NAVBAR_TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
                helper.TextView.setShadow(txtTitle, LOOK.NAVBAR_TITLESHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                        LOOK.NAVBAR_TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);
            }

            //Home Button
            helper.setViewBackgroundImageOrColor(_homeButton, LOOK.NAVBAR_HOMEBUTTONBACKIMAGE, LOOK.NAVBAR_HOMEBUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            ImageView imgHomeButton = (ImageView)findViewById(R.id.navbar_imgHomeButton);
            helper.setImageViewImage(imgHomeButton, LOOK.NAVBAR_HOMEBUTTONIMAGE);

            TextView txtHomeButton = (TextView)findViewById(R.id.navbar_lblHomeButton);
            helper.TextView.setColor(txtHomeButton, LOOK.NAVBAR_HOMEBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.TextView.setSize(txtHomeButton, LOOK.NAVBAR_HOMEBUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtHomeButton, LOOK.NAVBAR_HOMEBUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtHomeButton, LOOK.NAVBAR_HOMEBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.NAVBAR_HOMEBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            //Up Button
            helper.setViewBackgroundImageOrColor(_upButton, LOOK.NAVBAR_UPBUTTONBACKIMAGE, LOOK.NAVBAR_UPBUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);

            ImageView imgUpButton = (ImageView)findViewById(R.id.navbar_imgUpButton);
            helper.setImageViewImage(imgUpButton, LOOK.NAVBAR_UPBUTTONIMAGE);

            TextView txtUpButton = (TextView)findViewById(R.id.navbar_lblUpButton);
            helper.TextView.setColor(txtUpButton, LOOK.NAVBAR_UPBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.TextView.setSize(txtUpButton, LOOK.NAVBAR_UPBUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtUpButton, LOOK.NAVBAR_UPBUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtUpButton, LOOK.NAVBAR_UPBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.NAVBAR_UPBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in NavBarBox:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(this,LOOK.NAVIGATIONBAR,_xml);

            helper.tryText(R.id.navbar_lblHomeButton, TEXT.NAVBAR_HOMEBUTTON);
            helper.tryText(R.id.navbar_lblUpButton, TEXT.NAVBAR_UPBUTTON);
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
        try {
            if(page.hasChild(PAGE.FRONTPAGE) && page.getBoolFromNode(PAGE.FRONTPAGE)){
                _homeButton.setVisibility(View.GONE);
            }
            else
            {
                _homeButton.setVisibility(View.VISIBLE);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when updating the visibility of the Home Button", e);
        }
    }

    private void updateUpButton(XmlNode page){
        try {
            XmlNode parentPage = _xml.getOfficialParentOf(page);
            if(parentPage.hasChild(PAGE.FRONTPAGE) && parentPage.getBoolFromNode(PAGE.FRONTPAGE)){
                _upButton.setVisibility(GONE);
            }
            else{
                _upButton.setVisibility(VISIBLE);
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

        TextView txtUpButton = (TextView)findViewById(R.id.navbar_lblUpButton);

        XmlNode parentPage = null;
        try {
            parentPage = _xml.getOfficialParentOf(thisPage);

            if(parentPage.hasChild(PAGE.NAVNAME)){
                txtUpButton.setText(determineNavname(parentPage));
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

    private String determineNavname(XmlNode parentPage) throws NoSuchFieldException {
        String navname = parentPage.getStringFromNode(PAGE.NAVNAME);
        if(navname.equals(PAGE.TAG_SESSIONTITLE)){
            int sessionId = parentPage.getChildFromNode(PAGE.PARENTPARAMETERS).getIntegerFromNode(PAGE.SESSIONID);
            SessionVM session = _db.Sessions.getVMFromId(sessionId);
            return session.Title();
        }

        return navname;
    }

}
