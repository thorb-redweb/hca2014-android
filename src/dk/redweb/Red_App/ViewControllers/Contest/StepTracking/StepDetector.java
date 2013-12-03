package dk.redweb.Red_App.ViewControllers.Contest.StepTracking;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import dk.redweb.Red_App.MyLog;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/2/13
 * Time: 15:22
 */
public class StepDetector implements SensorEventListener{

    private float _limit = 10;
    private float _lastValues[] = new float[2];
    private float _scale[] = new float[2];
    private float _yOffset;

    private float _lastDirections[] = new float[3*2];
    private float _lastExtremes[][] = {new float[3*2], new float[3*2]};
    private float _lastDiff[] = new float[3*2];
    private int _lastMatch = -1;

    private StepListener _listener;

    public StepDetector(StepListener listener, float sensitivity){
        _listener = listener;
        _limit = sensitivity;

        int h = 480;
        _yOffset = h * 0.5f;
        _scale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        _scale[1] = - (h * 0.5f * (1.0f / SensorManager.MAGNETIC_FIELD_EARTH_MAX));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }
            else {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = _yOffset + event.values[i] * _scale[1];
                        vSum += v;
                    }
                    float v = vSum / 3;

                    float direction = (v > _lastValues[0] ? 1 : (v < _lastValues[0] ? -1 : 0));
                    if (direction == - _lastDirections[0]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        _lastExtremes[extType][0] = _lastValues[0];
                        float diff = Math.abs(_lastExtremes[extType][0] - _lastExtremes[1 - extType][0]);

                        if (diff > _limit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (_lastDiff[0]*2/3);
                            boolean isPreviousLargeEnough = _lastDiff[0] > (diff/3);
                            boolean isNotContra = (_lastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                MyLog.i("step");
                                _listener.onStep();
                                _lastMatch = extType;
                            }
                            else {
                                _lastMatch = -1;
                            }
                        }
                        _lastDiff[0] = diff;
                    }
                    _lastDirections[0] = direction;
                    _lastValues[0] = v;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
