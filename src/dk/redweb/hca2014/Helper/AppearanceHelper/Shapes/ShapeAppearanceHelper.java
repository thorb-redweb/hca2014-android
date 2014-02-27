package dk.redweb.hca2014.Helper.AppearanceHelper.Shapes;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelperGetter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/27/14
 * Time: 12:50
 */
public class ShapeAppearanceHelper {
    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;

    public ShapeAppearanceHelper(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _getter = getter;
    }

    public void setViewBackgroundAsShape(View view, String localBackgroundColorName, String globalBackgroundColorName,
                                         float borderWidth, String localBorderColor, String globalBorderColor, float cornerRadius) throws Exception{
        int backgroundcolor = _getter.getColor(localBackgroundColorName, globalBackgroundColorName);
        int bordercolor = _getter.getColor(localBorderColor, globalBorderColor);
        float[] radii = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};

        Drawable[] layers = new Drawable[2];

        ShapeDrawable sd1 = new ShapeDrawable(new RoundRectShape(radii, null, null));
        sd1.getPaint().setColor(bordercolor);
        sd1.setPadding((int)borderWidth, (int)borderWidth, (int)borderWidth, (int)borderWidth);
//        ShapeDrawable shapeDrawable = new CustomShapeDrawable(roundRectShape, backgroundcolor,bordercolor,borderWidth);
//        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        ShapeDrawable sd2 = new ShapeDrawable(new RoundRectShape(radii, null, null));
        sd2.getPaint().setColor(backgroundcolor);

        layers[0] = sd1;
        layers[1] = sd2;
        LayerDrawable composite = new LayerDrawable(layers);

        view.setBackground(composite);
    }

    public void setViewBackgroundAsShapeWithGradiant(View view, String localBackgroundColorNameTop, String globalBackgroundColorNameTop,
                                                     String localBackgroundColorNameBottom, String globalBackgroundColorNameBottom,
                                         int borderWidth, String localBorderColor, String globalBorderColor, float cornerRadius) throws Exception{
        int backgroundcolortop = _getter.getColor(localBackgroundColorNameTop, globalBackgroundColorNameTop);
        int backgroundcolorbottom = _getter.getColor(localBackgroundColorNameBottom, globalBackgroundColorNameBottom);
        int bordercolor = _getter.getColor(localBorderColor, globalBorderColor);
        float[] radii = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};

        Drawable[] layers = new Drawable[2];

        ShapeDrawable sd1 = new ShapeDrawable(new RoundRectShape(radii, null, null));
        sd1.getPaint().setColor(bordercolor);
        sd1.setPadding(borderWidth, borderWidth, borderWidth, borderWidth);

        GradientDrawable sd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {backgroundcolortop, backgroundcolorbottom});
        sd2.setCornerRadius(cornerRadius);

        layers[0] = sd1;
        layers[1] = sd2;
        LayerDrawable composite = new LayerDrawable(layers);
        view.setBackground(composite);
    }

    public void setViewThreeSides(View view, String localBackgroundColorName, String globalBackgroundColorName,
                                  int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
        Drawable[] layers = new Drawable[2];

        int backgroundColor = _getter.getColor(localBackgroundColorName, globalBackgroundColorName);
        int borderColor = _getter.getColor(localBorderColor, globalBorderColor);

        ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
        sd1.getPaint().setColor(borderColor);
        sd1.setPadding(0, borderWidth, borderWidth, borderWidth);

        ShapeDrawable sd2 = new ShapeDrawable(new RectShape());
        sd2.getPaint().setColor(backgroundColor);

        layers[0] = sd1;
        layers[1] = sd2;
        LayerDrawable composite = new LayerDrawable(layers);
        view.setBackground(composite);
    }

    public void setViewBackgroundWithBorder(View view, String localBackgroundColor, String globalBackgroundColor, int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
        Drawable[] layers = new Drawable[2];

        int backgroundColor = _getter.getColor(localBackgroundColor, globalBackgroundColor);
        int borderColor = _getter.getColor(localBorderColor, globalBorderColor);

        ShapeDrawable sd1 = new ShapeDrawable(new RectShape());
        sd1.getPaint().setColor(borderColor);
        sd1.setPadding(borderWidth, borderWidth, borderWidth, borderWidth);

        ShapeDrawable sd2 = new ShapeDrawable(new RectShape());
        sd2.getPaint().setColor(backgroundColor);

        layers[0] = sd1;
        layers[1] = sd2;
        LayerDrawable composite = new LayerDrawable(layers);
        view.setBackground(composite);
    }

//    public void setViewWithBorder(View view, int borderWidth, String localBorderColor, String globalBorderColor) throws NoSuchFieldException {
//        int borderColor = _getter.getColor(localBorderColor, globalBorderColor);
//        float[] radii = {15,15,15,15,0,0,0,0};
//
//        ShapeDrawable sd1 = new RoundRectBorder(new RoundRectShape(radii, null, null));
//        sd1.getPaint().setColor(borderColor);
//        sd1.getPaint().setStrokeWidth(borderWidth);
//
//        view.setBackground(sd1);
//    }
}
