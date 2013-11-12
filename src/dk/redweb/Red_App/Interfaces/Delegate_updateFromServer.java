package dk.redweb.Red_App.Interfaces;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/16/13
 * Time: 9:44 AM
 */
public interface Delegate_updateFromServer {
    public void returnFromUpdateFromServer(String result);
    public void errorOccured(String errorMessage);
}
