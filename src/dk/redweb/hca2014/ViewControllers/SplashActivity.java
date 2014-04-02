package dk.redweb.hca2014.ViewControllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Interfaces.Delegate_dumpServer;
import dk.redweb.hca2014.Interfaces.Delegate_dumpToDatabase;
import dk.redweb.hca2014.Interfaces.Delegate_updateFromServer;
import dk.redweb.hca2014.Interfaces.Delegate_updateToDatabase;
import dk.redweb.hca2014.Network.ServerInterface;
import dk.redweb.hca2014.StaticNames.EXTRA;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TYPE;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;
import org.joda.time.DateTime;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity implements Delegate_dumpServer, Delegate_dumpToDatabase,
        Delegate_updateFromServer, Delegate_updateToDatabase{

    private RedEventApplication _app;
    private DbInterface _db;
    private ServerInterface _sv;
    private XmlStore _xml;

    private RelativeLayout _screen;
    private ProgressDialog _progressDialog;
    private Timer _timer;

    private boolean _shortload;
    private boolean _firsttime;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        _firsttime = true;

        _app = (RedEventApplication)getApplication();
        _db = _app.getDbInterface();
        _xml = _app.getXmlStore();
        _sv = _app.getServerInterface();

        _screen = (RelativeLayout) findViewById(R.id.splash_lnrPage);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(_firsttime){
            startDownloads();
        }
        else{
            this.finish();
        }
    }

    public void startDownloads(){
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setTitle("Henter data fra databasen");
        _progressDialog.setMessage("Dette kan tage et par minutter...");
        _progressDialog.show();
        if(_db.DatabaseIsEmpty()){
            _shortload = true;
            _sv.dumpServer(this);
        }
        else if(_app.getLastDatabaseUpdate().isBefore(DateTime.now().minusMinutes(0)) && !_app.shouldSkipUpdate()){
            _shortload = false;
            returnFromDumpToDatabase();
        }
        else{
            MyLog.v("No update needed");
            _shortload = false;
            returnFromUpdateToDatabase();
        }
    }

    @Override
    public void returnFromDumpServer(String result) {
        _db.dumpServer(result, this);
    }

    @Override
    public void returnFromDumpToDatabase() {
        _sv.updateFromServer(this);
    }

    @Override
    public void returnFromUpdateFromServer(String result) {
        if (result.equals("")){
            MyLog.v("No updates received");
            returnFromUpdateToDatabase();
        }
        else
        {
            _db.updateFromServer(result, this);
        }
    }

    @Override
    public void returnFromUpdateToDatabase() {
        _app.setLastDatabaseUpdate(DateTime.now());
        _screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_timer != null){
                    _timer.cancel();
                }
                launchFrontPage();
            }
        });

        _progressDialog.dismiss();

        int time;
        if(_shortload)
        {
            time = 1000;
        }
        else
        {
            time = 3000;
        }
        _timer = new Timer();
        TimerTask timerTask =  new TimerTask() {
            @Override
            public void run() {

                launchFrontPage();
            }
        };
        _timer.schedule(timerTask, time);
    }

    private void launchFrontPage(){
        try {
            _firsttime = false;
            XmlNode frontPage = _xml.getPage("Autosubscriber");

            Intent mainIntent = new Intent(this, FragmentPagesActivity.class);
            mainIntent.putExtra(EXTRA.PAGE, frontPage);
            startActivity(mainIntent);
        } catch (Exception e) {
            MyLog.e("Exception when attempting to get the frontpage from xml", e);
            fatalErrorOccured("En fejl skete ved start af appens hovedside. Kontakt venligst udvikleren.");
        }
    }

    @Override
    public void errorOccured(String errorMessage) {
        //Alleviate the possibility that last databaseupdate was set before the error occured
        _app.setLastDatabaseUpdate(DateTime.now().minusMinutes(6));

        _progressDialog.dismiss();

        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.setTitle("An Error Occured");
        alertDialog.setMessage("Der er sket en fejl under hentning af data");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Prøv Igen", tryAgainOnClickListener());
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afslut App", closeAppOnClickListener());
        alertDialog.show();
    }

    public DialogInterface.OnClickListener tryAgainOnClickListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startDownloads();
            }
        };
    }

    public DialogInterface.OnClickListener closeAppOnClickListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
    }

    public void fatalErrorOccured(String errorMessage) {
        //Alleviate the possibility that last databaseupdate was set before the error occured
        _app.setLastDatabaseUpdate(DateTime.now().minusMinutes(6));

        _progressDialog.dismiss();

        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.setTitle("An Error Occured");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Afslut App", closeAppOnClickListener());
        alertDialog.show();

        MyLog.w("Alertdialog displayed");
    }
}
