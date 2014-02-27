package dk.redweb.hca2014.ViewModels;

import dk.redweb.hca2014.DatabaseModel.PushMessage;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/22/13
 * Time: 15:47
 */
public class PushMessageVM {

    Locale locale = new Locale("da_DK", "da_DK");
    private PushMessage _pushMessage;

    public PushMessageVM(PushMessage pushMessage) {
        _pushMessage = pushMessage;
    }

    public int PushMessageId(){
        return _pushMessage.PushMessageId;
    }

    public int GroupId(){
        return _pushMessage.GroupId;
    }

    public String Author(){
        return _pushMessage.Author;
    }

    public String Intro(){
        return _pushMessage.Intro;
    }

    public String Message(){
        return _pushMessage.Message;
    }

    public String SendDateWithPattern(String pattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern).withLocale(locale);
        return dateTimeFormatter.print(_pushMessage.SendDate);
    }
}
