package dk.redweb.Red_App.Views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 9:45 AM
 */
public class TabbarBox extends LinearLayout {

    RedEventApplication _app;
    XmlStore _xml;
    LinearLayout _tabbarBox;

    ArrayList<TabbarButton> _tabbarbuttons;

    public TabbarBox(Context context, AttributeSet attrs){
        super(context, attrs);

        _app = (RedEventApplication)context.getApplicationContext();
        _xml = _app.getXmlStore();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_tabbarbox, this);

        _tabbarBox = (LinearLayout)findViewById(R.id.tabbarBox);

        setAppearance();
        createButtons();
    }

    private void setAppearance(){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.pageHasAppearance(LOOK.TABBAR)){
                localLook = _xml.getAppearanceForPage(LOOK.TABBAR);
                if(localLook.hasChild(LOOK.TABBAR_VISIBLE) && !localLook.getBoolFromNode(LOOK.TABBAR_VISIBLE)){
                    LinearLayout tabbarbox = (LinearLayout)findViewById(R.id.tabbarBox);
                    tabbarbox.setVisibility(GONE);
                }
            }
            AppearanceHelper helper = new AppearanceHelper(getContext(),localLook, globalLook);

            helper.setViewBackgroundImageOrColor(_tabbarBox, LOOK.NAVBAR_BACKGROUNDIMAGE, LOOK.NAVBAR_BACKGROUNDCOLOR, LOOK.GLOBAL_BARCOLOR);

        } catch (Exception e) {
            MyLog.e("Exception in TabBarBox:setAppearance", e);
        }
    }

    private void createButtons(){
        _tabbarbuttons = new ArrayList<TabbarButton>();
        for(XmlNode page : _xml.pages){
            if(page.hasChild(PAGE.TABNAME) | page.hasChild(PAGE.TABIMAGE)) {
                addButton(page);
            }
        }
    }

    private void addButton(XmlNode page){
        TabbarButton button = new TabbarButton(getContext(), _app);
        try {
            if(page.hasChild(PAGE.TABNAME)) {
                button.setButtonText(page.getStringFromNode(PAGE.TABNAME));
            }
        } catch (NoSuchFieldException e) {
            Log.e("RedEvent", "NoSuchFieldException in Tabbar:addButton for adding text", e);
        }
        try {
            if(page.hasChild(PAGE.TABIMAGE)) {
                String iconName = page.getStringFromNode(PAGE.TABIMAGE);
                Drawable icon = My.getDrawableFromResourceWithFilename(iconName, getContext());
                button.setButtonImage(icon);
            }
        } catch (NoSuchFieldException e) {
            Log.e("RedEvent", "NoSuchFieldException in Tabbar:addButton for adding image", e);
        }
        button.setPage(page);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);
        button.setLayoutParams(params);

        _tabbarbuttons.add(button);
        _tabbarBox.addView(button);
    }

    public void setParentActivity(FragmentActivity activity){
        for (TabbarButton button : _tabbarbuttons){
            button.setParentActivity(activity);
        }
    }
}
