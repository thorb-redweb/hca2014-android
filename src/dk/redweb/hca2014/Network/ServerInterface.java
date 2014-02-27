package dk.redweb.hca2014.Network;

import dk.redweb.hca2014.Interfaces.*;
import dk.redweb.hca2014.RedEventApplication;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 2:37 PM
 */
public class ServerInterface {
    RedEventApplication _app;

    public ServerInterface(RedEventApplication app){
        this._app = app;
    }

    public void dumpServer(Delegate_dumpServer delegate){
        new Handler_DumpServer(delegate, _app.getXmlStore()).execute();
    }

    public void updateFromServer(Delegate_updateFromServer delegate){
        new Handler_UpdateFromServer(delegate, _app).execute();
    }

    public void removeRegistrationAttributes(Delegate_removeRegistrationAttributes delegate, String regid){
        new Handler_RemoveRegistrationAttributes(delegate, _app, regid).execute();
    }

    public void uploadRegistrationAttributes(Delegate_uploadRegistrationAttributes delegate, String registrationId, String username){
        new Handler_UploadRegistrationAttributes(delegate, _app, registrationId, username).execute();
    }

    public void pushPMGroupSubscriptionUpdate(Delegate_PushPMGroupSubscriptionUpdate delegate, int groupid, boolean checked) {
        new Handler_PushPMGroupSubscriptionUpdate(delegate, _app, groupid, checked).execute();
    }
}
