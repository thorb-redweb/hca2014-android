package dk.redweb.Red_App.Helper.AppearanceHelper.Labels;

import android.graphics.Typeface;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;
import dk.redweb.Red_App.StaticNames.LOOK;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:14
 */
public class TextViewAppearanceForwarder {
    private AppearanceHelper _helper;
    private TextViewAppearanceHelper _textHelper;

    public TextViewAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _helper = helper;
        _textHelper = new TextViewAppearanceHelper(helper, getter);
    }

    public void setColor(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setColor(textView, localName, globalName);
    }

    public void setColor(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setColor(textView, localName, globalName);
        }
    }

    public void setSize(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setSize(textView, localName, globalName);
    }

    public void setSize(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setSize(textView, localName, globalName);
        }
    }

    public void setStyle(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setStyle(textView, localName, globalName);
    }

    public void setStyle(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setStyle(textView, localName, globalName);
        }
    }

    public void setShadow(TextView textView, String localColorName, String globalColorName,
                          String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        _textHelper.setShadow(textView, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setShadow(TextView[] textViews, String localColorName, String globalColorName,
                          String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (TextView textView : textViews)
            _textHelper.setShadow(textView, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setCustomTextStyle(TextView textView, String tag, String defaultColor, String defaultSize) throws NoSuchFieldException {
        String localColor = tag + LOOK.APPEARANCEHELPER_DEF_COLOR;
        String globalColor = defaultColor + LOOK.APPEARANCEHELPER_DEF_TEXTCOLOR;
        String localSize = tag + LOOK.APPEARANCEHELPER_DEF_SIZE;
        String globalSize = defaultSize + LOOK.APPEARANCEHELPER_DEF_SIZE;
        String localStyle = tag + LOOK.APPEARANCEHELPER_DEF_STYLE;
        String globalStyle = defaultSize + LOOK.APPEARANCEHELPER_DEF_STYLE;
        String localShadowColor = tag + LOOK.APPEARANCEHELPER_DEF_SHADOWCOLOR;
        String globalShadowColor = defaultColor + LOOK.APPEARANCEHELPER_DEF_TEXTSHADOWCOLOR;
        String localShadowOffset = tag + LOOK.APPEARANCEHELPER_DEF_SHADOWOFFSET;
        String globalShadowOffset = defaultSize + LOOK.APPEARANCEHELPER_DEF_SHADOWOFFSET;

        setColor(textView, localColor, globalColor);
        setSize(textView, localSize, globalSize);
        setStyle(textView, localStyle, globalStyle);
        setShadow(textView, localShadowColor, globalShadowColor, localShadowOffset, globalShadowOffset);
    }
}
