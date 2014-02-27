package dk.redweb.hca2014.ViewControllers.Map.OverviewMap;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.ViewControllers.Map.BaseMapFragment;
import dk.redweb.hca2014.ViewControllers.Map.MapMarker;
import dk.redweb.hca2014.ViewControllers.Map.MapMarkerInfoWindowAdapter;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 3:14 PM
 */
public class OverviewMapFragment extends BaseMapFragment {
    ArrayList<Marker> _locationMarkers;
    Marker _userMarker;

    boolean _isOnCreateInitialized = false;

    public OverviewMapFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            super.onCreateView(inflater, container, R.layout.map_mapview);

            _locationMarkers = new ArrayList<Marker>();

        } catch (Exception e) {
            MyLog.e("Exception in MainMapActivity.onCreate", e);

            //setContentView(R.layout.act_maps_unavailable);
        }
        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        navBarBox.setUpButtonTargetForThisPage(_page);
    }

    @Override
    public void onStart(){
        super.onStart();

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

        MapMarkerInfoWindowAdapter infoWindowAdapter = new MapMarkerInfoWindowAdapter(getActivity().getLayoutInflater(), getActivity());
        _googleMap.setInfoWindowAdapter(infoWindowAdapter);
        _googleMap.setOnInfoWindowClickListener(infoWindowAdapter);

        _isOnCreateInitialized = true;
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
