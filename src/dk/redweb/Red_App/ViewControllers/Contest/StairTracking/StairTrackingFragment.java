package dk.redweb.Red_App.ViewControllers.Contest.StairTracking;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewControllers.Contest.StepTracking.StepTracker;
import dk.redweb.Red_App.ViewControllers.Contest.StepTracking.StepTrackerListener;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/2/13
 * Time: 15:20
 */
public class StairTrackingFragment extends BasePageFragment {

    public Chronometer _chronometer;

    private State _state;
    private enum State {PRELAUNCH, RUNNING, STOPPED;};

    public StairTrackingFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_stairtracking);
        setAppearance();
        setText();

        _state = State.PRELAUNCH;
        _chronometer = (Chronometer)findViewById(R.id.stairtracking_chronometer);

        FlexibleButton flxTrackerStateButton = (FlexibleButton)findViewById(R.id.stairtracking_flxTrackerStateButton);
        flxTrackerStateButton.setOnClickListener(getTrackerStateButtonListener());

        setupBackButton();


        return _view;
    }

    public void setAppearance(){

    }

    public void setText(){
        try{
            _textHelper.setText(R.id.stairtracking_lblTrackerRunning, TEXT.BIKETRACKING_STOPPED, DEFAULTTEXT.BIKETRACKING_STOPPED);
            _textHelper.setFlexibleButtonText(R.id.stairtracking_flxTrackerStateButton, TEXT.BIKETRACKING_STARTBUTTON, DEFAULTTEXT.BIKETRACKING_STARTBUTTON);
            _textHelper.setFlexibleButtonText(R.id.flxBackButton, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting static text for StairTrackingFragment", e);
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

    public void handleButtonAccordingToState(){
        switch (_state){
            case PRELAUNCH:
                startStepCounter();
                break;
            case RUNNING:
                stopStepCounter();
                break;
        }
    }

    public void startStepCounter(){
        _chronometer.start();
        try{
            _textHelper.setText(R.id.stairtracking_lblTrackerRunning, TEXT.BIKETRACKING_RUNNING, DEFAULTTEXT.BIKETRACKING_RUNNING);
            _textHelper.setFlexibleButtonText(R.id.stairtracking_flxTrackerStateButton, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Running' text on lblTrackerRunning", e);
        }
        _state = State.RUNNING;
    }

    public void stopStepCounter(){
        _chronometer.stop();
        try{
            _textHelper.setText(R.id.stairtracking_lblTrackerRunning, TEXT.BIKETRACKING_STOPPED, DEFAULTTEXT.BIKETRACKING_STOPPED);
            _textHelper.setFlexibleButtonText(R.id.stairtracking_flxTrackerStateButton, TEXT.BIKETRACKING_CONTINUEBUTTON, DEFAULTTEXT.BIKETRACKING_CONTINUEBUTTON);
            findViewById(R.id.stairtracking_flxTrackerStateButton).setVisibility(View.GONE);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Not Running' text on lblTrackerRunning", e);
        }
        _state = State.STOPPED;
    }
}
