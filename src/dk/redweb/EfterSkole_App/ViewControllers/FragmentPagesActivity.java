package dk.redweb.EfterSkole_App.ViewControllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dk.redweb.EfterSkole_App.MyLog;
import dk.redweb.EfterSkole_App.NavController;
import dk.redweb.EfterSkole_App.R;
import dk.redweb.EfterSkole_App.RedEventApplication;
import dk.redweb.EfterSkole_App.StaticNames.PAGE;
import dk.redweb.EfterSkole_App.Views.NavBarBox;
import dk.redweb.EfterSkole_App.Views.TabbarBox;
import dk.redweb.EfterSkole_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 3:42 PM
 */
public class FragmentPagesActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fragmentpages);

        TabbarBox tabbarBox = (TabbarBox)findViewById(R.id.tabbar);
        tabbarBox.setParentActivity(this);
        NavBarBox navBarBox = (NavBarBox)findViewById(R.id.navbar);
        navBarBox.setParentActivity(this);

        RedEventApplication app = (RedEventApplication)getApplication();
        app.setNavbar(navBarBox);

        Bundle extras = getIntent().getExtras();
        XmlNode page = (XmlNode)extras.getSerializable(PAGE.PAGE);

        try {
            NavController.changePageWithXmlNode(page, this, false);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting up FragmentPagesActivity fragments", e);
        }
    }

    public void setAppearance(){

    }
}