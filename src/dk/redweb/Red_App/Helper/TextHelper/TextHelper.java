package dk.redweb.Red_App.Helper.TextHelper;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

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

    public void tryText(int id, String textName) throws NoSuchFieldException {
        if(textStore.hasChild(textName)){
            setText(id,textName,"");
        }
    }

    public void setFlexibleButtonText(int id, String textName, String defaultText) throws NoSuchFieldException {
        String text = getText(textName, defaultText);

        FlexibleButton button = (FlexibleButton)view.findViewById(id);
        button.setText(text);
    }

    public void tryFlexibleButtonText(int id, String textName) throws NoSuchFieldException {
        if(textStore.hasChild(textName)){
            setFlexibleButtonText(id,textName,"");
        }
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
