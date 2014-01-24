package dk.redweb.Red_App.Model;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:45
 */
public class VolatileStores extends ArrayList<VolatileStore> {

    public RedUploadDataStore getRedUpload(){
        for(VolatileStore store : this){
            if(store instanceof RedUploadDataStore){
                return (RedUploadDataStore)store;
            }
        }
        return null;
    }
}
