package dk.redweb.hca2014.ViewControllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.Views.TabbarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;

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