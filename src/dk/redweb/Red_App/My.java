package dk.redweb.Red_App;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import dk.redweb.Red_App.StaticNames.LOOK;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 9:29 AM
 */
public class My {
    public static Drawable getDrawableFromFilename(String fileName, Context context){
        String[] partArray = fileName.split("\\.");
        String imageName = partArray[0];
        return getDrawableFromImageName(imageName, context);
    }

    public static Drawable getDrawableFromImageName(String imageName, Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(imageName, "drawable", context.getPackageName());
        return resources.getDrawable(resourceId);
    }

    public static int getTextStyleFromName(String textStyle){
        if(textStyle.equals(LOOK.TYPEFACE_NORMAL))
            return Typeface.NORMAL;
        else if(textStyle.equals(LOOK.TYPEFACE_BOLD))
            return Typeface.BOLD;
        else if(textStyle.equals(LOOK.TYPEFACE_ITALIC))
            return Typeface.ITALIC;
        else if(textStyle.equals(LOOK.TYPEFACE_BOLD_ITALIC))
            return Typeface.BOLD_ITALIC;
        return -1;
    }

    public static <T> T[] reverseArray(T[] array){
        for (int i = 0; i < array.length/2; i++){
            T tmp = array[i];
            int oppositeIndex = array.length - i - 1;
            array[i] = array[oppositeIndex];
            array[oppositeIndex] = tmp;
        }
        return array;
    }
}
