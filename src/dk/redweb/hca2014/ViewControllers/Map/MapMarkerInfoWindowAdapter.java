package dk.redweb.hca2014.ViewControllers.Map;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.RedEventApplication;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 2:41 PM
 */
public class MapMarkerInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    FragmentActivity _parentActivity;
    LayoutInflater _inflater;
    XmlStore _xml;

    public MapMarkerInfoWindowAdapter(LayoutInflater inflater, FragmentActivity parentActivity){
        _inflater = inflater;
        _parentActivity = parentActivity;
        _xml = ((RedEventApplication)parentActivity.getApplication()).getXmlStore();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infowindow = _inflater.inflate(R.layout.mapmarker_infowindow, null);

        TextView txtTitle = (TextView)infowindow.findViewById(R.id.mapmarker_title);
        TextView txtBody = (TextView)infowindow.findViewById(R.id.mapmarker_body);

        String[] input = marker.getTitle().split("<>");
        String title = input[0];

        txtTitle.setText(title);
        txtBody.setText(marker.getSnippet());

        return infowindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String[] input = marker.getTitle().split("<>");
        if(input.length < 3){
            return;
        }
        Integer sessionId = Integer.valueOf(input[1]);
        String childname = input[2];

        try {
            XmlNode childPage = _xml.getPage(childname);
            XmlNode nextPage = childPage.deepClone();
            nextPage.addChildToNode(PAGE.SESSIONID,sessionId);
            NavController.changePageWithXmlNode(nextPage, _parentActivity);
        } catch (Exception e) {
            MyLog.e("Exception in MapMarkerInfoWindowAdapter:onInfoWindowClick", e);
        }
    }
}
