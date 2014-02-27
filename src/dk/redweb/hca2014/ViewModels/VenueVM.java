package dk.redweb.hca2014.ViewModels;

import com.google.android.gms.maps.model.LatLng;
import dk.redweb.hca2014.DatabaseModel.Venue;
import dk.redweb.hca2014.StringUtils;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/10/13
 * Time: 2:29 PM
 */
public class VenueVM {
    private Venue _venue;

    public VenueVM(Venue venue){
        _venue = venue;
    }

    public int VenueId(){
        return _venue.VenueId;
    }

    public String Name(){
        return _venue.Title;
    }

    public String Street(){
        return _venue.Street;
    }

    public String City(){
        return _venue.City;
    }

    public String Description(){
        return _venue.Description;
    }

    public String DescriptionWithHtml(){
        return StringUtils.stripJoomlaTags(Description());
    }

    public String DescriptionWithoutHtml(){
        return StringUtils.stripHtmlAndJoomla(Description());
    }

    public double Latitude(){
        return _venue.Latitude;
    }

    public double Longitude(){
        return _venue.Longitude;
    }

    public LatLng Location(){
        return new LatLng(Latitude(), Longitude());
    }

    public String ImagePath(){
        return _venue.ImagePath;
    }
}
