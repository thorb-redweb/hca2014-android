package dk.redweb.Red_App.ViewControllers.System.PushMessageAutoSubscriber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.redweb.Red_App.Interfaces.Delegate_uploadRegistrationAttributes;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.Network.PushMessages.PushMessageInitializationHandling;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/27/13
 * Time: 15:01
 */
public class PushMessageAutoSubscriberFragment extends BasePageFragment implements Delegate_uploadRegistrationAttributes {

    boolean firstTime;
    PushMessageInitializationHandling _pmHandler;

    public PushMessageAutoSubscriberFragment(XmlNode page) {
        super(page);
        firstTime = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.emptyview);

        _pmHandler = new PushMessageInitializationHandling(getActivity(),this);
        boolean hasInitialized = PushMessageInitializationHandling.checkInitialization(getActivity());

        if(!firstTime){
            NavController.popPage(getActivity());
            return null;
        }

        if(hasInitialized){
            firstTime = false;
            changeToNextPage();
            return null;
        }

        addUserToDatabase();
        return _view;
    }



    private void addUserToDatabase(){
        _pmHandler.initializePushService("");
        firstTime = false;
    }

    @Override
    public void returnFromUploadToServer(String result) {
    }

    @Override
    public void errorOccured(String errorMessage) {
        String alertMessage = "Der skete en fejl under tilmelding til Push Beskeder.";

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(alertMessage);
        alertDialog.setMessage("Næste gang du starter appen vil vi prøve at tilmelde dig igen." + errorMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", closeDialogOnClickListener());
        alertDialog.show();

        MyLog.w("Alertdialog displayed");
    }

    private DialogInterface.OnClickListener closeDialogOnClickListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
    }

    private void changeToNextPage(){
        try {
            XmlNode nextpage = _xml.getPage(_childname);
            NavController.changePageWithXmlNode(nextpage, getActivity());
        } catch (Exception e) {
            MyLog.e("Exception when changing page to childpage", e);
            errorOccured("Der er en fejl i appen. Kontakt venligst udvikleren.");
        }
    }
}
