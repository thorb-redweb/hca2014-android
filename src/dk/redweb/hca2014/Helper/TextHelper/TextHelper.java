package dk.redweb.hca2014.Helper.TextHelper;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/3/13
 * Time: 9:32 AM
 */
public class TextHelper {
    View view;
    XmlNode textStore;

    public TextHelper(Activity activity, String pageName, XmlStore xml) throws Exception {
        view = activity.getWindow().getDecorView();
        if(xml.pageHasText(pageName)){
            textStore = xml.getTextForPage(pageName);
        } else {
            textStore = xml.getEmptyNode();
        }
    }

    public TextHelper(View view, String pageName, XmlStore xml) throws Exception {
        this.view = view;
        if(xml.pageHasText(pageName)){
            textStore = xml.getTextForPage(pageName);
        } else {
            textStore = xml.getEmptyNode();
        }
    }

    public void setText(int id, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        TextView textView = (TextView)view.findViewById(id);
        textView.setText(text);
    }

    public void setText(TextView textView, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);
        textView.setText(text);
    }

    public void tryText(int id, String textName) throws NoSuchFieldException {
        if(textStore.hasChild(textName)){
            setText(id,textName,"");
        }
    }

    public void setEditTextHint(int id, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        EditText editText = (EditText)view.findViewById(id);
        editText.setHint(text);
    }

    public void setEditTextHint(EditText editText, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        editText.setHint(text);
    }

    public void setFlexibleButtonText(int id, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        FlexibleButton button = (FlexibleButton)view.findViewById(id);
        button.setText(text);
    }

    public void setFlexibleButtonText(FlexibleButton button, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        button.setText(text);
    }

    public void setSwitchText(Switch theswitch, String textNameOn, String defaultTextOn, String textNameOff, String defaultTextOff) throws NoSuchFieldException {
        String textOn = getText(textNameOn, defaultTextOn);
        String textOff = getText(textNameOff, defaultTextOff);

        theswitch.setTextOn(textOn);
        theswitch.setTextOff(textOff);
    }

    public String getText(String textName, String defaultText) throws NoSuchFieldException {
        if(textStore.hasChild(textName)){
            return getTextFromStore(textName);
        } else {
            return defaultText;
        }
    }

    private String getTextFromStore(String textName) throws NoSuchFieldException {
        String text = textStore.getStringFromNode(textName);
        if(text.equals("\"\"")){
            text = "";
        }
        return text;
    }
}
