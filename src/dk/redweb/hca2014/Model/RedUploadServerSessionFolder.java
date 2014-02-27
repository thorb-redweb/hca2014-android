package dk.redweb.hca2014.Model;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:46
 */
public class RedUploadServerSessionFolder extends RedUploadServerFolder{
    private String _time;

    public RedUploadServerSessionFolder(int id, String name, String time, String serverFolder) {
        super(id, name, serverFolder);
        _time = time;
    }

    public RedUploadServerSessionFolder(int id, String name, String serverFolder) {
        super(id, name, serverFolder);
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String time) {
        _time = time;
    }
}
