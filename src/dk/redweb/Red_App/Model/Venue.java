package dk.redweb.Red_App.Model;

import dk.redweb.Red_App.Interfaces.IDbInterface;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:15 AM
 */
public class Venue {
    private IDbInterface Idb;

    public int VenueId;
    public String Title;
    public String Description;
    public String Street;
    public String City;
    public Double Latitude;
    public Double Longitude;
    public String ImagePath;
    private ArrayList<Session> _sessions;
    private boolean _sessionsLazyloaded;

    public ArrayList<Session> Sessions(){
        if(_sessionsLazyloaded){
            Idb.GetLazyLoadedObjects(VenueId,Session.class,this.getClass());
            _sessionsLazyloaded = false;
        }
        return _sessions;
    }
    public void setSessions(ArrayList<Session> sessions){
        _sessions = sessions;
        _sessionsLazyloaded = false;
    }

    public Venue(IDbInterface idb){
        Idb = idb;
        _sessionsLazyloaded = true;
        VenueId = -1;
    }
}
