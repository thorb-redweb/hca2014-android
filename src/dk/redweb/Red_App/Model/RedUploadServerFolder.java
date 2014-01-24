package dk.redweb.Red_App.Model;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:46
 */
public class RedUploadServerFolder {
    private int _folderId;
    private String _name;
    private String _serverFolder;


    public int getFolderId() {
        return _folderId;
    }

    public void setFolderId(int folderId) {
        _folderId = folderId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getServerFolder() {
        return _serverFolder;
    }

    public void setServerFolder(String serverFolder) {
        _serverFolder = serverFolder;
    }

    public RedUploadServerFolder(int id, String name, String serverFolder){
        _folderId = id;
        _name = name;
        _serverFolder = serverFolder;
    }

}
