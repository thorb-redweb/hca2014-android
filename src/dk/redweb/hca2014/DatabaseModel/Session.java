package dk.redweb.hca2014.DatabaseModel;

import dk.redweb.hca2014.Interfaces.IDbInterface;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:00 AM
 */
public class Session {
    public IDbInterface Idb;

    public int SessionId;
    public int EventId;
    public int VenueId;
    public String Title;
    public String Details;
    public LocalDate StartDate;
    public LocalTime StartTime;
    public LocalDate EndDate;
    public LocalTime EndTime;
    public String Type;

    private Event _event;
    private boolean _eventLazyloaded;
    public Event Event(){
        if(_eventLazyloaded){
            _event = Idb.GetLazyLoadedObject(EventId,Event.class,this.getClass());
            _eventLazyloaded = false;
        }
        return _event;
    }
    public void setEvent(Event event){
        _event = event;
        _eventLazyloaded = false;
    }

    private Venue _venue;
    private boolean _venueLazyloaded;
    public Venue Venue(){
        if(_venueLazyloaded){
            _venue = Idb.GetLazyLoadedObject(VenueId,Venue.class,this.getClass());
            _venueLazyloaded = false;
        }
        return _venue;
    }
    public void setVenue(Venue venue){
        _venue = venue;
        _venueLazyloaded = false;
    }

    public Session(IDbInterface idb){
        Idb = idb;
        _eventLazyloaded = true;
        _venueLazyloaded = true;
        EventId = -1;
        SessionId = -1;
        VenueId = -1;
    }
}
