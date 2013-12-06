package dk.redweb.Red_App.ViewControllers.Contest.StepTracking;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import dk.redweb.Red_App.RedEventApplication;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/2/13
 * Time: 15:22
 */
public class StepTracker implements StepListener{

    private RedEventApplication _app;

    private SensorManager _sensorManager;
    private Sensor _sensor;
    private StepDetector _stepDetector;

    private StepTrackerListener _listener;

    private int _numberSteps;

    public StepTracker(StepTrackerListener listener, RedEventApplication app){
        _listener = listener;
        _app = app;
    }

    public void startStepCounter(){
        _stepDetector = new StepDetector(this, 12);
        _sensorManager = (SensorManager)_app.getSystemService(_app.SENSOR_SERVICE);

        _sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _sensorManager.registerListener(_stepDetector,_sensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopStepCounter(){

    }

    @Override
    public void onStep() {
        _numberSteps++;
        _listener.numberStepsUpdated(String.valueOf(_numberSteps));
    }

    @Override
    public void passValue() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
