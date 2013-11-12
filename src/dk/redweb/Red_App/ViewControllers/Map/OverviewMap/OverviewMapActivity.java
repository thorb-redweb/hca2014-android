package dk.redweb.Red_App.ViewControllers.Map.OverviewMap;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.ViewControllers.Map.BaseMapFragmentActivity;
import dk.redweb.Red_App.ViewControllers.Map.MapMarker;
import dk.redweb.Red_App.ViewControllers.Map.MapMarkerInfoWindowAdapter;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 3:14 PM
 */
public class OverviewMapActivity extends BaseMapFragmentActivity {
    ArrayList<Marker> _locationMarkers;
    Marker _userMarker;

    boolean _isOnCreateInitialized = false;

    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_mapview);
            super.onAfterContentViewCreated();

            _locationMarkers = new ArrayList<Marker>();
            MapMarker[] mapMarkers = _db.MapMarkers.getAll();

            for(int i = 0; i < mapMarkers.length; i++)
            {
                LatLng loc = mapMarkers[i].Location;
                String titleArray = mapMarkers[i].Name + "<>" + mapMarkers[i].SessionId + "<>" + _childname;
                Marker marker = _googleMap.addMarker(new MarkerOptions().position(loc).title(titleArray).snippet(mapMarkers[i].Text));
                _locationMarkers.add(marker);
            }

            CameraUpdate center = CameraUpdateFactory.newLatLng(_standardCenter);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(_standardZoom);

            _googleMap.moveCamera(center);
            _googleMap.animateCamera(zoom);

            MapMarkerInfoWindowAdapter infoWindowAdapter = new MapMarkerInfoWindowAdapter(getLayoutInflater(), this);
            _googleMap.setInfoWindowAdapter(infoWindowAdapter);
            _googleMap.setOnInfoWindowClickListener(infoWindowAdapter);
            _isOnCreateInitialized = true;

        } catch (Exception e) {
            MyLog.e("Exception in MainMapActivity.onCreate", e);

            //setContentView(R.layout.act_maps_unavailable);
        }
    }

    @Override
    public void onConnected(Bundle bundle){
        super.onConnected(bundle);
        if(_userLatLng != _standardCenter){
            positionUserIcon();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        super.onLocationChanged(location);
        positionUserIcon();
    }

    private void positionUserIcon(){
        if(_userMarker == null && _isOnCreateInitialized)                                      {
            BitmapDescriptor hereIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
            _userMarker = _googleMap.addMarker(new MarkerOptions().position(_userLatLng).title("Du er her").icon(hereIcon));

            LatLngBounds.Builder b = new LatLngBounds.Builder();

            for(Marker marker : _locationMarkers)
            {
                b.include(marker.getPosition());
            }
            b.include(_userMarker.getPosition());

            LatLngBounds bounds = b.build();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 5);
            _googleMap.animateCamera(cameraUpdate);
        }
        else if(_isOnCreateInitialized){
            _userMarker.setPosition(_userLatLng);
        }
    }
}
