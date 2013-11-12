package dk.redweb.Red_App.Interfaces;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/25/13
 * Time: 12:38
 */
public interface Delegate_uploadRegistrationAttributes {
    public void returnFromUploadToServer(String result);
    public void errorOccured(String errorMessage);
}
