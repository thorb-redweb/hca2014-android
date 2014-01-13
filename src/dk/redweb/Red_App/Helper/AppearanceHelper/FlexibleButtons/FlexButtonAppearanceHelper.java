package dk.redweb.Red_App.Helper.AppearanceHelper.FlexibleButtons;

import android.util.TypedValue;
import android.view.View;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;
import dk.redweb.Red_App.Views.FlexibleButton;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:14
 */
public class FlexButtonAppearanceHelper {
    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;

    public FlexButtonAppearanceHelper(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _getter = getter;
    }

    public void setImage(FlexibleButton flexButton, String localName) throws Exception {
        if(_getter.localPageHas(localName)) {
            flexButton.setImageDrawable(_getter.getDrawableFromResource(localName, null));
        } else {
            flexButton.setImageVisibility(View.GONE);
        }
    }

    public void setTextColor(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        flexButton.setTextColor(_getter.getColor(localName, globalName));

    }

    public void setTextSize(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {

        flexButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, _getter.getFloat(localName, globalName));
    }

    public void setTextStyle(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        flexButton.setTypeface(null, _getter.getTextStyle(localName, globalName));
    }

    public void setTextShadow(FlexibleButton flexButton, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        if(_getter.localOrGlobalPageHas(localColorName, globalColorName) && _getter.localOrGlobalPageHas(localOffsetName, globalOffsetName))
        {
            int shadowColor = _getter.getColor(localColorName, globalColorName);
            float[] shadowOffset = _getter.getCoords(localOffsetName, globalOffsetName);
            flexButton.setTextShadowLayer(1, shadowOffset[0], shadowOffset[1], shadowColor);
        }
    }

    public void setTextShadow(FlexibleButton[] flexButtons, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (FlexibleButton flexButton : flexButtons)
            setTextShadow(flexButton, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }
}
