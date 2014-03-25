package dk.redweb.hca2014;

import android.support.v4.app.FragmentActivity;

import java.io.*;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 3/6/14
 * Time: 13:31
 */
public class cssfile {
    public static String css(FragmentActivity activity){
        String css = "";
        try{
            StringBuffer datax = new StringBuffer("");
            FileInputStream fIn = activity.openFileInput("/dk/redweb/hca2014/joomla.css");
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }
            isr.close() ;

            css = datax.toString();
        }
        catch (Exception e) {
            MyLog.e("Exception when getting css from file", e);
        }

        return "<style media=\"screen\" type=\"text/css\">" + css + "</style>";
    }
}
