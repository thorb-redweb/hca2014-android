package dk.redweb.Red_App.Interfaces;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 11:22 AM
 */
public interface IDbInterface {
    public <T extends Object> T GetLazyLoadedObject(int wantedObjectsId, Class<T> wantedType, Type askingType);
    public <T extends Object> ArrayList<T> GetLazyLoadedObjects(int askingObjectsId, Class<T> wantedType, Type askingType);
}
