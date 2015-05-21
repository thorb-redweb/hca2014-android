package dk.redweb.hca2014.Network;

import android.os.AsyncTask;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.ViewControllers.Map.BaseMapFragment;

/**
 * package: dk.redweb.hca2014.Network
 * copyright: Copyright (C) 2005 - 2014 redweb ApS. All rights reserved.
 * license: GNU General Public License version 2 or later.
 */
public class Handler_GetDirections extends AsyncTask<String, Void, String> {

    BaseMapFragment _delegate;
    String _travelMode;
    String _origin;
    String _destination;

    public Handler_GetDirections(BaseMapFragment delegate, String travelMode, String origin, String destination){
        _delegate = delegate;
        _travelMode = travelMode;
        _origin = origin;
        _destination = destination;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?mode=" + _travelMode + "&origin=" + _origin + "&destination=" + _destination;

        MyLog.v("Start Directions Download in Handler_GetDirections with url: " + url);

        try{
            String result = HttpHandler.GetString(url);
            return result;
        }
        catch (Exception e){
            MyLog.e("Exception when getting update from server", e);
            return "Error: " + e.getMessage();
        }
    }

    protected void onPostExecute(String result){
        if(result.length() >= 6 && result.substring(0,6).equals("Error:")){
            _delegate.errorOccured(result);
            return;
        }
        MyLog.d("Update download finished");
        _delegate.returnWithDirections(result);
    }
}
