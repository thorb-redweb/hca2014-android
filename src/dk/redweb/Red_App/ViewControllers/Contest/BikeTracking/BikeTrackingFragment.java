package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dk.redweb.Red_App.Interfaces.Delegate_biketracker;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.DEFAULTTEXT;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.TextHelper;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/29/13
 * Time: 14:22
 */
public class BikeTrackingFragment extends BasePageFragment implements Delegate_biketracker{

    private BikeTracker _bikeTracker;

    private State _state;

    private enum State {PRELAUNCH, RUNNING, STOPPED;};

    public BikeTrackingFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_biketracking);
        setAppearance();
        setText();

        _state = State.PRELAUNCH;
        _bikeTracker = new BikeTracker(this, _app, getActivity());

        FlexibleButton flxTrackerStateButton = (FlexibleButton)findViewById(R.id.biketracking_flxTrackerStateButton);
        flxTrackerStateButton.setOnClickListener(getTrackerStateButtonListener());

        setupBackButton();

        return _view;
    }

    public void setAppearance(){

    }

    public void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name,_xml);
            helper.setText(R.id.biketracking_lblTrackerRunning,TEXT.BIKETRACKING_STOPPED,DEFAULTTEXT.BIKETRACKING_STOPPED);
            helper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_STARTBUTTON, DEFAULTTEXT.BIKETRACKING_STARTBUTTON);
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
        _bikeTracker.startLocationClient();
        try{
            _textHelper.setText(R.id.biketracking_lblTrackerRunning, TEXT.BIKETRACKING_RUNNING, DEFAULTTEXT.BIKETRACKING_RUNNING);
            _textHelper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Running' text on lblTrackerRunning", e);
        }
        _state = State.RUNNING;
    }

    public void stopLocationClient(){
        _bikeTracker.stopLocationClient();
        try{
            _textHelper.setText(R.id.biketracking_lblTrackerRunning, TEXT.BIKETRACKING_STOPPED, DEFAULTTEXT.BIKETRACKING_STOPPED);
            _textHelper.setFlexibleButtonText(R.id.biketracking_flxTrackerStateButton, TEXT.BIKETRACKING_CONTINUEBUTTON, DEFAULTTEXT.BIKETRACKING_CONTINUEBUTTON);
            findViewById(R.id.biketracking_flxTrackerStateButton).setVisibility(View.GONE);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Not Running' text on lblTrackerRunning", e);
        }
        _state = State.STOPPED;
    }

    public void ContinueLocationClient(){
        _bikeTracker.stopLocationClient();
        try{
            _textHelper.setText(R.id.biketracking_lblTrackerRunning, TEXT.BIKETRACKING_RUNNING, DEFAULTTEXT.BIKETRACKING_RUNNING);
            _textHelper.setText(R.id.biketracking_lblTrackerRunning, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting 'Not Running' text on lblTrackerRunning", e);
        }
        _state = State.RUNNING;
    }

    @Override
    public void averageSpeedUpdated(String averageSpeed) {
        TextView lblAvSpeed = (TextView)findViewById(R.id.biketracking_lblSpeedData);
        lblAvSpeed.setText(_bikeTracker.getAverageSpeed() + " km/t");
    }

    @Override
    public void topSpeedUpdated(String topSpeed) {
        TextView lblTopSpeed = (TextView)findViewById(R.id.biketracking_lblTopSpeedData);
        lblTopSpeed.setText(_bikeTracker.getTopSpeed() + " km/t");
    }

    @Override
    public void distanceUpdated(String distance) {
        TextView lblDistance = (TextView)findViewById(R.id.biketracking_lblDistanceData);
        lblDistance.setText(_bikeTracker.getTotalDistanceTravelled() + " m");
    }
}
