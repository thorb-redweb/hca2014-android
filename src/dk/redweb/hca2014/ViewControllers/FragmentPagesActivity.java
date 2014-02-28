package dk.redweb.hca2014.ViewControllers;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.EXTRA;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TYPE;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.Views.TabbarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 3:42 PM
 */
public class FragmentPagesActivity extends FragmentActivity {
    private XmlStore _xml;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fragmentpages);

        TabbarBox tabbarBox = (TabbarBox)findViewById(R.id.tabbar);
        tabbarBox.setParentActivity(this);
        NavBarBox navBarBox = (NavBarBox)findViewById(R.id.navbar);
        navBarBox.setParentActivity(this);

        RedEventApplication app = (RedEventApplication)getApplication();
        app.setNavbar(navBarBox);
        _xml = app.getXmlStore();

        Bundle extras = getIntent().getExtras();
        XmlNode page = (XmlNode)extras.getSerializable(PAGE.PAGE);
        if(page.name().matches(PAGE.PUSHMESSAGERESULT)){
            String type = null;
            String messageid = null;
            try {
                type = page.getStringFromNode(PAGE.TYPE);
                messageid = page.getStringFromNode(PAGE.PUSHMESSAGEID);
            } catch (NoSuchFieldException e) {
                MyLog.e("Exception when getting type or messageid from PushForwarder's forwarded XmlNode", e);
            }
            page = getPushedPage(type, messageid);
        }

        try {
            NavController.changePageWithXmlNode(page, this, false);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting up FragmentPagesActivity fragments", e);
        }
    }

    public void setAppearance(){

    }

    private XmlNode getPushedPage(String type, String messageid){
        if(type.equals("pm")){
            return startPushMessageDetail(messageid);
        }
        else if(type.equals("a")){
            return startArticleDetail(messageid);
        }
        return null;
    }

    public XmlNode startArticleDetail(String articleId){
        String type = TYPE.ARTICLEDETAIL;

        XmlNode pendingPage = null;
        try {
            XmlNode selectedPage = _xml.getPage(type);

            pendingPage = selectedPage.deepClone();
            pendingPage.addChildToNode(PAGE.ARTICLEID, articleId);
        } catch (Exception e) {
            MyLog.e("Exception when setting up ArticleDetail page", e);
        }

        XmlNode pendingParentPage = null;
        try{
            XmlNode parentPage = _xml.getPage(pendingPage.getStringFromNode(PAGE.PARENT));

            pendingParentPage = parentPage.deepClone();
            pendingParentPage.addChildToNode(PAGE.CHILDPAGE, (ArrayList<XmlNode>)pendingPage.value());
        } catch (Exception e) {
            MyLog.e("Exception when setting up ArticleDetail parentpage", e);
        }

        return pendingParentPage;
    }

    public XmlNode startPushMessageDetail(String pushMessageId){
        String type = TYPE.PUSHMESSAGEDETAIL;

        XmlNode pendingPage = null;
        try {
            XmlNode selectedPage = _xml.getPage(type);

            pendingPage = selectedPage.deepClone();
            pendingPage.addChildToNode(PAGE.PUSHMESSAGEID, pushMessageId);
        } catch (Exception e) {
            MyLog.e("Exception when setting up PushMessageDetail page", e);
        }

        XmlNode pendingParentPage = null;
        try{
            XmlNode parentPage = _xml.getPage(pendingPage.getStringFromNode(PAGE.PARENT));

            pendingParentPage = parentPage.deepClone();
            pendingParentPage.addChildToNode(PAGE.CHILDPAGE, pendingPage.value());
        } catch (Exception e) {
            MyLog.e("Exception when setting up PushMessageDetail parentpage", e);
        }

        return pendingParentPage;
    }
}