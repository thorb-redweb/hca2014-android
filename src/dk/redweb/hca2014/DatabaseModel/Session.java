package dk.redweb.hca2014.DatabaseModel;

import dk.redweb.hca2014.Interfaces.IDbInterface;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;


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
    public String Submission;
    public LocalDate StartDate;
    public LocalTime StartTime;
    public LocalDate EndDate;
    public LocalTime EndTime;
    public String Type;
    public JSONArray Prices;
    public Event Event;
    public Venue Venue;

    public Session(){
        EventId = -1;
        SessionId = -1;
        VenueId = -1;
    }
}
