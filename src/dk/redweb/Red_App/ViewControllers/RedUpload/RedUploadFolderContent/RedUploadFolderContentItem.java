package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolderContent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.DatabaseModel.RedUploadImage;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.ViewControllers.BaseViewItem;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import static android.R.color.holo_red_dark;
import static android.R.color.holo_red_light;
import static android.R.color.white;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/23/14
 * Time: 16:13
 */
public class RedUploadFolderContentItem extends BaseViewItem{
    RedUploadImage _datasource;
    Drawable _thumbnail;

    ImageView _imgPicture;
    TextView _lblTitle;
    LinearLayout _lnrApprovedTag;
    LinearLayout _lnrBottomArea;

    public RedUploadFolderContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initializeItem(RedUploadImage datasource, Drawable thumbnail, XmlNode itempage, FragmentActivity activity){
        super.initWithPage(itempage, activity);

        _datasource = datasource;
        _thumbnail = thumbnail;

        fetchControls();
        setValues();
        setControls();
        setCellContent();
        setAppearance();
        setText();
    }

    protected void fetchControls(){
        _imgPicture = (ImageView)findViewById(R.id.reduploadfoldercontent_imgPicture);
        _lblTitle = (TextView)findViewById(R.id.reduploadfoldercontent_lblTitle);
        _lnrApprovedTag = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lnrApprovedTag);
        _lnrBottomArea = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lrnBottomArea);
    }

    protected void setValues(){

    }

    protected void setControls(){

    }

    protected void setCellContent(){
        _imgPicture.setImageDrawable(_thumbnail);
        _lblTitle.setText(_datasource.text);
    }

    protected void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;
            if(_page.getBoolWithNoneAsFalseFromNode("Selected")){
                helper.setViewBackgroundColor(_lnrBottomArea, "", LOOK.BLACK);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting appearance for RedUploadFolderContentItem", e);
        }
    }

    protected void setText(){

    }

    public void setSelected(){
        try {
            _appearanceHelper.setViewBackgroundColor(_lnrBottomArea, "", LOOK.BLACK);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when changing appearance due to selection of RedUploadFolderContentItem", e);
        }
    }

    public void setDeselected(){
        try {
            _appearanceHelper.setViewBackgroundColor(_lnrBottomArea, "", LOOK.WHITE);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when changing appearance due to deselection of RedUploadFolderContentItem", e);
        }
    }
}
