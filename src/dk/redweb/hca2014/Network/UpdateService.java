package dk.redweb.hca2014.Network;

import android.os.Handler;
import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.Interfaces.Delegate_dumpServer;
import dk.redweb.hca2014.Interfaces.Delegate_updateFromServer;
import dk.redweb.hca2014.Interfaces.Delegate_updateToDatabase;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.RedEventApplication;
import org.joda.time.DateTime;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 3/6/14
 * Time: 14:53
 */
public class UpdateService {

    RedEventApplication _app;
    DbInterface _db;

    Handler _handler;
    UpdateRunner _runnable;
    boolean shouldStop;

    public UpdateService(RedEventApplication app){
        _app = app;
        _db = app.getDbInterface();
        shouldStop = false;
    }

    public void start(){
        MyLog.v("Starting updateService");
        _handler = new Handler();
        _runnable = new UpdateRunner();
        _handler.post(_runnable);
    }

    public void stop(){
        MyLog.v("Stopping updateService");
        _handler.removeCallbacks(_runnable);
    }

    private class UpdateRunner implements Runnable, Delegate_updateFromServer, Delegate_updateToDatabase {

        @Override
        public void run() {
            MyLog.v("Preparing to run update");
            if(_app.getLastDatabaseUpdate().isBefore(DateTime.now().minusMinutes(5)) && !_app.shouldSkipUpdate()){
                startUpdate();
            }
            else{
                MyLog.v("No updating; too close to old update: " + _app.getLastDatabaseUpdate().toString() + " / " + DateTime.now().toString());
                _runnable = new UpdateRunner();
                _handler.postDelayed(_runnable, 5 * 60 * 1000);
            }
        }

        private void startUpdate() {
            MyLog.v("Getting update from server");
            new Handler_UpdateFromServer(this, _app).execute();
        }

        @Override
        public void returnFromUpdateFromServer(String result) {
            if (result.equals("")){
                MyLog.v("No updates received");
                returnFromUpdateToDatabase();
            }
            else
            {
                MyLog.v("Applying update to database");
                _db.updateFromServer(result, this);
            }
        }

        @Override
        public void returnWithNoUpdateRetrievedFromServer() {
            returnFromUpdateToDatabase();
        }

        @Override
        public void errorOccured(String errorMessage) {
            MyLog.e("An error occured during automatic updating: " + errorMessage);
        }

        @Override
        public void returnFromUpdateToDatabase() {
            _app.setLastDatabaseUpdate(DateTime.now());
            _runnable = new UpdateRunner();
            _handler.postDelayed(_runnable, 5 * 60 * 1000);
            MyLog.v("Updating finished");
        }
    }
}
