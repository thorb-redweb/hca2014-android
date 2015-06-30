package dk.redweb.hca2014.ViewModels;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.gms.maps.model.LatLng;
import dk.redweb.hca2014.DatabaseModel.Session;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 3:36 PM
 */
public class SessionVM {
    Locale locale = new Locale("da_DK", "da_DK");
    private Session _session;

    public int SessionId(){
        return _session.SessionId;
    }

    public String Title(){
        if (_session.Title.length() != 0){
            return _session.Title;
        } else {
            return _session.Event().Title;
        }
    }

    public String SummaryWithHtml(){
        if(_session.Event().Summary.length() != 0){
            return StringUtils.stripJoomlaTags(_session.Event().Summary);
        } else {
            return StringUtils.stripJoomlaTags(_session.Event().Details);
        }
    }

    public String SummaryWithoutHtml(){
        if(_session.Event().Summary.length() != 0) {
            return StringUtils.stripHtmlAndJoomla(_session.Event().Summary);
        } else {
            return StringUtils.stripHtmlAndJoomla(_session.Event().Details);
        }
    }

    public String DetailsWithHtml(){
        return StringUtils.stripJoomlaTags(_session.Event().Details + "<br/>" + _session.Details);
    }

    public String DetailsWithoutHtml(){
        return StringUtils.stripHtmlAndJoomla(_session.Event().Details + "<br/>" + _session.Details);
    }

    public LocalDate StartDate(){
        return _session.StartDate;
    }

    public LocalTime StartTime(){
        return _session.StartTime;
    }

    public GregorianCalendar StartDateTimeAsCalendar(){
        LocalDate date = _session.StartDate;
        LocalTime time = _session.StartTime;
        GregorianCalendar calendar = new GregorianCalendar(date.getYear(), date.getMonthOfYear()-1, date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
        return calendar;
    }

    public LocalDateTime StartDateTime(){
        LocalDate date = _session.StartDate;
        LocalTime time = _session.StartTime;
        LocalDateTime dateTime = new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
        return dateTime;
    }

    public String StartDateWithPattern(String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dateTimeFormatter.print(_session.StartDate);
    }

    public String StartTimeAsString(){
        if(_session.StartTime == null){
            return "00";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm").withLocale(locale);
        return dateTimeFormatter.print(_session.StartTime);
    }

    public LocalDate EndDate(){
        return _session.EndDate;
    }

    public LocalTime EndTime(){
        return _session.EndTime;
    }

    public GregorianCalendar EndDateTimeAsCalendar(){
        LocalDate date = _session.EndDate;
        LocalTime time = _session.EndTime;
        GregorianCalendar calendar = new GregorianCalendar(date.getYear(), date.getMonthOfYear()-1, date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
        return calendar;
    }

    public LocalDateTime EndDateTime(){
        LocalDate date = _session.EndDate;
        LocalTime time = _session.EndTime;
        LocalDateTime dateTime = new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
        return dateTime;
    }

    public String EndDateWithPattern(String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dateTimeFormatter.print(_session.EndDate);
    }

    public String EndTimeAsString(){
        if(_session.EndTime == null){
            return "00";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm").withLocale(locale);
        return dateTimeFormatter.print(_session.EndTime);
    }

    public String SubmissionPath()
    {
        if(_session.Submission == null || _session.Submission.equals("")) {
            return _session.Event().Submission;
        }
        else {
            return _session.Submission;
        }
    }

    public String ImagePath(){
        return _session.Event().Imagepath;
    }

    public String Venue(){
        return _session.Venue().Title;
    }

    public Double Latitude(){
        return _session.Venue().Latitude;
    }

    public Double Longitude(){
        return _session.Venue().Longitude;
    }

    public LatLng Location(){
        return new LatLng(Latitude(), Longitude());
    }

    public String Type(){
        return _session.Type;
    }

    public int TypeColor(){
        if (_session.Type.matches("Kulturformidling")){
            return Color.argb(255,31,98,181);
        }
        else if (_session.Type.matches("Kunst og kultur")){
            return Color.argb(255,127,127,127);
        }
        else if (_session.Type.matches("Leg og læring")){
            return Color.argb(255,81,148,32);
        }
        else if (_session.Type.matches("Musik")){
            return Color.argb(255,233,143,15);
        }
        else if (_session.Type.matches("Spoken Word")){
            return Color.argb(255,100,100,100);
        }
        else if (_session.Type.matches("Spoken Word Festival")){
            return Color.argb(255,100,100,100);
        }
        else if (_session.Type.matches("Underholdning og teater")){
            return Color.argb(255,229,0,125);
        }
        return Color.parseColor("#000000");
    }

    public int TypeImage(){
        if (_session.Type.matches("Kulturformidling")){
            return R.drawable.culture;
        }
        else if (_session.Type.matches("Kunst og kultur")){
            return R.drawable.art;
        }
        else if (_session.Type.matches("Leg og læring")){
            return R.drawable.play;
        }
        else if (_session.Type.matches("Musik")){
            return R.drawable.music;
        }
        else if (_session.Type.matches("Spoken Word")){
            return R.drawable.spoken;
        }
        else if (_session.Type.matches("Spoken Word Festival")){
            return R.drawable.spoken;
        }
        else if (_session.Type.matches("Underholdning og teater")){
            return R.drawable.theater;
        }
        return R.drawable.default_icon;
    }

    public SessionVM(Session session) {
        _session = session;
    }

    public String WebsiteLink(){
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(locale);
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH-mm").withLocale(locale);
        return "www.hcafestivals.dk/da/details/" + _session.SessionId + "-" + _session.Title.replace(" ","-") +
                "/" + _session.Venue().City.replace(" ","-") +
                "/" + _session.StartDate.toString(dateFormatter) + "/" + _session.StartTime.toString(timeFormatter);
    }
}
