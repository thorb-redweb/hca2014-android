package dk.redweb.Red_App.Helper.AppearanceHelper.Labels;

import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelperGetter;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/13/14
 * Time: 9:14
 */
public class TextViewAppearanceForwarder {
    private TextViewAppearanceHelper _textHelper;

    public TextViewAppearanceForwarder(AppearanceHelper helper, AppearanceHelperGetter getter){
        _textHelper = new TextViewAppearanceHelper(helper, getter);
    }

    public void setTextColor(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setTextColor(textView, localName, globalName);
    }

    public void setTextColor(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setTextColor(textView, localName, globalName);
        }
    }

    public void setTextSize(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setTextSize(textView, localName, globalName);
    }

    public void setTextSize(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setTextSize(textView, localName, globalName);
        }
    }

    public void setTextStyle(TextView textView, String localName, String globalName) throws NoSuchFieldException {
        _textHelper.setTextStyle(textView, localName, globalName);
    }

    public void setTextStyle(TextView[] textViews, String localName, String globalName) throws NoSuchFieldException {
        for(TextView textView : textViews){
            _textHelper.setTextStyle(textView, localName, globalName);
        }
    }

    public void setTextShadow(TextView textView, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        _textHelper.setTextShadow(textView, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }

    public void setTextShadow(TextView[] textViews, String localColorName, String globalColorName,
                              String localOffsetName, String globalOffsetName) throws NoSuchFieldException{
        for (TextView textView : textViews)
            _textHelper.setTextShadow(textView, localColorName, globalColorName, localOffsetName, globalOffsetName);
    }
}
