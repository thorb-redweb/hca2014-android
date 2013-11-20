package dk.redweb.Red_App.ViewControllers.Settings.PushMessageUnsubscriber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.AppearanceHelper;
import dk.redweb.Red_App.Interfaces.Delegate_removeRegistrationAttributes;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.PushMessages.PushMessageInitializationHandling;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.TextHelper;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/20/13
 * Time: 12:53
 */
public class PushMessageUnsubscriberFragment extends BasePageFragment implements Delegate_removeRegistrationAttributes {

    String lblDialogTitle;
    String lblDialogMessage;
    String lblDialogOk;
    String lblDialogCancel;

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
            helper.setTextColor(lblTitle, LOOK.TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(lblTitle, LOOK.TITLESIZE, LOOK.GLOBAL_TITLESIZE);
            helper.setTextStyle(lblTitle, LOOK.TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
            helper.setTextShadow(lblTitle, LOOK.TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                                           LOOK.TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);

            TextView lblWarning = (TextView)findViewById(R.id.pushmessageunsubscriber_lblWarning);
            helper.setTextColor(lblWarning, LOOK.TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(lblWarning, LOOK.TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(lblWarning, LOOK.TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(lblWarning, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxUnsubscribe = (FlexibleButton)findViewById(R.id.pushmessageunsubscriber_flxUnsubscribe);
            helper.setViewBackgroundImageOrColor(flxUnsubscribe, LOOK.BUTTONBACKGROUNDIMAGE,
                    LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.setFlexibleButtonImage(flxUnsubscribe, LOOK.BUTTONICON);
            helper.setFlexibleButtonTextColor(flxUnsubscribe, LOOK.BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setFlexibleButtonTextSize(flxUnsubscribe, LOOK.BUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setFlexibleButtonTextStyle(flxUnsubscribe, LOOK.BUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setFlexibleButtonTextShadow(flxUnsubscribe, LOOK.BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
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

            lblDialogTitle = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGTITLE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGTITLE);
            lblDialogMessage = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGMESSAGE, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGMESSAGE);
            lblDialogOk = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGOK, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGOK);
            lblDialogCancel = helper.getText(TEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGCANCEL, DEFAULTTEXT.PUSHMESSAGEUNSUBSCRIBER_DIALOGCANCEL);

        } catch (Exception e) {
            MyLog.e("Exception when setting Text for PushMessageUnsubscriberFragment", e);
        }
    }

    public AlertDialog areYouSureDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setMessage(lblDialogMessage);
        dialog.setTitle(lblDialogTitle);
        dialog.setPositiveButton(lblDialogOk, okButtonListener());
        dialog.setNegativeButton(lblDialogCancel, cancelButtonListener());
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

    public View.OnClickListener unsubscribeButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog warningDialog = areYouSureDialog();
                warningDialog.show();
            }
        };
    }

    public DialogInterface.OnClickListener okButtonListener(){
        final PushMessageUnsubscriberFragment context = this;
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String regid = PushMessageInitializationHandling.getRegistrationId(getActivity());
                _sv.removeRegistrationAttributes(context, regid);
            }
        };
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
}
