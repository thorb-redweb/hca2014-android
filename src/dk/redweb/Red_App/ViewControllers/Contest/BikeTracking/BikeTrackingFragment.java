package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public BikeTrackingFragment(XmlNode page) {
        super(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_biketracking);
        setAppearance();
        setText();

        _bikeTracker = new BikeTracker(this, _app, getActivity());

        FlexibleButton flxStartButton = (FlexibleButton)findViewById(R.id.biketracking_flxStartButton);
        flxStartButton.setOnClickListener(getStartButtonListener());

        FlexibleButton flxStopButton = (FlexibleButton)findViewById(R.id.biketracking_flxStopButton);
        flxStopButton.setOnClickListener(getStopButtonListener());

        setupBackButton();

        return _view;
    }

    public void setAppearance(){

    }

    public void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name,_xml);
            helper.setFlexibleButtonText(R.id.biketracking_flxStartButton, TEXT.BIKETRACKING_STARTBUTTON, DEFAULTTEXT.BIKETRACKING_STARTBUTTON);
            helper.setFlexibleButtonText(R.id.biketracking_flxStopButton, TEXT.BIKETRACKING_STOPBUTTON, DEFAULTTEXT.BIKETRACKING_STOPBUTTON);
            helper.setFlexibleButtonText(R.id.flxBackButton, TEXT.BACKBUTTON, DEFAULTTEXT.BACKBUTTON);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    public View.OnClickListener getStartButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bikeTracker.Start();
            }
        };
    }

    public View.OnClickListener getStopButtonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bikeTracker.Stop();
            }
        };
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
