package dk.redweb.hca2014.Database;

import android.net.Uri;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 12:59 PM
 */
public class Converters
{
    public static int BooleanToInteger(boolean bool){
        return (bool)? 1 : 0;
    }

    public static Boolean IntegerToBoolean(int integer){
        return (integer == 1)? true : false;
    }

    public static String JodaDateTimeToSQLDateTime(DateTime dateTime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String SQLDateTime = dateTimeFormatter.print(dateTime);
        return SQLDateTime;
    }

    public static String JodaDateTimeToSQLDate(DateTime dateTime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String SQLDate = dateTimeFormatter.print(dateTime);
        return SQLDate;
    }

    public static String JodaDateTimeToSQLTime(DateTime dateTime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        String SQLTime = dateTimeFormatter.print(dateTime);
        return SQLTime;
    }

    public static String LocalDateToSQLDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        String SQLDate = dateTimeFormatter.print(date);
        return SQLDate;
    }

    public static Uri MakeUri(String url)
    {
        if(url.equals("")){
            return null;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://"))  {
            url = "http://" + url;
        }

        return Uri.parse(url);
    }

    public static DateTime SQLDateTimeToJodaDateTime(String SQLdatetime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.parseDateTime(SQLdatetime);
    }

    public static LocalDate SQLDateTimeToLocalDate(String SQLdatetime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.parseLocalDate(SQLdatetime);
    }

    public static LocalDate SQLDateToLocalDate(String SQLdate)
    {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return dateFormatter.parseLocalDate(SQLdate);
    }

    public static LocalTime SQLTimeToLocalTime(String SQLtime)
    {
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        return timeFormatter.parseLocalTime(SQLtime);
    }
}
