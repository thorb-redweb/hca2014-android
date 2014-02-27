package dk.redweb.hca2014.Helper.AppearanceHelper.Labels;

import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelperGetter;
import dk.redweb.hca2014.StaticNames.LOOK;

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

    public void setTitleStyle(TextView textView) throws NoSuchFieldException {
        setColor(textView, LOOK.TITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
        setSize(textView, LOOK.TITLESIZE, LOOK.GLOBAL_TITLESIZE);
        setStyle(textView, LOOK.TITLESTYLE, LOOK.GLOBAL_TITLESTYLE);
        setShadow(textView, LOOK.TITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.TITLESHADOWOFFSET, LOOK.GLOBAL_TITLESHADOWOFFSET);
    }

    public void setBackItemTitleStyle(TextView textView) throws NoSuchFieldException {
        setColor(textView, LOOK.ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
        setSize(textView, LOOK.ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
        setStyle(textView, LOOK.ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
        setShadow(textView, LOOK.ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
    }

    public void setAltItemTitleStyle(TextView textView) throws NoSuchFieldException {
        setColor(textView, LOOK.ITEMTITLECOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
        setSize(textView, LOOK.ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
        setStyle(textView, LOOK.ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
        setShadow(textView, LOOK.ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);
    }

    public void setBackTextStyle(TextView textView) throws NoSuchFieldException {
        setColor(textView, LOOK.TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
        setSize(textView, LOOK.TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
        setStyle(textView, LOOK.TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
        setShadow(textView, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR, LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);
    }

    public void setBackTextStyleForList(TextView[] textViews) throws NoSuchFieldException {
        for(TextView textView : textViews){
            setBackTextStyle(textView);
        }
    }

    public void setAltTextStyle(TextView textView) throws NoSuchFieldException {
        setColor(textView, LOOK.TEXTCOLOR, LOOK.GLOBAL_ALTTEXTCOLOR);
        setSize(textView, LOOK.TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
        setStyle(textView, LOOK.TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
        setShadow(textView, LOOK.TEXTSHADOWCOLOR, LOOK.GLOBAL_ALTTEXTSHADOWCOLOR, LOOK.TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);
    }
}
