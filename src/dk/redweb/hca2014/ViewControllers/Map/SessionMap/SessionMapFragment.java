package dk.redweb.hca2014.ViewControllers.Map.SessionMap;

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
import dk.redweb.hca2014.StaticNames.EXTRA;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.Map.BaseMapFragment;
import dk.redweb.hca2014.ViewControllers.Map.MapMarkerInfoWindowAdapter;
import dk.redweb.hca2014.ViewModels.SessionVM;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/25/13
 * Time: 9:39 AM
 */
public class SessionMapFragment extends BaseMapFragment {
    Marker _sessionMarker;
    Marker _userMarker;

    SessionVM _session;

    boolean _isOnCreateInitialized = false;

    public SessionMapFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            super.onCreateView(inflater, container, R.layout.map_mapview);

            int sessionId = _page.getIntegerFromNode(EXTRA.SESSIONID);

            _session = _db.Sessions.getVMFromId(sessionId);

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

        XmlNode navBarPage = _page.deepClone();
        try {
            XmlNode parentParameters = navBarPage.addNodeToNode(PAGE.PARENTPARAMETERS);
            parentParameters.addChildToNode(PAGE.SESSIONID, _session.SessionId());
        } catch (InvalidPropertiesFormatException e) {
            MyLog.e("Exception when creatimg parentParameters on up-navigation helper page", e);
        }
        navBarBox.setUpButtonTargetForThisPage(navBarPage);
    }

    @Override
    public void onStart(){
        super.onStart();

        String titleArray = _session.Title() + "<>" + _session.SessionId() + "<>" + _childname;
        String snippet = _session.StartTimeAsString();

        _sessionMarker = _googleMap.addMarker(new MarkerOptions().position(_session.Location()).title(titleArray).snippet(snippet));

        CameraUpdate center = CameraUpdateFactory.newLatLng(_standardCenter);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(_standardZoom);

        _googleMap.moveCamera(center);
        _googleMap.animateCamera(zoom);

        MapMarkerInfoWindowAdapter infoWindowAdapter = new MapMarkerInfoWindowAdapter(getActivity().getLayoutInflater(), getActivity());
        _googleMap.setInfoWindowAdapter(infoWindowAdapter);
        _googleMap.setOnInfoWindowClickListener(infoWindowAdapter);
        _isOnCreateInitialized = true;

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