package dk.redweb.hca2014.Helper.AppearanceHelper.Labels;

import android.util.TypedValue;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelperGetter;

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

    public void setColor(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTextColor(_getter.getColor(localName, globalName));
    }

    public void setSize(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, _getter.getFloat(localName, globalName));
    }

    public void setStyle(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        textView.setTypeface(null, _getter.getTextStyle(localName, globalName));
    }

    public void setShadow(TextView textView, String localColorName, String globalColorName,
                          String localOffsetName, String globalOffsetName) throws NoSuchFieldException{

        if(_getter.localOrGlobalPageHas(localColorName, globalColorName) && _getter.localOrGlobalPageHas(localOffsetName, globalOffsetName))
        {
            int shadowColor = _getter.getColor(localColorName, globalColorName);
            float[] shadowOffset = _getter.getCoords(localOffsetName, globalOffsetName);
            textView.setShadowLayer(1,shadowOffset[0],shadowOffset[1],shadowColor);
        }
    }
}
