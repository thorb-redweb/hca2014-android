package dk.redweb.Red_App.Helper.AppearanceHelper.FlexibleButtons;

import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;
import dk.redweb.Red_App.Views.FlexibleButton;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:13
 */
public class FlexButtonAppearanceForwarder {
    private AppearanceHelperGetter _getter;
    private FlexButtonAppearanceHelper _flxBtnHelper;

    public FlexButtonAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _getter = getter;
        _flxBtnHelper = new FlexButtonAppearanceHelper(helper, getter);
    }

    public void setImage(FlexibleButton flexButton, String localName) throws Exception {
        _flxBtnHelper.setImage(flexButton, localName);
    }

    public void setImage(FlexibleButton[] flexButtons, String localName) throws Exception {
        for(FlexibleButton flexButton : flexButtons){
            _flxBtnHelper.setImage(flexButton, localName);
        }
    }

    public void setTextColor(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        _flxBtnHelper.setTextColor(flexButton, localName, globalName);

    }

    public void setTextColor(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException{
        for(FlexibleButton flexButton : flexButtons){
            _flxBtnHelper.setTextColor(flexButton, localName, globalName);
        }
    }

    public void setTextSize(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        _flxBtnHelper.setTextSize(flexButton, localName, globalName);
    }

    public void setTextSize(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException {
        for(FlexibleButton flexButton : flexButtons){
            _flxBtnHelper.setTextSize(flexButton, localName, globalName);
        }
    }

    public void setTextStyle(FlexibleButton flexButton, String localName, String globalName) throws NoSuchFieldException {
        _flxBtnHelper.setTextStyle(flexButton, localName, globalName);
    }

    public void setTextStyle(FlexibleButton[] flexButtons, String localName, String globalName) throws NoSuchFieldException {
        for(FlexibleButton flexButton : flexButtons){
            _flxBtnHelper.setTextStyle(flexButton, localName, globalName);
        }
    }

    public void setTextShadow(FlexibleButton flexButton, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        _flxBtnHelper.setTextShadow(flexButton, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setTextShadow(FlexibleButton[] flexButtons, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (FlexibleButton flexButton : flexButtons)
            _flxBtnHelper.setTextShadow(flexButton, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }
}
