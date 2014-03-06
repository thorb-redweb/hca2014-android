package dk.redweb.hca2014.ViewControllers.System.PushMessageForwarder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Interfaces.Delegate_updateFromServer;
import dk.redweb.hca2014.Interfaces.Delegate_updateToDatabase;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.EXTRA;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TYPE;
import dk.redweb.hca2014.ViewControllers.FragmentPagesActivity;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

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

    private ProgressDialog _progressDialog;

    Boolean _doingwork;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _app = (RedEventApplication)getApplication();
        _db = _app.getDbInterface();
        _xml = _app.getXmlStore();

        _progressDialog = new ProgressDialog(this);
        _progressDialog.setTitle("Henter data fra databasen");
        _progressDialog.setMessage("Dette kan tage et par minutter...");
        _progressDialog.show();

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
        _progressDialog.dismiss();

        Bundle extras = getIntent().getExtras();
        String type = extras.getString(EXTRA.TYPE);
        String messageId = extras.getString(EXTRA.MESSAGEID);

        XmlNode forwardNode = new XmlNode(PAGE.PUSHMESSAGERESULT,new ArrayList<XmlNode>());
        try {
            forwardNode.addChildToNode(PAGE.TYPE,type);
            forwardNode.addChildToNode(PAGE.PUSHMESSAGEID, messageId);
        } catch (InvalidPropertiesFormatException e) {
            MyLog.e("Exception when adding type and messageid to forwardnode", e);
        }

        Intent pushMessageDetail = new Intent(this, FragmentPagesActivity.class);
        pushMessageDetail.putExtra(EXTRA.PAGE, forwardNode);
        this.startActivity(pushMessageDetail);
    }

    @Override
    public void errorOccured(String errorMessage) {
        MyLog.e(errorMessage);
    }
}