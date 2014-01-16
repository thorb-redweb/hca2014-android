package dk.redweb.Red_App.Helper.AppearanceHelper.FlexibleButtons;

import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.Views.FlexibleButton;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:13
 */
public class FlexButtonAppearanceForwarder {
    private AppearanceHelper _helper;
    private AppearanceHelperGetter _getter;
    private FlexButtonAppearanceHelper _flxBtnHelper;

    public FlexButtonAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
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

    public void setCustomStyle(FlexibleButton button, String tag, String defaultColor, String defaultSize) throws Exception {
        String localImage = tag + LOOK.APPEARANCEHELPER_DEF_BACKGROUNDIMAGE;
        String localBackgroundColor = tag + LOOK.APPEARANCEHELPER_DEF_BACKGROUNDCOLOR;
        String globalBackgroundColor = defaultColor + LOOK.APPEARANCEHELPER_DEF_COLOR;
        String localIcon = tag + LOOK.APPEARANCEHELPER_DEF_ICON;

        String localColor = tag + LOOK.APPEARANCEHELPER_DEF_TEXTCOLOR;
        String globalColor = defaultColor + LOOK.APPEARANCEHELPER_DEF_TEXTCOLOR;
        String localSize = tag + LOOK.APPEARANCEHELPER_DEF_TEXTSIZE;
        String globalSize = defaultSize + LOOK.APPEARANCEHELPER_DEF_SIZE;
        String localStyle = tag + LOOK.APPEARANCEHELPER_DEF_TEXTSTYLE;
        String globalStyle = defaultSize + LOOK.APPEARANCEHELPER_DEF_STYLE;
        String localShadowColor = tag + LOOK.APPEARANCEHELPER_DEF_SHADOWCOLOR;
        String globalShadowColor = defaultColor + LOOK.APPEARANCEHELPER_DEF_TEXTSHADOWCOLOR;
        String localShadowOffset = tag + LOOK.APPEARANCEHELPER_DEF_TEXTSHADOWOFFSET;
        String globalShadowOffset = defaultSize + LOOK.APPEARANCEHELPER_DEF_SHADOWOFFSET;

        _helper.setViewBackgroundImageOrColor(button, localImage, localBackgroundColor, globalBackgroundColor);
        setImage(button, localIcon);
        setTextColor(button, localColor, globalColor);
        setTextSize(button, localSize, globalSize);
        setTextStyle(button, localStyle, globalStyle);
        setTextShadow(button, localShadowColor, globalShadowColor, localShadowOffset, globalShadowOffset);
    }

    public void setButtonStyle(FlexibleButton button) throws Exception {
        _helper.setViewBackgroundImageOrColor(button, LOOK.BUTTONBACKGROUNDIMAGE, LOOK.BUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
        setImage(button, LOOK.BUTTONICON);
        setTextColor(button, LOOK.BUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
        setTextSize(button, LOOK.BUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
        setTextStyle(button, LOOK.BUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
        setTextShadow(button, LOOK.BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
    }

    public void setBackButtonStyle(FlexibleButton button) throws Exception {
        _helper.setViewBackgroundImageOrColor(button, LOOK.BACKBUTTONBACKGROUNDIMAGE, LOOK.BACKBUTTONBACKGROUNDCOLOR, LOOK.GLOBAL_ALTCOLOR);
        setImage(button, LOOK.BACKBUTTONICON);
        setTextColor(button, LOOK.BACKBUTTONTEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
        setTextSize(button, LOOK.BACKBUTTONTEXTSIZE, LOOK.GLOBAL_ITEMTITLESIZE);
        setTextStyle(button, LOOK.BACKBUTTONTEXTSTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
        setTextShadow(button, LOOK.BACKBUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.BACKBUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
    }
}
