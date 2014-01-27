package dk.redweb.Red_App.ViewControllers.RedUpload.RedUploadFolderContent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dk.redweb.Red_App.DatabaseModel.RedUploadImage;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.ViewControllers.BaseViewItem;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/23/14
 * Time: 16:13
 */
public class RedUploadFolderContentItem extends BaseViewItem{
    RedUploadImage _redUploadImageObject;
    Drawable _thumbnail;

    RelativeLayout _rltItemBox;
    LinearLayout _lnrImageBox;
    ImageView _imgPicture;
    TextView _lblTitle;
    LinearLayout _lnrApprovedTag;
    LinearLayout _lnrBottomArea;
    LinearLayout _lnrItemBoxBorder;

    public RedUploadFolderContentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initializeItem(RedUploadImage datasource, Drawable thumbnail, XmlNode itempage, FragmentActivity activity){
        super.initWithPage(itempage, activity);

        _redUploadImageObject = datasource;
        _thumbnail = thumbnail;

        fetchControls();
        setValues();
        setControls();
        setCellContent();
        setAppearance();
        setText();
    }

    protected void fetchControls(){
        _rltItemBox = (RelativeLayout)findViewById(R.id.reduploadfoldercontent_rltItemBox);
        _lnrImageBox = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lnrImageBox);
        _imgPicture = (ImageView)findViewById(R.id.reduploadfoldercontent_imgPicture);
        _lblTitle = (TextView)findViewById(R.id.reduploadfoldercontent_lblTitle);
        _lnrApprovedTag = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lnrApprovedTag);
        _lnrBottomArea = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lrnBottomArea);
        _lnrItemBoxBorder = (LinearLayout)findViewById(R.id.reduploadfoldercontent_lnrItemBoxBorder);
    }

    protected void setValues(){

    }

    protected void setControls(){

    }

    protected void setCellContent(){
        _imgPicture.setImageDrawable(_thumbnail);
        _lblTitle.setText(_redUploadImageObject.text);
    }

    protected void setAppearance(){
        try{
            AppearanceHelper helper = _appearanceHelper;

            helper.setViewBackgroundColor(_lnrImageBox, "imagebackgroundcolor", LOOK.GLOBAL_BACKCOLOR);

            helper.Shape.setViewBackgroundAsShape(_rltItemBox, "itembackgroundcolor", LOOK.GLOBAL_BACKCOLOR, "borderwidth", "itembordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            helper.TextView.setBackItemTitleStyle(_lblTitle);
            if(_redUploadImageObject.approved){
                helper.setViewBackgroundColor(_lnrApprovedTag, "approveditemmarkercolor", LOOK.GLOBAL_ALTCOLOR);
            }
            else {
                helper.setViewBackgroundColor(_lnrApprovedTag, "unapproveditemmarkercolor", LOOK.GLOBAL_BARCOLOR);
            }

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance for RedUploadFolderContentItem", e);
        }
    }

    protected void setText(){

    }

    public void setSelected(){
        try {
            _appearanceHelper.Shape.setViewBackgroundAsShape(_rltItemBox, "selecteditembackgroundcolor", LOOK.GLOBAL_ALTCOLOR, "borderwidth", "itembordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            _appearanceHelper.TextView.setColor(_lblTitle, "selecteditemtextcolor", LOOK.GLOBAL_ALTTEXTCOLOR);
            _appearanceHelper.setViewBackgroundColor(_lnrImageBox, "selecteditembackgroundcolor", LOOK.GLOBAL_BACKCOLOR);
        } catch (Exception e) {
            MyLog.e("Exception when changing appearance due to selection of RedUploadFolderContentItem", e);
        }
    }

    public void setDeselected(){
        try {
            _appearanceHelper.Shape.setViewBackgroundAsShape(_rltItemBox, "itembackgroundcolor", LOOK.GLOBAL_BACKCOLOR, "borderwidth", "itembordercolor", LOOK.GLOBAL_BACKTEXTCOLOR, "corner");
            _appearanceHelper.TextView.setBackItemTitleStyle(_lblTitle);
            _appearanceHelper.setViewBackgroundColor(_lnrImageBox, "imagebackgroundcolor", LOOK.GLOBAL_BACKCOLOR);
        } catch (Exception e) {
            MyLog.e("Exception when changing appearance due to deselection of RedUploadFolderContentItem", e);
        }
    }
}
