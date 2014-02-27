package dk.redweb.hca2014;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Model.RedUploadDataStore;
import dk.redweb.hca2014.Model.VolatileStores;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.Views.NavBarBox;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;
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

    private VolatileStores _volatileDataStores;
    public VolatileStores getVolatileDataStores() { return  _volatileDataStores; }

    private NavBarBox _navbar;
    public NavBarBox getNavbar(){return _navbar;}
    public void setNavbar(NavBarBox navbar){_navbar = navbar;}

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
    private boolean debugging = true;
    private boolean debugSkipUpdates = true;
    private DateTime debugCurrentDate;
    private int debugYear = 2013;
    private int debugMonth = 8;
    private int debugDay = 24;
    private int debugHour = 13;
    private int debugMinute = 54;
    public DateTime getDebugCurrentDate(){ return debugCurrentDate; }
    public boolean isDebugging(){ return debugging; }
    public boolean shouldSkipUpdate() { return debugSkipUpdates; }

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

        getInitializationData();
    }

    public void getInitializationData(){
        _volatileDataStores = new VolatileStores();
        try {
            if(_xmlStore.pages.hasChild(PAGE.GLOBAL)){
                XmlNode global = _xmlStore.pages.getChildFromNode(PAGE.GLOBAL);
                if(global.getBoolWithNoneAsFalseFromNode(PAGE.USEREDUPLOAD)){
                    _volatileDataStores.add(new RedUploadDataStore(_dbInterface));
                }
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when adding addons", e);
        }
    }
}