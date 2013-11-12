package dk.redweb.Red_App.Network;

import android.util.Log;
import dk.redweb.Red_App.MyLog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 4:20 PM
 */
public class HttpHandler {
    public static String GetString(String url) {
        try{
            String parsedString;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if(entity != null){
                InputStream inStream = entity.getContent();

                parsedString = JSONTranslator.ConvertStreamToString(inStream);
                inStream.close();
                return parsedString;
            }
            Log.e("RedEvent", "Error: No Entity");
            return "Error: No Entity";
        }
        catch (Exception e){
            Log.e("RedEvent", "Exception in HttpHandler.GetString", e);
            return "Error " + e;
        }
    }

    public static String SendString(String url, List<NameValuePair> postPairs){
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(postPairs));

            HttpResponse response = httpClient.execute(httpPost);

            return response.toString();
        } catch (Exception e) {
            MyLog.e("Exception when sending a HttpPost message", e);
            return "Error " + e;
        }
    }
}
