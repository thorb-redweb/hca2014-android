package dk.redweb.hca2014.ViewModels;

import dk.redweb.hca2014.DatabaseModel.PushMessageGroup;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/31/13
 * Time: 14:05
 */
public class PushMessageGroupVM {
    private PushMessageGroup _pushMessageGroup;

    public PushMessageGroupVM(PushMessageGroup pushMessageGroup) {
        _pushMessageGroup = pushMessageGroup;
    }

    public int GroupId(){
        return _pushMessageGroup.GroupId;
    }

    public String Name(){
        return _pushMessageGroup.Name;
    }

    public boolean Subscribing(){
        return _pushMessageGroup.Subscribing;
    }
}
