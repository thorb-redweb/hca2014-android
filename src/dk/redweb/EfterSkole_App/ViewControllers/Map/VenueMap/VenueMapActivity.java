package dk.redweb.EfterSkole_App.ViewControllers.Map.VenueMap;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import dk.redweb.EfterSkole_App.MyLog;
import dk.redweb.EfterSkole_App.R;
import dk.redweb.EfterSkole_App.StaticNames.EXTRA;
import dk.redweb.EfterSkole_App.ViewControllers.Map.BaseMapFragment;
import dk.redweb.EfterSkole_App.ViewModels.VenueVM;
import dk.redweb.EfterSkole_App.Views.NavBarBox;
import dk.redweb.EfterSkole_App.XmlHandling.XmlNode;

import java.util.HashMap;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/25/13
 * Time: 9:39 AM
 */
public class VenueMapActivity extends BaseMapFragment {
    Marker _venueMarker;
    Marker _userMarker;

    VenueVM _venue;

    boolean _isOnCreateInitialized = false;

    public VenueMapActivity(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            super.onCreateView(inflater, container, R.layout.map_mapview);

            int venueid = _page.getIntegerFromNode(EXTRA.VENUEID);

            _venue = _db.Venues.getVMFromId(venueid);

        } catch (Exception e) {
            MyLog.e("Exception in SessionMapActivity.onCreate", e);

            //setContentView(R.layout.act_maps_unavailable);
        }
        return _view;
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)getActivity().findViewById(R.id.navbar);
        HashMap<String, Integer> extras = new HashMap<String, Integer>();
        extras.put(EXTRA.VENUEID, _venue.VenueId());
        navBarBox.setUpButtonTargetForThisPage(_page,extras);
    }

    @Override
    public void onStart(){
        super.onStart();

        String titleArray = _venue.Name();
        _venueMarker = _googleMap.addMarker(new MarkerOptions().position(_venue.Location()).title(titleArray));

        CameraUpdate center = CameraUpdateFactory.newLatLng(_standardCenter);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(_standardZoom);

        _googleMap.moveCamera(center);
        _googleMap.animateCamera(zoom);

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

            b.include(_venueMarker.getPosition());
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