package dk.redweb.Red_App;

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
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.Views.FlexibleButton;
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

    public AppearanceHelper(XmlNode local, XmlNode global){
        localLook = local;
        globalLook = global;
    }

    public AppearanceHelper(Context context, XmlNode local, XmlNode global){
        this.context = context;
        localLook = local;
        globalLook = global;
    }

    public void setImageViewImage(ImageView imageView, String localName) throws Exception {
        if (context == null)
            throw new Exception("setImageViewImage Method cannot be used without context having been initialized in the AppearanceHelper constructor");
        if(localLook != null && localLook.hasChild(localName)) {
            String imagename = localLook.getStringFromNode(localName);
            Drawable drawable = My.getDrawableFromFilename(imagename, context);
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setListViewBackgroundColor(ListView listView, String localName, String globalName) throws NoSuchFieldException {
        int backgroundColor = getColor(localName, globalName);
        listView.setBackgroundColor(backgroundColor);
        listView.setCacheColorHint(backgroundColor);
    }

    public void setFlexibleButtonImage(FlexibleButton flexButton, String localName) throws Exception {
        if (context == null)
            throw new Exception("setImageViewImage Method cannot be used without context having been initialized in the AppearanceHelper constructor");
        if(localLook != null && localLook.hasChild(localName)) {
            String imagename = localLook.getStringFromNode(localName);
            Drawable drawable = My.getDrawableFromFilename(imagename, context);
            flexButton.setImageDrawable(drawable);
        } else {
            flexButton.setImageVisibility(View.GONE);
        }
    }
    public void setFlexibleButtonImage(FlexibleButton[] flexButtons, String localName) throws Exception {
        for(FlexibleButton flxButton : flexButtons){
            setFlexibleButtonImage(flxButton, localName);
        }
    }

    public void setFlexibleButtonTextColor(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        flexButton.setTextColor(getColor(localName, globalName));

    }

    public void setFlexibleButtonTextColor(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException{
        for(FlexibleButton flxButton : flexButtons){
            setFlexibleButtonTextColor(flxButton,localName,globalName);
        }
    }


    public void setFlexibleButtonTextSize(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localName))
            flexButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, localLook.getFloatFromNode(localName));
        else
            flexButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, globalLook.getFloatFromNode(globalName));
    }

    public void setFlexibleButtonTextSize(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException {
        for(FlexibleButton flexButton : flexButtons){
            setFlexibleButtonTextSize(flexButton, localName, globalName);
        }
    }

    public void setFlexibleButtonTextStyle(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        String itemtitleStyleName;
        if(localLook != null && localLook.hasChild(localName))
            itemtitleStyleName = localLook.getStringFromNode(localName);
        else
            itemtitleStyleName = globalLook.getStringFromNode(globalName);
        flexButton.setTypeface(null, My.getTextStyleFromName(itemtitleStyleName));
    }

    public void setFlexibleButtonTextStyle(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException {
        for(FlexibleButton flexButton : flexButtons){
            setFlexibleButtonTextStyle(flexButton, localName, globalName);
        }
    }

    public void setFlexibleButtonTextShadow(FlexibleButton flexButton, String localColorName, String globalColorName,
                                            String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        if(pageOrGlobalHas(localColorName, globalColorName) && pageOrGlobalHas(localOffsetName, globalOffsetName))
        {
            int shadowColor = getColor(localColorName, globalColorName);
            float[] shadowOffset = getCoords(localOffsetName, globalOffsetName);
            flexButton.setTextShadowLayer(1, shadowOffset[0], shadowOffset[1], shadowColor);
        }
    }

    public void setFlexibleButtonTextShadow(FlexibleButton[] flexButtons, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (FlexibleButton flexButton : flexButtons)
            setFlexibleButtonTextShadow(flexButton, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setTextColor(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTextColor(getColor(localName, globalName));
    }

    public void setTextColor(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            setTextColor(textView, localName, globalName);
        }
    }

    public void setTextSize(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localName))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, localLook.getFloatFromNode(localName));
        else
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, globalLook.getFloatFromNode(globalName));
    }

    public void setTextSize(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            setTextSize(textView, localName, globalName);
        }
    }

    public void setTextStyle(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        String itemtitleStyleName;
        if(localLook != null && localLook.hasChild(localName))
            itemtitleStyleName = localLook.getStringFromNode(localName);
        else
            itemtitleStyleName = globalLook.getStringFromNode(globalName);
        textView.setTypeface(null, My.getTextStyleFromName(itemtitleStyleName));
    }

    public void setTextStyle(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            setTextStyle(textView, localName, globalName);
        }
    }

    public void setTextShadow(TextView textView, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        if(pageOrGlobalHas(localColorName, globalColorName) && pageOrGlobalHas(localOffsetName, globalOffsetName))
        {
            int shadowColor = getColor(localColorName, globalColorName);
            float[] shadowOffset = getCoords(localOffsetName, globalOffsetName);
            textView.setShadowLayer(1,shadowOffset[0],shadowOffset[1],shadowColor);
        }
    }

    public void setTextShadow(TextView[] textViews, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (TextView textView : textViews)
            setTextShadow(textView, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setViewBackgroundColor(View view, String localName, String globalName) throws NoSuchFieldException {
        int backgroundColor = getColor(localName, globalName);
        view.setBackgroundColor(backgroundColor);
    }

    public void setViewBackgroundImageOrColor(View view, String localImageName, String localColorName, String globalColorName) throws NoSuchFieldException {
        if(localLook != null && localLook.hasChild(localImageName))
            view.setBackground(My.getDrawableFromFilename(localLook.getStringFromNode(localImageName), context));
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
            view.setBackground(My.getDrawableFromFilename(localLook.getStringFromNode(localImageName), context));
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
            int color = parseColor(localLook.getStringFromNode(localColorName));
            float[] radii = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

            RoundRectShape roundRectShape = new RoundRectShape(radii, null, null);
            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(color);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable});

            view.setBackground(layerDrawable);
        }
        else if(localLook != null && localLook.hasChild(localImageName))
            view.setBackground(My.getDrawableFromFilename(localLook.getStringFromNode(localImageName), context));
        else
            setViewBackgroundColor(view, localColorName, globalColorName);
    }

    private int getColor(String localName, String globalName) throws NoSuchFieldException{
        if(pageHas(localName)) {
            return parseColor(localLook.getStringFromNode(localName));
        } else {
            return parseColor(globalLook.getStringFromNode(globalName));
        }
    }

    private int parseColor(String color){
        if(color.equals(LOOK.INVISIBLE)) {
            return Color.parseColor("#00000000");
        } else {
            return Color.parseColor(color);
        }
    }

    private float[] getCoords(String localName, String globalName) throws NoSuchFieldException{
        String pointAsString;
        if(pageHas(localName)){
            pointAsString = localLook.getStringFromNode(localName);
        } else {
            pointAsString = globalLook.getStringFromNode(globalName);
        }
        String[] pointArray = pointAsString.split(",");
        return new float[]{Float.parseFloat(pointArray[0]), Float.parseFloat(pointArray[1])};
    }

    private boolean pageOrGlobalHas(String localname, String globalname){
        return (pageHas(localname) || globalLook.hasChild(globalname));
    }

    private boolean pageHas(String localname){
        return localLook != null && localLook.hasChild(localname);
    }
}
