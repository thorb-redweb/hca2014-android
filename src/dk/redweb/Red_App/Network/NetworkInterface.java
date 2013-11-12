package dk.redweb.Red_App.Network;

import android.widget.ImageView;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 2:51 PM
 */
public class NetworkInterface {
    DrawableManager drawableManager;

    public NetworkInterface(){
        drawableManager = new DrawableManager();
    }

    public void fetchImageOnThread(String urlString, ImageView imageView){
        drawableManager.fetchDrawableOnThread(urlString, imageView);
    }
}
