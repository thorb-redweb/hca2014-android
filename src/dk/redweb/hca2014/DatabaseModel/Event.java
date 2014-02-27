package dk.redweb.hca2014.DatabaseModel;

import dk.redweb.hca2014.Interfaces.IDbInterface;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:05 AM
 */
public class Event {
    private IDbInterface Idb;

    public int EventId;
    public String Title;
    public String Summary;
    public String Details;
    public String Submission;
    public String Imagepath;
    private ArrayList<Session> _sessions;
    private boolean _sessionsLazyloaded;

    public ArrayList<Session> Sessions(){
        if(_sessionsLazyloaded){
            Idb.GetLazyLoadedObjects(EventId,Session.class,this.getClass());
            _sessionsLazyloaded = false;
        }
        return _sessions;
    }
    public void setSessions(ArrayList<Session> sessions){
        _sessions = sessions;
        _sessionsLazyloaded = false;
    }

    public Event(IDbInterface idb){
        Idb = idb;
        _sessionsLazyloaded = true;
        EventId = -1;
    }
}
