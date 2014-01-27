package dk.redweb.Red_App.Model;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:46
 */
public class RedUploadServerOtherFolder extends RedUploadServerFolder{
    private RedUploadServerOtherFolder _parent;

    public int getLevel(){
        if(_parent != null){
            return _parent.getLevel() + 1;
        }
        return 0;
    }

    public RedUploadServerOtherFolder getParent() {
        return _parent;
    }

    public void setParent(RedUploadServerOtherFolder parent) {
        _parent = parent;
    }

    public RedUploadServerOtherFolder(int id, String name, RedUploadServerOtherFolder parent, String serverFolder) {
        super(id, name, serverFolder);
        _parent = parent;
    }

    public RedUploadServerOtherFolder(int id, String name, String serverFolder) {
        super(id, name, serverFolder);
    }

}
