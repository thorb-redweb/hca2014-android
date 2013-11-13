package dk.redweb.Red_App;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import dk.redweb.Red_App.Database.DbInterface;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.Network.ServerInterface;
import dk.redweb.Red_App.Views.NavBarBox;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:49 PM
 */
public class RedEventApplication  extends Application {
    private DbInterface _dbInterface;
    public DbInterface getDbInterface(){ return _dbInterface; }
    private NetworkInterface _networkInterface;;
    public NetworkInterface getNetworkInterface() { return _networkInterface; }
    private ServerInterface _serverInterface;
    public ServerInterface getServerInterface(){ return _serverInterface; }
    private XmlStore _xmlStore;
    public XmlStore getXmlStore(){ return _xmlStore; }

    private NavBarBox _navbar;
    public NavBarBox getNavbar(){return _navbar;}
    public void setNavbar(NavBarBox navbar){_navbar = navbar;}

    public XmlNode currentPage;

    public int getDatabaseDataVersion(){
        SharedPreferences prefs = this.getSharedPreferences("database", MODE_PRIVATE);
        return prefs.getInt("dataVersion", 0);
    }

    public void setDatabaseDataVersion(int newVersion){
        SharedPreferences prefs = this.getSharedPreferences("database", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("dataVersion", newVersion);
        editor.apply();
    }

    public DateTime getLastDatabaseUpdate(){
        SharedPreferences prefs = getSharedPreferences("database", MODE_PRIVATE);
        String dateString = prefs.getString("updatetimestamp", "2000-01-01 00:00:00");

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormatter.parseDateTime(dateString);
    }

    public void setLastDatabaseUpdate(DateTime timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = dateTimeFormatter.print(timestamp);

        SharedPreferences prefs = getSharedPreferences("database", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("updatetimestamp", dateString);
        editor.commit();
    }

    //Debugging section
    private boolean debugging = false;
    private DateTime debugCurrentDate;
    private int debugYear = 2013;
    private int debugMonth = 10;
    private int debugDay = 1;
    private int debugHour = 13;
    private int debugMinute = 54;
    public DateTime getDebugCurrentDate(){ return debugCurrentDate; }
    public boolean isDebugging(){ return debugging; }

    public RedEventApplication(){
        debugCurrentDate = new DateTime(debugYear, debugMonth, debugDay, debugHour, debugMinute);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        try {
            _xmlStore = new XmlStore(getApplicationContext());
        } catch (NoSuchFieldException e) {
            Log.e("RedEvent", "NoSucFieldException during creation of XmlStore in RedEventApplication:onCreate", e);
        }
        _networkInterface = new NetworkInterface();
        _serverInterface = new ServerInterface(this);
        _dbInterface = new DbInterface(this, _networkInterface, _serverInterface, _xmlStore);
    }
}