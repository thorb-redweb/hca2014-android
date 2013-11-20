package dk.redweb.Red_App.ViewControllers.Settings.PushMessageInitializer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Interfaces.Delegate_uploadRegistrationAttributes;
import dk.redweb.Red_App.Network.PushMessages.PushMessageInitializationHandling;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.Views.NavBarBox;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/25/13
 * Time: 11:45
 */
public class PushMessageInitializerFragment extends BasePageFragment implements Delegate_uploadRegistrationAttributes{

    Boolean firstVisit;
    PushMessageInitializationHandling _pmHandler;
    private ProgressDialog _progressDialog;

    public PushMessageInitializerFragment(XmlNode page) {
        super(page);
        firstVisit = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflate, container, R.layout.page_pushmessageinitializer);

        _pmHandler = new PushMessageInitializationHandling(getActivity(),this);
        boolean hasInitialized = PushMessageInitializationHandling.checkInitialization(getActivity());

        if(hasInitialized){
            changeToNextPage();
            return null;
        }

        setAppearance();
        setText();

        EditText txtUserName = (EditText)findViewById(R.id.pushmessageinitializer_txtUserName);
        txtUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    closeKeyBoard();
                    addUserToDatabase();
                    return true;
                }
                return false;
            }
        });

        FlexibleButton flxSubmitButton = (FlexibleButton)findViewById(R.id.pushmessageinitializer_flxSubmit);
        flxSubmitButton.setOnClickListener(submitButtonOnClickListener());

        FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.pushmessageinitializer_flxBack);
        flxBackButton.setOnClickListener(backButtonOnClickListener());

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        navBarBox.setUpButtonTargetForThisPage(_page);
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.pushmessageinitializer_lnrMainView);
            helper.setViewBackgroundImageOrColor(lnrBackground,LOOK.PUSHMESSAGEINITIALIZER_BACKGROUNDIMAGE, LOOK.PUSHMESSAGEINITIALIZER_BACKGROUNDCOLOR,LOOK.GLOBAL_BACKCOLOR);

            TextView lblDescription = (TextView)findViewById(R.id.pushmessageinitializer_lblPageDescription);
            helper.setTextColor(lblDescription, LOOK.PUSHMESSAGEINITIALIZER_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(lblDescription, LOOK.PUSHMESSAGEINITIALIZER_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setTextStyle(lblDescription, LOOK.PUSHMESSAGEINITIALIZER_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setTextShadow(lblDescription, LOOK.PUSHMESSAGEINITIALIZER_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.PUSHMESSAGEINITIALIZER_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView lblUserName = (TextView)findViewById(R.id.pushmessageinitializer_lblUserName);
            helper.setTextColor(lblUserName, LOOK.PUSHMESSAGEINITIALIZER_LABELCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.setTextSize(lblUserName, LOOK.PUSHMESSAGEINITIALIZER_LABELSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.setTextStyle(lblUserName, LOOK.PUSHMESSAGEINITIALIZER_LABELSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.setTextShadow(lblUserName, LOOK.PUSHMESSAGEINITIALIZER_LABELSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.PUSHMESSAGEINITIALIZER_LABELSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            FlexibleButton flxSubmitButton = (FlexibleButton)findViewById(R.id.pushmessageinitializer_flxSubmit);
            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.pushmessageinitializer_flxBack);
            FlexibleButton[] buttons = new FlexibleButton[]{flxSubmitButton,flxBackButton};
            helper.setViewBackgroundImageOrColor(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONBACKGROUNDIMAGE, LOOK.PUSHMESSAGEDETAIL_BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.setFlexibleButtonImage(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONICON);
            helper.setFlexibleButtonTextColor(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.setFlexibleButtonTextSize(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.setFlexibleButtonTextStyle(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.setFlexibleButtonTextShadow(buttons, LOOK.PUSHMESSAGEINITIALIZER_BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.PUSHMESSAGEINITIALIZER_BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);


        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for PushMessageDetail", e);
        }
    }

    private void setText(){
        try{
            TextHelper helper = new TextHelper(_view, _name, _xml);

            helper.setText(R.id.pushmessageinitializer_lblPageDescription, TEXT.PUSHMESSAGEINITIALIZER_PAGEDESCRIPTION, DEFAULTTEXT.PUSHMESSAGEINITIALIZER_PAGEDESCRIPTION);

            helper.setText(R.id.pushmessageinitializer_lblUserName, TEXT.PUSHMESSAGEINITIALIZER_NAMELABEL, DEFAULTTEXT.PUSHMESSAGEINITIALIZER_NAMELABEL);

            helper.setFlexibleButtonText(R.id.pushmessageinitializer_flxSubmit, TEXT.PUSHMESSAGEINITIALIZER_SUBMITBUTTON, DEFAULTTEXT.PUSHMESSAGEINITIALIZER_SUBMITBUTTON);
            helper.setFlexibleButtonText(R.id.pushmessageinitializer_flxBack, TEXT.PUSHMESSAGEINITIALIZER_BACKBUTTON, DEFAULTTEXT.PUSHMESSAGEINITIALIZER_BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting page text", e);
        }
    }

    private View.OnClickListener submitButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                addUserToDatabase();
            }
        };
    }

    private View.OnClickListener backButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                getFragmentManager().popBackStack();
            }
        };
    }

    private void closeKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getActivity().getCurrentFocus();
        inputManager.hideSoftInputFromWindow((null == currentFocus) ? null : currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void addUserToDatabase(){
        EditText txtName = (EditText)findViewById(R.id.pushmessageinitializer_txtUserName);
        String username = txtName.getText().toString();
        if(username.length() < 1){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Navn påkrævet");
            alertDialog.setMessage("Skriv venligst dit navn");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Luk", closeDialogOnClickListener());
            alertDialog.show();
        }
        else
        {
            _progressDialog = new ProgressDialog(getActivity());
            _progressDialog.setTitle("Gemmer dine kontaktinformationer");
            _progressDialog.setMessage("Gemmer...");
            _progressDialog.show();

            _pmHandler.initializePushService(username);
        }
    }

    @Override
    public void returnFromUploadToServer(String result) {
        _progressDialog.dismiss();
        changeToNextPage();
    }

    private void changeToNextPage(){
        try {
            if(firstVisit){
                XmlNode nextpage = _xml.getPage(_childname);
                NavController.changePageWithXmlNode(nextpage, getActivity());
                firstVisit = false;
            }
            else{
                getFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            MyLog.e("Exception when changing page to childpage", e);
            errorOccured("Der er en fejl i appen. Kontakt venligst udvikleren.");
        }
    }

    @Override
    public void errorOccured(String errorMessage) {
        String alertMessage = "Der er sket en fejl under din tilmelding";

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(alertMessage);
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Prøv Igen", closeDialogOnClickListener());
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afbryd", closeAppOnClickListener());
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

    private DialogInterface.OnClickListener closeAppOnClickListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().popBackStack();
            }
        };
    }
}
