package dk.redweb.Red_App.ViewControllers.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.RedEventApplication;
import dk.redweb.Red_App.StaticNames.EXTRA;
import dk.redweb.Red_App.ViewControllers.Session.SessionDetail.SessionDetailActivity;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 2:41 PM
 */
public class MapMarkerInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    Activity context;
    LayoutInflater inflater;
    XmlStore _xml;

    public MapMarkerInfoWindowAdapter(LayoutInflater inflater, Activity context){
        this.inflater = inflater;
        this.context = context;
        _xml = ((RedEventApplication)context.getApplication()).getXmlStore();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infowindow = inflater.inflate(R.layout.mapmarker_infowindow, null);

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
        Integer sessionId = Integer.valueOf(input[1]);
        String childname = input[2];

        try {
            XmlNode selectedPage = _xml.getPage(childname);
            Intent sessionDetailIntent = new Intent(context, SessionDetailActivity.class);
            sessionDetailIntent.putExtra(EXTRA.SESSIONID, sessionId);
            sessionDetailIntent.putExtra(EXTRA.PAGE, selectedPage);
            context.startActivity(sessionDetailIntent);
        } catch (Exception e) {
            MyLog.e("Exception in MapMarkerInfoWindowAdapter:onInfoWindowClick", e);
        }
    }
}
