package dk.redweb.Red_App.Interfaces;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 12/2/13
 * Time: 13:21
 */
public interface Delegate_biketracker {
    public void averageSpeedUpdated(String averageSpeed);
    public void topSpeedUpdated(String topSpeed);
    public void distanceUpdated(String distance);
}
