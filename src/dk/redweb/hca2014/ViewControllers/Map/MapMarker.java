package dk.redweb.hca2014.ViewControllers.Map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 2:53 PM
 */
public class MapMarker {
    public int SessionId;
    public String Name;
    public String Text;
    public LatLng Location;

    public double Latitude(){
        return Location.latitude;
    }

    public double Longitude(){
        return Location.longitude;
    }
}
