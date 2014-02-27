package dk.redweb.hca2014.Network;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 2:39 PM
 */
public class DrawableManager {
    private final Map<String, Drawable> drawableMap;

    public DrawableManager(){
        drawableMap = new HashMap<String, Drawable>();
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView){
        if(drawableMap.containsKey(urlString)){
            imageView.setImageDrawable(drawableMap.get(urlString));
        }

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                imageView.setImageDrawable((Drawable) message.obj);
            }
        };

        Thread thread = new Thread(){
            @Override
            public void run(){
                Drawable drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    public Drawable fetchDrawable(String urlString){
        Log.d("RedEvent", "DrawableManager.fetchDrawable executing...   Image url: " + urlString);
        if(drawableMap.containsKey(urlString)){
            Log.v("RedEvent", "Image in cache, " + urlString);
            return drawableMap.get(urlString);
        }
        try{
            InputStream inputStream = fetch(urlString);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, opts);

            int currentWidth = opts.outWidth;
            int currentHeight = opts.outHeight;
            opts.inJustDecodeBounds = false;
            Log.v("RedEvent", "Raw size: " + currentWidth + "x" + currentHeight + ", " + urlString);

            inputStream = fetch(urlString);

            Bitmap bitmapImage;
            if(currentHeight >= 5120 || currentWidth >= 5120){
                opts.inSampleSize = 8;
                bitmapImage = BitmapFactory.decodeStream(inputStream, null, opts);
            }
            else if(currentHeight >= 2560 || currentWidth >= 2560){
                opts.inSampleSize = 4;
                bitmapImage = BitmapFactory.decodeStream(inputStream, null, opts);
            }
            else if(currentHeight >= 1280 || currentWidth >= 1280){
                opts.inSampleSize = 2;
                bitmapImage = BitmapFactory.decodeStream(inputStream, null, opts);
            }
            else{
                bitmapImage = BitmapFactory.decodeStream(inputStream, null, opts);
            }

            Drawable drawable = new BitmapDrawable(Resources.getSystem(), bitmapImage);

            if(drawable != null){
                drawableMap.put(urlString,drawable);
                Log.v("RedEvent", "Final size: " + drawable.getIntrinsicWidth() + "x" + drawable.getIntrinsicHeight() + ", " + urlString);
            } else {
                Log.w("RedEvent", "Could not get image");
            }

            return drawable;
        }
        catch (Exception e){
            Log.e("RedEvent", "DrawableManager.fetchDrawable failed: ", e);
            return null;
        }
    }

    private InputStream fetch(String urlString) throws IOException {

        urlString = urlString.replaceAll(" ", "%20");
        HttpGet request = new HttpGet(urlString);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        return response.getEntity().getContent();
    }
}
