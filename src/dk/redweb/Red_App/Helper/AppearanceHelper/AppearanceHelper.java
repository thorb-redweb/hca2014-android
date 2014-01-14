package dk.redweb.Red_App.Helper.AppearanceHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import dk.redweb.Red_App.Helper.AppearanceHelper.FlexibleButtons.FlexButtonAppearanceForwarder;
import dk.redweb.Red_App.Helper.AppearanceHelper.Labels.TextViewAppearanceForwarder;
import dk.redweb.Red_App.My;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/26/13
 * Time: 10:57 AM
 */
public class AppearanceHelper {

    Context context;
    XmlNode localLook;
    XmlNode globalLook;

    public AppearanceHelperGetter Getter;

    public FlexButtonAppearanceForwarder FlexButton;
    public TextViewAppearanceForwarder TextView;

    public AppearanceHelper(Context context, XmlNode local, XmlNode global){
        this.context = context;
        localLook = local;
        globalLook = global;

        Getter = new AppearanceHelperGetter(context, local, global);
        FlexButton = new FlexButtonAppearanceForwarder(this,Getter);
        TextView = new TextViewAppearanceForwarder(this,Getter);
    }

    public void setImageViewImage(ImageView imageView, String localName) throws Exception {
        if (context == null)
            throw new Exception("setImageViewImage Method cannot be used without context having been initialized in the AppearanceHelper constructor");
        if(localLook != null && localLook.hasChild(localName)) {
            String imagename = localLook.getStringFromNode(localName);
            Drawable drawable = My.getDrawableFromResourceWithFilename(imagename, context);
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setListViewBackgroundColor(ListView listView, String localName, String globalName) throws NoSuchFieldException {
        int backgroundColor = Getter.getColor(localName, globalName);
        listView.setBackgroundColor(backgroundColor);
        listView.setCacheColorHint(backgroundColor);
    }

    public void setViewBackgroundColor(View view, String localName, String globalName) throws NoSuchFieldException {
        int backgroundColor = Getter.getColor(localName, globalName);
        view.setBackgroundColor(backgroundColor);
    }

    public void setViewBackgroundImageOrColor(View view, String localImageName, String localColorName, String globalColorName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localImageName))
            view.setBackground(My.getDrawableFromResourceWithFilename(localLook.getStringFromNode(localImageName), context));
        else
            setViewBackgroundColor(view, localColorName, globalColorName);
    }

    public void setViewBackgroundImageOrColor(View[] views, String localImageName, String localColorName, String globalColorName) throws NoSuchFieldException {
        for (View view : views){
             setViewBackgroundImageOrColor(view,localImageName,localColorName,globalColorName);
        }
    }

    public void setViewBackgroundTileImageOrColor(View view, String localImageName, String localColorName, String globalColorName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localImageName)) {
            view.setBackground(My.getDrawableFromResourceWithFilename(localLook.getStringFromNode(localImageName), context));
            BitmapDrawable bmp = (BitmapDrawable)view.getBackground();
            bmp.mutate();
            bmp.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        } else {
            setViewBackgroundColor(view, localColorName, globalColorName);
        }
    }

    public void setViewBackgroundImageOrColor(View view, String localImageName, String localCornerName, String localColorName, String globalColorName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localCornerName)){
            float radius = localLook.getFloatFromNode(localCornerName);
            int color = Getter.getColor(localColorName, globalColorName);
            float[] radii = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

            RoundRectShape roundRectShape = new RoundRectShape(radii, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(color);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable});

            view.setBackground(layerDrawable);
        }
        else if(localLook != null && localLook.hasChild(localImageName))
            view.setBackground(My.getDrawableFromResourceWithFilename(localLook.getStringFromNode(localImageName), context));
        else
            setViewBackgroundColor(view, localColorName, globalColorName);
    }
}
