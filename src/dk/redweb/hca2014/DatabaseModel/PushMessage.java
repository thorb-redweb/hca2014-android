package dk.redweb.hca2014.DatabaseModel;

import org.joda.time.DateTime;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/22/13
 * Time: 15:31
 */
public class PushMessage {
    public int PushMessageId;
    public int GroupId;
    public String Intro;
    public String Message;
    public String Author;
    public DateTime SendDate;
}
