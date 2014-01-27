package dk.redweb.Red_App.Helper.AppearanceHelper.Shapes;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import dk.redweb.Red_App.CustomShapeDrawable;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/27/14
 * Time: 12:49
 */
public class ShapeAppearanceForwarder {
    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;
    private ShapeAppearanceHelper _shapeHelper;

    public ShapeAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _getter = getter;
        _shapeHelper = new ShapeAppearanceHelper(helper, getter);
    }

    public void setViewThreeSides(View view, String localBackgroundColorName, String globalBackgroundColorName,
                                  int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
        _shapeHelper.setViewThreeSides(view, localBackgroundColorName, globalBackgroundColorName, borderWidth, localBorderColor, globalBorderColor);
    }

    public void setViewBackgroundWithBorder(View view, String localBackgroundColor, String globalBackgroundColor, int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
        _shapeHelper.setViewBackgroundWithBorder(view, localBackgroundColor, globalBackgroundColor, borderWidth, localBorderColor, globalBorderColor);
    }

    public void setViewBackgroundAsShape(View view, String localBackgroundColorName, String globalBackgroundColorName,
                                         String localBorderWidth, String localBorderColor, String globalBorderColor, String localCornerRadiusName) throws Exception{
        float radius = _getter.getFloat(localCornerRadiusName, null);
        float borderwidth = _getter.getFloat(localBorderWidth, null);
        _shapeHelper.setViewBackgroundAsShape(view, localBackgroundColorName, globalBackgroundColorName, borderwidth, localBorderColor, globalBorderColor, radius);
    }

    public void setViewBackgroundAsShape(View view, String localBackgroundColorName, String globalBackgroundColorName,
                                         int borderWidth, String localBorderColor, String globalBorderColor, int cornerRadius) throws Exception{
        _shapeHelper.setViewBackgroundAsShape(view, localBackgroundColorName, globalBackgroundColorName, borderWidth, localBorderColor, globalBorderColor, cornerRadius);
    }

    public void setViewBackgroundAsShape(View[] views, String localBackgroundColorName, String globalBackgroundColorName, String borderwidth, String localBorderColor, String globalBorderColor, String cornerRadius) throws Exception {
        for(View view : views){
            setViewBackgroundAsShape(view, localBackgroundColorName, globalBackgroundColorName, borderwidth, localBorderColor, globalBorderColor, cornerRadius);
        }
    }

    public void setViewBackgroundAsShapeWithGradiant(View view, String localBackgroundColorNameTop, String globalBackgroundColorNameTop,
                                         String localBackgroundColorNameBottom, String globalBackgroundColorNameBottom,
                                         int borderWidth, String localBorderColor, String globalBorderColor, float cornerRadius) throws Exception {
        _shapeHelper.setViewBackgroundAsShapeWithGradiant(view, localBackgroundColorNameTop, globalBackgroundColorNameTop, localBackgroundColorNameBottom, globalBackgroundColorNameBottom,
                borderWidth, localBorderColor, globalBorderColor, cornerRadius);
    }

    public void setViewBackgroundAsShapeWithGradiant(View view, String localBackgroundColorNameTop, String globalBackgroundColorNameTop,
                                                     String localBackgroundColorNameBottom, String globalBackgroundColorNameBottom,
                                                     String localBorderWidth, String localBorderColor, String globalBorderColor, String localCornerRadius) throws Exception {
        float radius = _getter.getFloat(localCornerRadius, null);
        int borderwidth = _getter.getInt(localBorderWidth, null);
        setViewBackgroundAsShapeWithGradiant(view, localBackgroundColorNameTop, globalBackgroundColorNameTop, localBackgroundColorNameBottom, globalBackgroundColorNameBottom,
                borderwidth, localBorderColor, globalBorderColor, radius);
    }

//    public void setViewWithBorder(View view, int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
//        _shapeHelper.setViewWithBorder(view, borderWidth, localBorderColor, globalBorderColor);
//    }
}
