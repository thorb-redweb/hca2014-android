package dk.redweb.Red_App;

import android.util.Log;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 2:28 PM
 */
public class MyLog {
    private final static String APP_NAME = "RedEvent";


    public static void v(String msg){
        Log.v(APP_NAME, msg);
    }

    public static void d(String msg){
        Log.d(APP_NAME, msg);
    }

    public static void i(String msg){
        Log.i(APP_NAME, msg);
    }

    public static void w(String msg){
        Log.w(APP_NAME, msg);
    }

    public static void w(String msg, Throwable e){
        Log.w(APP_NAME, msg, e);
    }

    public static void e(String msg){
        Log.e(APP_NAME, msg);
    }

    public static void e(String msg, Throwable e){
        Log.e(APP_NAME, msg, e);
    }
}
