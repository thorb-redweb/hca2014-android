package dk.redweb.Red_App.Views;

import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 9:46 AM
 */
public class TabbarButton extends LinearLayout {

    private RedEventApplication _app;
    private XmlStore _xml;
    private XmlNode _page;

    private TextView _buttonTextView;
    private ImageView _buttonImageView;

    private FragmentActivity _parentActivity;

    public TabbarButton(Context context, RedEventApplication app) {
        super(context);

        _app = app;
        _xml = app.getXmlStore();

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_tabbarbutton, this);

        loadViews();

        setAppearance();
    }

    private void loadViews(){
        _buttonTextView = (TextView)findViewById(R.id.tabbarButtonText);
        _buttonImageView = (ImageView)findViewById(R.id.tabbarButtonImage);

        this.setOnClickListener(buttonListener());
    }

    private void setAppearance(){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.pageHasAppearance(LOOK.TABBAR))
                localLook = _xml.getAppearanceForPage(LOOK.TABBAR);
            AppearanceHelper helper = new AppearanceHelper(_app.getApplicationContext(), localLook, globalLook);

            helper.TextView.setTextColor(_buttonTextView, LOOK.TABBAR_TEXTCOLOR, LOOK.GLOBAL_BARTEXTCOLOR);
            helper.TextView.setTextSize(_buttonTextView, LOOK.TABBAR_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setTextStyle(_buttonTextView, LOOK.TABBAR_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setTextShadow(_buttonTextView, LOOK.TABBAR_TEXTSHADOWCOLOR, LOOK.GLOBAL_BARTEXTSHADOWCOLOR, LOOK.TABBAR_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            LinearLayout lnrView = (LinearLayout)findViewById(R.id.tabbarButton_lnrView);
            ViewGroup.LayoutParams tabbarButtonParams = lnrView.getLayoutParams();
            int tabbuttonheight = 50;
            if(_xml.pageHasAppearance(LOOK.TABBAR) && localLook.hasChild(LOOK.TABBAR_HEIGHT)){
                tabbuttonheight = localLook.getIntegerFromNode(LOOK.TABBAR_HEIGHT);
            }
            int heightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabbuttonheight, getResources().getDisplayMetrics());
            tabbarButtonParams.height = heightInPixels;

        } catch (Exception e) {
            MyLog.e("Exception in TabBarBox:setAppearance", e);
        }
    }

    public void setButtonText(String text){
        _buttonTextView.setText(text);
    }

    public ImageView getButtonImageView(){
        return _buttonImageView;
    }

    public void setButtonImage(Drawable image){
        _buttonImageView.setImageDrawable(image);
    }

    public void setPage(XmlNode page){
        this._page = page;
    }

    public void setParentActivity(FragmentActivity parent){
        _parentActivity = parent;
    }

    public View.OnClickListener buttonListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NavController.changePageWithXmlNode(_page, _parentActivity);
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception when setting up new page fragment from onClick", e);
                }
            }
        };
    }
}