package dk.redweb.Red_App.Helper.AppearanceHelper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:14
 */
public class AppearanceHelperGetter {

    private Context _context;
    private XmlNode _localLook;
    private XmlNode _globalLook;

    public AppearanceHelperGetter(Context context, XmlNode localLook, XmlNode globalLook){
        _context = context;
        _localLook = localLook;
        _globalLook = globalLook;
    }

    public int getColor(String localName, String globalName) throws NoSuchFieldException{
        if(staticColors.containsKey(globalName)) {
            return staticColors.get(globalName);
        }

        if(localPageHas(localName)) {
            return parseColor(_localLook.getStringFromNode(localName));
        } else {
            return parseColor(_globalLook.getStringFromNode(globalName));
        }
    }

    private int parseColor(String color){
        if(staticColors.containsKey(color)) {
            return staticColors.get(color);
        }

        return Color.parseColor(color);
    }

    private static final Map<String, Integer> staticColors;
    static {
        staticColors = new HashMap<String, Integer>();
        staticColors.put(LOOK.BLACK,Color.parseColor("#000000"));
        staticColors.put(LOOK.LIGHTGREY,Color.parseColor("#bfbfbf"));
        staticColors.put(LOOK.INVISIBLE,Color.parseColor("#00000000"));
        staticColors.put(LOOK.WHITE,Color.parseColor("#FFFFFF"));
    }

    public float[] getCoords(String localName, String globalName) throws NoSuchFieldException{
        String pointAsString;
        if(localPageHas(localName)){
            pointAsString = _localLook.getStringFromNode(localName);
        } else {
            pointAsString = _globalLook.getStringFromNode(globalName);
        }
        String[] pointArray = pointAsString.split(",");
        return new float[]{Float.parseFloat(pointArray[0]), Float.parseFloat(pointArray[1])};
    }

    public Drawable getDrawableFromResource(String localName, String globalName) throws NoSuchFieldException {
        Resources resources = _context.getResources();

        String fileName = "";

        fileName = getString(localName, globalName);
        String[] partArray = fileName.split("\\.");
        String imageName = partArray[0];
        int resourceId = resources.getIdentifier(imageName, "drawable", _context.getPackageName());
        return resources.getDrawable(resourceId);
    }

    public float getFloat(String localName, String globalName) throws NoSuchFieldException {
        if(localPageHas(localName)){
            return _localLook.getFloatFromNode(localName);
        } else {
            return _globalLook.getFloatFromNode(globalName);
        }
    }

    public int getInt(String localName, String globalName) throws NoSuchFieldException {
        if(localPageHas(localName)){
            return _localLook.getIntegerFromNode(localName);
        } else {
            return _globalLook.getIntegerFromNode(globalName);
        }
    }

    public String getString(String localName, String globalName) throws NoSuchFieldException {
        if(localPageHas(localName)){
            return _localLook.getStringFromNode(localName);
        } else {
            return _globalLook.getStringFromNode(globalName);
        }
    }

    public int getTextStyle(String localName, String globalName) throws NoSuchFieldException {
        String textStyle = getString(localName, globalName);

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

    public boolean localPageHas(String name){
        return _localLook != null && _localLook.hasChild(name);
    }

    public boolean localOrGlobalPageHas(String localname, String globalname){
        return (localPageHas(localname) || _globalLook.hasChild(globalname));
    }
}
