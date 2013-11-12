package dk.redweb.Red_App.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 4:22 PM
 */
public class JSONTranslator {
    public static String ConvertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("RedEvent", "IOException in JSONTranslator.ConvertStreamToString", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("RedEvent", "IOException in JSONTranslator.ConvertStreamToString", e);
            }
        }
        return sb.toString();
    }
}
