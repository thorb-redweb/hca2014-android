package dk.redweb.Red_App.ViewControllers.Contest.BikeTracking;

import android.location.Location;
import dk.redweb.Red_App.Database.DbInterface;

import java.text.DecimalFormat;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 11/29/13
 * Time: 14:39
 */
public class BikeTrackerStats {

    private DbInterface _sql;

    private Location[] _locations;

    private double _distanceTravelled;
    public String DistanceTravelled(){return decimalFormatter.format(_distanceTravelled);}
    private double _averagespeed;
    public String AverageSpeed(){return decimalFormatter.format(_averagespeed);}
    private double _topspeed;
    public String TopSpeed(){return decimalFormatter.format(_topspeed);}

    private final static DecimalFormat decimalFormatter = new DecimalFormat("#.#");

    public BikeTrackerStats(DbInterface sql){
        _sql = sql;
    }

    public void updateNumbers(){
        _locations = _sql.BikeContest.getAll();
        if(_locations.length > 0){
            getTotalDistanceTravelled();
            getTopSpeed();
            getAverageSpeed();
        }
    }

    private void getTotalDistanceTravelled(){
        Location lastLoc = null;
        _distanceTravelled = 0.0;
        for (Location loc : _locations){
            if (lastLoc != null){
                _distanceTravelled += calculateDistanceBetweenTwoLocations(lastLoc, loc);
            }
            lastLoc = loc;
        }
    }

    private void getTopSpeed(){
        Location lastLoc = null;
        _topspeed = 0.0;
        for (Location loc : _locations){
            if (lastLoc != null){
                long startTime = lastLoc.getTime();
                long stopTime = loc.getTime();
                double elapsedTime = stopTime - startTime;
                double timeInHours = elapsedTime / (1000.0 * 60.0 * 60.0);

                double distance = calculateDistanceBetweenTwoLocations(lastLoc, loc);
                double distanceInKm = distance / 1000;

                double speed =  distanceInKm/timeInHours;

                if(speed > _topspeed){
                    _topspeed = speed;
                }
            }
            lastLoc = loc;
        }
    }

    private void getAverageSpeed(){

        long startTime = _locations[0].getTime();
        long stopTime = _locations[_locations.length-1].getTime();
        double elapsedTime = stopTime - startTime;
        double timeInHours = elapsedTime / (1000.0 * 60.0 * 60.0);
        double distanceInKm = _distanceTravelled / 1000;
        if(timeInHours == 0.0){
            _averagespeed = 0;
        }
        else{
            _averagespeed = distanceInKm / timeInHours;
        }
    }

    private double calculateDistanceBetweenTwoLocations(Location a, Location b) {
        double pk = (180/3.14169);
        double lat_a = a.getLatitude();
        double lng_a = a.getLongitude();
        double lat_b = b.getLatitude();
        double lng_b = b.getLongitude();

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000*tt;
    }
}
