package dk.redweb.Red_App.Helper.AppearanceHelper.Labels;

import android.util.TypedValue;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:14
 */
public class TextViewAppearanceHelper {

    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;

    public TextViewAppearanceHelper(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _getter = getter;
    }

    public void setTextColor(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTextColor(_getter.getColor(localName, globalName));
    }

    public void setTextSize(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, _getter.getFloat(localName, globalName));
    }

    public void setTextStyle(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTypeface(null, _getter.getTextStyle(localName, globalName));
    }

    public void setTextShadow(TextView textView, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{

        if(_getter.localOrGlobalPageHas(localColorName, globalColorName) && _getter.localOrGlobalPageHas(localOffsetName, globalOffsetName))
        {
            int shadowColor = _getter.getColor(localColorName, globalColorName);
            float[] shadowOffset = _getter.getCoords(localOffsetName, globalOffsetName);
            textView.setShadowLayer(1,shadowOffset[0],shadowOffset[1],shadowColor);
        }
    }
}
