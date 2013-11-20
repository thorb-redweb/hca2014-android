package dk.redweb.Red_App.ViewControllers.System.PushMessageForwarder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Interfaces.Delegate_updateFromServer;
import dk.redweb.Red_App.Interfaces.Delegate_updateToDatabase;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TYPE;
import dk.redweb.Red_App.ViewControllers.FragmentPagesActivity;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/23/13
 * Time: 12:52
 *
 * This class is the target of push message notifications (whether of the PushMessage object type, or others), and
 * manages the creation and inflation of the correct fragments.
 */
public class PushMessageForwarder extends FragmentActivity implements Delegate_updateFromServer, Delegate_updateToDatabase {

    RedEventApplication _app;
    DbInterface _db;
    XmlStore _xml;

    Boolean _doingwork;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _app = (RedEventApplication)getApplication();
        _db = _app.getDbInterface();
        _xml = _app.getXmlStore();
        _app.getServerInterface().updateFromServer(this);

        _doingwork = true;
    }

    protected void onStart(){
        super.onStart();
        if(!_doingwork){
            finish();
        }
    }

    @Override
    public void returnFromUpdateFromServer(String result) {
        if (result.equals("")){
            MyLog.v("No updates received");
            returnFromUpdateToDatabase();
        }
        else
        {
            _db.updateFromServer(result, this);
        }
    }

    @Override
    public void returnFromUpdateToDatabase() {
        Bundle extras = getIntent().getExtras();
        String type = extras.getString(EXTRA.TYPE);

        if(type.equals("pm")){
            startPushMessageDetail();
        }
        else if(type.equals("a")){
            startArticleDetail();
        }
        _doingwork = false;
    }

    public void startPushMessageDetail(){
        Bundle extras = getIntent().getExtras();
        String pushMessageId = extras.getString(EXTRA.MESSAGEID);
        String type = TYPE.PUSHMESSAGEDETAIL;

        XmlNode pendingPage = null;
        try {
            XmlNode selectedPage = _xml.getPage(type);

            pendingPage = selectedPage.deepClone();
            pendingPage.addChildToNode(PAGE.PUSHMESSAGEID, pushMessageId);
        } catch (Exception e) {
            MyLog.e("Exception when setting up PushMessageDetail page for pendingIntent", e);
        }

        XmlNode pendingParentPage = null;
        try{
            XmlNode parentPage = _xml.getPage(pendingPage.getStringFromNode(PAGE.PARENT));

            pendingParentPage = parentPage.deepClone();
            pendingParentPage.addChildToNode(PAGE.CHILDPAGE, pendingPage.value());
        } catch (Exception e) {
            MyLog.e("Exception when setting up PushMessageDetail parentpage for pendingIntent", e);
        }

        Intent pushMessageDetail = new Intent(this, FragmentPagesActivity.class);
        pushMessageDetail.putExtra(EXTRA.PAGE, pendingParentPage);
        this.startActivity(pushMessageDetail);
    }

    public void startArticleDetail(){
        Bundle extras = getIntent().getExtras();
        String articleId = extras.getString(EXTRA.MESSAGEID);
        String type = TYPE.ARTICLEDETAIL;

        XmlNode pendingPage = null;
        try {
            XmlNode selectedPage = _xml.getPage(type);

            pendingPage = selectedPage.deepClone();
            pendingPage.addChildToNode(PAGE.ARTICLEID, articleId);
        } catch (Exception e) {
            MyLog.e("Exception when setting up ArticleDetail page for pendingIntent", e);
        }

        XmlNode pendingParentPage = null;
        try{
            XmlNode parentPage = _xml.getPage(pendingPage.getStringFromNode(PAGE.PARENT));

            pendingParentPage = parentPage.deepClone();
            pendingParentPage.addChildToNode(PAGE.CHILDPAGE, pendingPage.value());
        } catch (Exception e) {
            MyLog.e("Exception when setting up ArticleDetail parentpage for pendingIntent", e);
        }

        Intent articleDetail = new Intent(this, FragmentPagesActivity.class);
        articleDetail.putExtra(EXTRA.PAGE, pendingParentPage);
        this.startActivity(articleDetail);
    }

    @Override
    public void errorOccured(String errorMessage) {
        MyLog.e(errorMessage);
    }
}