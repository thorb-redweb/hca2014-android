package dk.redweb.Red_App.ViewControllers.Settings.PushMessageUnsubscriber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.Interfaces.Delegate_removeRegistrationAttributes;
import dk.redweb.Red_App.Network.PushMessages.PushMessageInitializationHandling;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/20/13
 * Time: 12:53
 */
public class PushMessageUnsubscriberFragment extends BasePageFragment implements Delegate_removeRegistrationAttributes {

    String lblWarningDialogTitle;
    String lblWarningDialogMessage;
    String lblWarningDialogOk;
    String lblWarningDialogCancel;

    String lblFinishedDialogTitle;
    String lblFinishedDialogMessage;
    String lblFinishedDialogOk;

    public PushMessageUnsubscriberFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_pushmessageunsubscriber);

        setAppearance();
        setText();

        FlexibleButton flxUnsubscribe = (FlexibleButton)findViewById(R.id.pushmessageunsubscriber_flxUnsubscribe);
        flxUnsubscribe.setOnClickListener(unsubscribeButtonListener());

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout mainViewLayout = (LinearLayout)findViewById(R.id.pushmessageunsubscriber_lnrMainView);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView lblTitle = (TextView)findViewById(R.id.pushmessageunsubscriber_lblTitle);
            helper.TextView.setTextColor(lblTitle, LOOK.TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(lblTitle, LOOK.TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.TextView.setTextStyle(lblTitle, LOOK.TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.TextView.setTextShadow(lblTitle, LOOK.TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                                           LOOK.TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

            TextView lblWarning = (TextView)findViewById(R.id.pushmessageunsubscriber_lblWarning);
            helper.TextView.setTextColor(lblWarning, LOOK.TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(lblWarning, LOOK.TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setTextStyle(lblWarning, LOOK.TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setTextShadow(lblWarning, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxUnsubscribe = (FlexibleButton)findViewById(R.id.pushmessageunsubscriber_flxUnsubscribe);
            helper.setViewBackgroundImageOrColor(flxUnsubscribe, LOOK.BUTTONBACKGROUNDIMAGE,
                    LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxUnsubscribe, LOOK.BUTTONICON);
            helper.FlexButton.setTextColor(flxUnsubscribe, LOOK.BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxUnsubscribe, LOOK.BUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxUnsubscribe, LOOK.BUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxUnsubscribe, LOOK.BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception when setting Appearance for PushMessageUnsubscriberFragment", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = new TextHelper(_view, _name, _xml);

            helper.setText(R.id.pushmessageunsubscriber_lblTitle, TEXT.PUSHMESSAGEUNSUBSCRIBER_TITLE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_TITLE);
            helper.setText(R.id.pushmessageunsubscriber_lblWarning, TEXT.PUSHMESSAGEUNSUBSCRIBER_WARNING, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_WARNING);
            helper.setFlexibleButtonText(R.id.pushmessageunsubscriber_flxUnsubscribe, TEXT.PUSHMESSAGEUNSUBSCRIBER_UNSUBSCRIBEBUTTON, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_UNSUBSCRIBEBUTTON);

            lblFinishedDialogTitle = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2TITLE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2TITLE);
            lblFinishedDialogMessage = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2MESSAGE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2MESSAGE);
            lblFinishedDialogOk = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2OK, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOG2OK);

            lblWarningDialogTitle = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGTITLE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGTITLE);
            lblWarningDialogMessage = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGMESSAGE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGMESSAGE);
            lblWarningDialogOk = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGOK, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGOK);
            lblWarningDialogCancel = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGCANCEL, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGCANCEL);

        } catch (Exception e) {
            MyLog.e("Exception when setting Text for PushMessageUnsubscriberFragment", e);
        }
    }

    public View.OnClickListener unsubscribeButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog warningDialog = areYouSureDialog();
                warningDialog.show();
            }
        };
    }

    public AlertDialog areYouSureDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setMessage(lblWarningDialogMessage);
        dialog.setTitle(lblWarningDialogTitle);
        dialog.setPositiveButton(lblWarningDialogOk, okButtonListener());
        dialog.setNegativeButton(lblWarningDialogCancel, cancelButtonListener());
        return dialog.create();
    }

    @Override
    public void errorOccured(String errorMessage) {
        String alertMessage = "Der er sket en fejl under forsøget på at framelde";

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(alertMessage);
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Prøv Igen", tryAgainButtonListener());
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afbryd", cancelButtonListener());
        alertDialog.show();

        MyLog.w("Alertdialog displayed");
    }

    public DialogInterface.OnClickListener cancelButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().popBackStack();
            }
        };
    }

    public DialogInterface.OnClickListener tryAgainButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
    }

    public DialogInterface.OnClickListener okButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeRegistrationAttributes();
                finishedDialog().show();
            }
        };
    }

    private void removeRegistrationAttributes(){
        _db.PushMessageGroups.unsubscribeAll();

        String regid = PushMessageInitializationHandling.getRegistrationId(getActivity());
        _sv.removeRegistrationAttributes(this, regid);

        PushMessageInitializationHandling.removeRegistrationId(getActivity());
    }

    public AlertDialog finishedDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setMessage(lblFinishedDialogMessage);
        dialog.setTitle(lblFinishedDialogTitle);
        dialog.setPositiveButton(lblFinishedDialogOk, finishedButtonListener());
        return dialog.create();
    }

    public DialogInterface.OnClickListener finishedButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    NavController.changePageWithXmlNode(_xml.getFrontPage(),getActivity());
                } catch (Exception e) {
                    MyLog.e("Exception when changing page to frontpage after removal of registration attributes", e);
                }
            }
        };
    }
}
