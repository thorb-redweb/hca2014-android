package dk.redweb.hca2014.ViewControllers.Map.SessionMap;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.*;
import dk.redweb.hca2014.My;
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

    ImageView _imgDriving;
    ImageView _imgBiking;
    ImageView _imgWalking;

    boolean _isOnCreateInitialized = false;

    public SessionMapFragment(){
        super(null);
    }

    public SessionMapFragment(XmlNode page) {
        super(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        My.saveXmlPageInBundle(_page, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(_page == null){
            _page = My.loadXmlPageFromBundle(savedInstanceState);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try{
            super.onCreateView(inflater, container, R.layout.map_mapview);

            int sessionId = _page.getIntegerFromNode(EXTRA.SESSIONID);

            _session = _db.Sessions.getVMFromId(sessionId);

            _imgDriving = (ImageView)findViewById(R.id.imgDriving);
            _imgBiking = (ImageView)findViewById(R.id.imgBiking);
            _imgWalking = (ImageView)findViewById(R.id.imgWalking);
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

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(_session.TypeImage());
        _sessionMarker = _googleMap.addMarker(new MarkerOptions().position(_session.Location()).title(titleArray).snippet(snippet).icon(icon));

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
            BitmapDescriptor hereIcon = BitmapDescriptorFactory.fromResource(R.drawable.man);
            _userMarker = _googleMap.addMarker(new MarkerOptions().position(_userLatLng).title("Du er her").icon(hereIcon));

            LatLngBounds.Builder b = new LatLngBounds.Builder();

            b.include(_sessionMarker.getPosition());
            b.include(_userMarker.getPosition());

            LatLngBounds bounds = b.build();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 5);
            _googleMap.animateCamera(cameraUpdate);


            _imgDriving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDirections("driving");
                }
            });

            _imgBiking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDirections("bicycling");
                }
            });

            _imgWalking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDirections("walking");
                }
            });
        }
        else if(_isOnCreateInitialized){
            _userMarker.setPosition(_userLatLng);
        }
    }

    private void getDirections(String travelMode) {
        _sv.getDirections(this, travelMode, _userLatLng.latitude + "," + _userLatLng.longitude, _session.Latitude() + "," + _session.Longitude());
    }
}