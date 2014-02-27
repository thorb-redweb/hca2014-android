package dk.redweb.hca2014.ViewControllers.RedUpload.RedUploadFolder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Model.RedUploadServerSessionFolder;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.NavController;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BaseViewItem;
import dk.redweb.hca2014.Views.FlexibleButton;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/22/14
 * Time: 12:58
 */
public class RedUploadSessionFolderItem extends BaseViewItem {

    private RelativeLayout _rltBoxBackground;
    private TextView _lblTime;
    private TextView _lblBody;
    private FlexibleButton _flxPicture;
    private FlexibleButton _flxArchive;

    private RedUploadServerSessionFolder _datasource;

    public RedUploadSessionFolderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initializeItem(RedUploadServerSessionFolder datasource, XmlNode page, FragmentActivity activity){
        super.initWithPage(page, activity);

        _datasource = datasource;

        fetchControls();
        setValues();
        setControls();
        setCellContent();
        setAppearance();
        setText();
    }

    protected void fetchControls(){
        _rltBoxBackground = (RelativeLayout)findViewById(R.id.reduploadfolder_rltBoxBackground);
        _lblTime = (TextView)findViewById(R.id.reduploadfolder_lblTime);
        _lblBody = (TextView)findViewById(R.id.reduploadfolder_lblBody);
        _flxPicture = (FlexibleButton)findViewById(R.id.reduploadfolder_flxpicture);
        _flxArchive = (FlexibleButton)findViewById(R.id.reduploadfolder_flxarchive);

        this.setTag(_flxPicture.getTag());
        this.setTag(_flxArchive.getTag());
    }

    protected void setValues(){

    }

    protected void setControls(){
        _flxPicture.setOnClickListener(flxPictureClicked());
        _flxArchive.setOnClickListener(flxArchiveClicked());
    }

    protected void setCellContent(){
        _lblTime.setText(_datasource.getTime());
        _lblBody.setText(_datasource.getName());
    }

    protected void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;

            helper.Shape.setViewBackgroundAsShape(_rltBoxBackground, LOOK.REDUPLOAD_ITEMBACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR, 3, LOOK.REDUPLOAD_ITEMBORDERCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR, 15);
            helper.setViewBackgroundColor(_flxPicture, LOOK.REDUPLOAD_CAMERABUTTONCOLOR, LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setImage(_flxPicture, "camerabuttonimage");
            if(_db.RedUpload.serverFolderHasEntries(_datasource.getServerFolder())){
                helper.Shape.setViewThreeSides(_flxArchive, LOOK.REDUPLOAD_ITEMBACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR, 3, LOOK.REDUPLOAD_ITEMBORDERCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            }
            else{
                helper.Shape.setViewThreeSides(_flxArchive, null, LOOK.LIGHTGREY, 3, LOOK.REDUPLOAD_ITEMBORDERCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            }
            helper.FlexButton.setImage(_flxArchive, "archivebuttonimage");

            helper.TextView.setAltItemTitleStyle(_lblTime);
            helper.TextView.setAltTextStyle(_lblBody);
        } catch (Exception e) {
            MyLog.e("Exception when setting appearance of RedUploadSessionFolderItem", e);
        }
    }

    protected void setText(){

    }

    private OnClickListener flxPictureClicked(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode nextPage = _xml.getPage(_page.getStringFromNode(PAGE.CHILD)).deepClone();
                    nextPage.addChildToNode(PAGE.REDUPLOADFOLDERID, _datasource.getFolderId());
                    NavController.changePageWithXmlNode(nextPage, _activity);
                } catch (Exception e) {
                    MyLog.e("Exception when handling flxPicture click event", e);
                };
            }
        };
    }

    private OnClickListener flxArchiveClicked(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    XmlNode nextPage = _xml.getPage(_page.getStringFromNode(PAGE.CHILD2)).deepClone();
                    nextPage.addChildToNode(PAGE.REDUPLOADFOLDERID, _datasource.getFolderId());
                    NavController.changePageWithXmlNode(nextPage, _activity);
                } catch (Exception e) {
                    MyLog.e("Exception when handling flxArchive click event", e);
                };
            }
        };
    }
}
