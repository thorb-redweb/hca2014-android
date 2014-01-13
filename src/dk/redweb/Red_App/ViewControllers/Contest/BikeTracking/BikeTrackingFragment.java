package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/29/13
 * Time: 14:22
 */
public class BikeTrackingFragment extends BasePageFragment{

    private BikeTrackerStats _bikeTrackerStats;

    private State _state;

    private enum State {PRELAUNCH, RUNNING, STOPPED;};

    AlarmManager _alarmManager;
    PendingIntent _locationGetterIntent;
    Handler _handler = new Handler();
    boolean _viewUpdaterShouldRun;

    public BikeTrackingFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_biketracking);
        setAppearance();
        setText();

        _state = State.PRELAUNCH;
        _bikeTrackerStats = new BikeTrackerStats(_db);



        FlexibleButton flxTrackerStateButton = (FlexibleButton)findViewById(R.id.biketracking_flxTrackerStateButton);
        flxTrackerStateButton.setOnClickListener(getTrackerStateButtonListener());

        FlexibleButton flxUploadButton = (FlexibleButton)findViewById(R.id.biketracking_flxUploadButton);
        flxUploadButton.setOnClickListener(getUploadButtonListener());

        setupBackButton();

        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();
        runViewUpdater();
    }

    @Override
    public void onPause(){
        stopViewUpdater();
        super.onPause();
    }

    public void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;

            helper.setViewBackgroundTileImageOrColor(_view, LOOK.BACKGROUNDIMAGE, LOOK.BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView lblDistanceLabel = (TextView)findViewById(R.id.biketracking_lblDistanceLabel);
            TextView lblSpeedLabel = (TextView)findViewById(R.id.biketracking_lblSpeedLabel);
            TextView lblTopSpeedLabel = (TextView)findViewById(R.id.biketracking_lblTopSpeedLabel);
            TextView[] labels = {lblDistanceLabel,lblSpeedLabel,lblTopSpeedLabel};
            helper.TextView.setTextColor(labels, LOOK.BIKETRACKING_LABELCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(labels, LOOK.BIKETRACKING_LABELSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setTextStyle(labels, LOOK.BIKETRACKING_LABELSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setTextShadow(labels, LOOK.BIKETRACKING_LABELSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.BIKETRACKING_LABELSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView lblDistanceData = (TextView)findViewById(R.id.biketracking_lblDistanceData);
            TextView lblSpeedData = (TextView)findViewById(R.id.biketracking_lblSpeedData);
            TextView lblTopSpeedData = (TextView)findViewById(R.id.biketracking_lblTopSpeedData);
            TextView lblStatus = (TextView)findViewById(R.id.biketracking_lblStatus);
            TextView[] datalabels = {lblDistanceData,lblSpeedData,lblTopSpeedData, lblStatus};
            helper.TextView.setTextColor(datalabels, LOOK.BIKETRACKING_DATACOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(datalabels, LOOK.BIKETRACKING_DATASIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setTextStyle(datalabels, LOOK.BIKETRACKING_DATASTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setTextShadow(datalabels, LOOK.BIKETRACKING_DATASHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.BIKETRACKING_DATASHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            FlexibleButton flxActionButton = (FlexibleButton)findViewById(R.id.biketracking_flxTrackerStateButton);
            helper.setViewBackgroundImageOrColor(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONBACKGROUNDIMAGE, LOOK.BIKETRACKING_STARTBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONICON);
            helper.FlexButton.setTextColor(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxActionButton, LOOK.BIKETRACKING_STARTBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BIKETRACKING_STARTBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            FlexibleButton flxUploadButton = (FlexibleButton)findViewById(R.id.biketracking_flxUploadButton);
            helper.setViewBackgroundImageOrColor(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONBACKGROUNDIMAGE, LOOK.BIKETRACKING_UPLOADBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONICON);
            helper.FlexButton.setTextColor(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxUploadButton, LOOK.BIKETRACKING_UPLOADBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BIKETRACKING_UPLOADBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            FlexibleButton flxBackButton = (FlexibleButton)findViewById(R.id.flxBackButton);
            helper.setViewBackgroundImageOrColor(flxBackButton, LOOK.BACKBUTTONBACKGROUNDIMAGE, LOOK.BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(flxBackButton, LOOK.BACKBUTTONICON);
            helper.FlexButton.setTextColor(flxBackButton, LOOK.BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
            helper.FlexButton.setTextSize(flxBackButton, LOOK.BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.FlexButton.setTextStyle(flxBackButton, LOOK.BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.FlexButton.setTextShadow(flxBackButton, LOOK.BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR,
                    LOOK.BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
        }
        catch (Exception e){
            MyLog.e("Exception when setting appearance", e);
        }
    }

    public void setText(){
        try {
            TextHelper helper = _textHelper;
            helper.setText(R.id.biketracking_lblDistanceLabel,TEXT.BIKETRACKING_DISTANCELABEL,DEFAULTTEXT.BIKETRACKING_DISTANCELABEL);
            helper.setText(R.id.biketracking_lblSpeedLabel,TEXT.BIKETRACKING_AVSPEEDLABEL,DEFAULTTEXT.BIKETRACKING_AVSPEEDLABEL);
            helper.setText(R.id.biketracking_lblTopSpeedLabel,TEXT.BIKETRACKING_TOPSPEEDLABEL,DEFAULTTEXT.BIKETRACKING_TOPSPEEDLABEL);
            helper.setText(R.id.biketracking_lblStatus,TEXT.BIKETRACKING_STOPPED,DEFAULTTEXT.BIKETRACKING_STOPPED);
            helper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_STARTBUTTON, DEFAULTTEXT.BIKETRACKING_STARTBUTTON);
            helper.setFlexibleButtonText(R.id.biketracking_flxUploadButton, TEXT.BIKETRACKING_UPLOADBUTTON, DEFAULTTEXT.BIKETRACKING_UPLOADBUTTON);
            helper.setFlexibleButtonText(R.id.flxBackButton, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    public View.OnClickListener getTrackerStateButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonAccordingToState();
            }
        };
    }

    public View.OnClickListener getUploadButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView();
                _db.BikeContest.dropAllEntries();
            }
        };
    }

    public void handleButtonAccordingToState(){
        switch (_state){
            case PRELAUNCH:
                startLocationClient();
                break;
            case RUNNING:
                stopLocationClient();
                break;
            case STOPPED:
                ContinueLocationClient();
                break;
        }
    }

    public void startLocationClient(){
       startLocationUpdating();

        try{
            _textHelper.setText(R.id.biketracking_lblStatus, TEXT.BIKETRACKING_RUNNING, DEFAULTTEXT.BIKETRACKING_RUNNING);
            _textHelper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Running' text on lblTrackerRunning", e);
        }
        _state = State.RUNNING;
    }

    public void stopLocationClient(){
        stopLocationUpdating();

        try{
            _textHelper.setText(R.id.biketracking_lblStatus, TEXT.BIKETRACKING_STOPPED, DEFAULTTEXT.BIKETRACKING_STOPPED);
            _textHelper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_CONTINUEBUTTON, DEFAULTTEXT.BIKETRACKING_CONTINUEBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Not Running' text on lblTrackerRunning", e);
        }
        _state = State.STOPPED;
    }

    public void ContinueLocationClient(){
        startLocationUpdating();

        try{
            _textHelper.setText(R.id.biketracking_lblStatus, TEXT.BIKETRACKING_RUNNING, DEFAULTTEXT.BIKETRACKING_RUNNING);
            _textHelper.setText(R.id.biketracking_lblStatus, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Not Running' text on lblTrackerRunning", e);
        }
        _state = State.RUNNING;
    }

    private void startLocationUpdating(){
        if(_alarmManager == null || _locationGetterIntent == null) {
            initializeAlarmManager();
        }
        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                30000, _locationGetterIntent);
    }

    private void stopLocationUpdating(){
        if(_alarmManager == null || _locationGetterIntent == null) {
            initializeAlarmManager();
        }

        _alarmManager.cancel(_locationGetterIntent);

    }

    private void initializeAlarmManager(){
        _alarmManager = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
        Intent i = new Intent(getActivity(), BikeContestReceiver.class);
        _locationGetterIntent = PendingIntent.getBroadcast(getActivity(), 5000, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void runViewUpdater(){
        if(!_viewUpdaterShouldRun){
            _viewUpdaterShouldRun = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (_viewUpdaterShouldRun) {
                        try {
                            _handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    updateView();
                                }
                            });
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            MyLog.e("ViewUpdater thread suffered an exception", e);
                        }
                    }
                }
            }).start();
        }
    }

    private void updateView(){
        _bikeTrackerStats.updateNumbers();

        TextView lblAvSpeed = (TextView)findViewById(R.id.biketracking_lblSpeedData);
        lblAvSpeed.setText(_bikeTrackerStats.AverageSpeed() + " km/t");

        TextView lblTopSpeed = (TextView)findViewById(R.id.biketracking_lblTopSpeedData);
        lblTopSpeed.setText(_bikeTrackerStats.TopSpeed() + " km/t");

        TextView lblDistance = (TextView)findViewById(R.id.biketracking_lblDistanceData);
        lblDistance.setText(_bikeTrackerStats.DistanceTravelled() + " m");
    }

    private void stopViewUpdater(){
        _viewUpdaterShouldRun = false;
    }
}
