package dk.redweb.hca2014;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import dk.redweb.hca2014.StaticNames.LOOK;

import java.io.File;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/24/13
 * Time: 9:29 AM
 */
public class My {
    public static Drawable getDrawableFromDiskWithFilename(String pathName, Context context, int height, int width, boolean rotate){
        Drawable image = null;

        // Calculate inSampleSize
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        options.inSampleSize = calculateInSampleSize(options, height, width);
        options.inJustDecodeBounds = false;

        //Get image
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        if(bitmap != null){
            //Rotate if necessary
            int rotateDegrees = getCameraPhotoOrientation(null, null, pathName);
            if(rotateDegrees != 0)
            {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotateDegrees);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            //Create drawable
            image = new BitmapDrawable(context.getResources(), bitmap);
        }
        //Return drawable, or null if image does not exist
        return image;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
//            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            MyLog.v("Exif orientation: " + orientation);
            MyLog.v("Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static Drawable getDrawableFromResourceWithFilename(String fileName, Context context){
        String[] partArray = fileName.split("\\.");
        String imageName = partArray[0];
        return getDrawableFromResourceWithImageName(imageName, context);
    }

    public static Drawable getDrawableFromResourceWithImageName(String imageName, Context context){
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
