package dk.redweb.hca2014.Model;

import dk.redweb.hca2014.Database.DbInterface;
import dk.redweb.hca2014.DatabaseModel.RedUploadImage;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 10:45
 */
public class RedUploadDataStore extends VolatileStore {
    private RedUploadServerSessionFolder[] _sessionFolders;
    public RedUploadServerSessionFolder[] getSessionFolders() {return _sessionFolders; }
    private RedUploadServerOtherFolder[] _otherFolders;
    public RedUploadServerOtherFolder[] getOtherFolders() {return _otherFolders; }

    public RedUploadDataStore(DbInterface db){
        _sessionFolders = new RedUploadServerSessionFolder[3];
        _sessionFolders[0] = new RedUploadServerSessionFolder(1, "Odense Marcipan og Claus Bager", "Kl. 10.00 - 12.00", "odense");
        _sessionFolders[1] = new RedUploadServerSessionFolder(2, "Klaveret, der kom forbi - med Findus og Den Halve Kvartet", "Kl. 10.00", "klaver");
        _sessionFolders[2] = new RedUploadServerSessionFolder(3, "Medicinernes Luciakor", "Efter mørkets frembrud", "luciakor");

        _otherFolders = new RedUploadServerOtherFolder[6];
        _otherFolders[0] = new RedUploadServerOtherFolder(4, "Art slideshow", null, "art");
        _otherFolders[1] = new RedUploadServerOtherFolder(5, "The Hospital", null, "hospital");
        RedUploadServerOtherFolder miscFolder = new RedUploadServerOtherFolder(6, "Miscellaneous", null, "misc");
        _otherFolders[2] = miscFolder;
        _otherFolders[3] = new RedUploadServerOtherFolder(7, "Children playing", miscFolder, "children");
        _otherFolders[4] = new RedUploadServerOtherFolder(8, "Families", miscFolder, "family");
        _otherFolders[5] = new RedUploadServerOtherFolder(9, "The personel", null, "personel");

        cleanUpImagesWithServerFoldersThatNoLongerExist(db);

    }

    public void cleanUpImagesWithServerFoldersThatNoLongerExist(DbInterface db){
        RedUploadImage[] redUploadImages = db.RedUpload.getAll();

        for (RedUploadImage image : redUploadImages) {
            boolean sessionFolderDoesntExist = true;
            for (RedUploadServerFolder folder : _sessionFolders) {
                if (folder.getServerFolder().equals(image.serverFolder)) {
                    sessionFolderDoesntExist = false;
                    break;
                }
            }
            boolean otherFolderDoesntExist = true;
            for (RedUploadServerFolder folder : _otherFolders) {
                if (folder.getServerFolder().equals(image.serverFolder)) {
                    otherFolderDoesntExist = false;
                    break;
                }
            }
            if(sessionFolderDoesntExist && otherFolderDoesntExist){
                db.RedUpload.deleteEntryWithImagePath(image.localImagePath);
            }
        }
    }

    public RedUploadServerFolder getFolder(int folderId){
        for (RedUploadServerFolder folder : _sessionFolders) {
            if (folderId == folder.getFolderId()) {
                return folder;
            }
        }
        for (RedUploadServerFolder folder : _otherFolders) {
            if (folderId == folder.getFolderId()) {
                return folder;
            }
        }
        return null;
    }
}
