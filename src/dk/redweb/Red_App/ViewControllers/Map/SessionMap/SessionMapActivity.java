package dk.redweb.Red_App.ViewControllers.Map.SessionMap;

import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.ViewControllers.Map.BaseMapFragmentActivity;
import dk.redweb.Red_App.ViewControllers.Map.MapMarkerInfoWindowAdapter;
import dk.redweb.Red_App.ViewModels.SessionVM;
import dk.redweb.Red_App.Views.NavBarBox;

import java.util.HashMap;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/25/13
 * Time: 9:39 AM
 */
public class SessionMapActivity extends BaseMapFragmentActivity {
    Marker _sessionMarker;
    Marker _userMarker;

    SessionVM _session;

    boolean _isOnCreateInitialized = false;

    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_mapview);
            super.onAfterContentViewCreated();

            Bundle extras = getIntent().getExtras();
            int sessionId = extras.getInt(EXTRA.SESSIONID);

            _session = _db.Sessions.getVMFromId(sessionId);

            String titleArray = _session.Venue() + "<>" + sessionId + "<>" + _childname;
            _sessionMarker = _googleMap.addMarker(new MarkerOptions().position(_session.Location()).title(titleArray).snippet(""));

            CameraUpdate center = CameraUpdateFactory.newLatLng(_standardCenter);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(_standardZoom);

            _googleMap.moveCamera(center);
            _googleMap.animateCamera(zoom);

            MapMarkerInfoWindowAdapter infoWindowAdapter = new MapMarkerInfoWindowAdapter(getLayoutInflater(), this);
            _googleMap.setInfoWindowAdapter(infoWindowAdapter);
            _googleMap.setOnInfoWindowClickListener(infoWindowAdapter);
            _isOnCreateInitialized = true;

        } catch (Exception e) {
            MyLog.e("Exception in SessionMapActivity.onCreate", e);

            //setContentView(R.layout.act_maps_unavailable);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        NavBarBox navBarBox = (NavBarBox)findViewById(R.id.navbar);
        HashMap<String, Integer> extras = new HashMap<String, Integer>();
        extras.put(EXTRA.SESSIONID, _session.SessionId());
        navBarBox.setUpButtonTargetForThisPage(_page,extras);
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

            b.include(_sessionMarker.getPosition());
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