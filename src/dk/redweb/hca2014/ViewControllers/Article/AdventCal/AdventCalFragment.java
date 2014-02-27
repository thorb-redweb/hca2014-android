package dk.redweb.hca2014.ViewControllers.Article.AdventCal;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.EXTRA;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import org.joda.time.DateTime;

import java.util.Arrays;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/16/13
 * Time: 3:07 PM
 */
public class AdventCalFragment extends BasePageFragment {

    private ArticleVM[] _windows;
    private ImageView[] _buttons;

    public AdventCalFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_adventcal);

        int catid = -1;

        try {
            catid = _page.getIntegerFromNode(PAGE.CATID);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting catid from xml page for an AdventCal", e);
        }

        _windows =  _db.Articles.getVMListFromCatid(catid);
        Arrays.sort(_windows, new AdventComparator());

        setAppearance();

        ImageView imgButton1 = (ImageView)findViewById(R.id.adventcal_imgButton1);
        ImageView imgButton2 = (ImageView) findViewById(R.id.adventcal_imgButton2);
        ImageView imgButton3 = (ImageView) findViewById(R.id.adventcal_imgButton3);
        ImageView imgButton4 = (ImageView) findViewById(R.id.adventcal_imgButton4);
        ImageView imgButton5 = (ImageView) findViewById(R.id.adventcal_imgButton5);
        ImageView imgButton6 = (ImageView) findViewById(R.id.adventcal_imgButton6);
        ImageView imgButton7 = (ImageView) findViewById(R.id.adventcal_imgButton7);
        ImageView imgButton8 = (ImageView) findViewById(R.id.adventcal_imgButton8);
        ImageView imgButton9 = (ImageView) findViewById(R.id.adventcal_imgButton9);
        ImageView imgButton10 = (ImageView) findViewById(R.id.adventcal_imgButton10);
        ImageView imgButton11 = (ImageView) findViewById(R.id.adventcal_imgButton11);
        ImageView imgButton12 = (ImageView) findViewById(R.id.adventcal_imgButton12);
        ImageView imgButton13 = (ImageView) findViewById(R.id.adventcal_imgButton13);
        ImageView imgButton14 = (ImageView) findViewById(R.id.adventcal_imgButton14);
        ImageView imgButton15 = (ImageView) findViewById(R.id.adventcal_imgButton15);
        ImageView imgButton16 = (ImageView) findViewById(R.id.adventcal_imgButton16);
        ImageView imgButton17 = (ImageView) findViewById(R.id.adventcal_imgButton17);
        ImageView imgButton18 = (ImageView) findViewById(R.id.adventcal_imgButton18);
        ImageView imgButton19 = (ImageView) findViewById(R.id.adventcal_imgButton19);
        ImageView imgButton20 = (ImageView) findViewById(R.id.adventcal_imgButton20);
        ImageView imgButton21 = (ImageView) findViewById(R.id.adventcal_imgButton21);
        ImageView imgButton22 = (ImageView) findViewById(R.id.adventcal_imgButton22);
        ImageView imgButton23 = (ImageView) findViewById(R.id.adventcal_imgButton23);
        ImageView imgButton24 = (ImageView) findViewById(R.id.adventcal_imgButton24);
        _buttons = new ImageView[]{imgButton1,imgButton2,imgButton3,imgButton4,imgButton5,imgButton6,imgButton7,imgButton8,imgButton9,imgButton10,
                                        imgButton11,imgButton12,imgButton13,imgButton14,imgButton15,imgButton16,imgButton17,imgButton18,imgButton19,imgButton20,
                                        imgButton21,imgButton22,imgButton23,imgButton24};

        for (int i = 0; i < _windows.length;i++){
            final ArticleVM window = _windows[i];
            ImageView button = _buttons[i];

            _net.fetchImageOnThread(window.IntroImagePath(),button);
            button.setOnClickListener(getButtonOnClick(window));
        }
        return _view;
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < _windows.length;i++){
            final ArticleVM window = _windows[i];
            ImageView button = _buttons[i];

            button.setVisibility(View.VISIBLE);

            DateTime windowOpenDate = DateTime.parse(window.Title());
            if(DateTimeNow().isBefore(windowOpenDate)){
                button.setColorFilter(Color.rgb(180,180,180), PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    public void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook,_globallook);

            LinearLayout lnrBackground = (LinearLayout)findViewById(R.id.adventcal_lnrMainView);
            helper.setViewBackgroundTileImageOrColor(lnrBackground, LOOK.ADVENTCAL_BACKGROUNDIMAGE, LOOK.ADVENTCAL_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting appearance for AdventCal page", e);
        }
    }

    public View.OnClickListener getButtonOnClick(final ArticleVM window){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DateTime windowOpenDate = new AdventComparator().getOpenDateTime(window);
                    if(DateTimeNow().isAfter(windowOpenDate)){

                        XmlNode selectedPage = _xml.getPage(_childname);
                        XmlNode childPage = selectedPage.deepClone();
                        childPage.addChildToNode(EXTRA.ARTICLEID, String.valueOf(window.ArticleId()));

                        changePageTo(new AdventWindowFragment(childPage));
                    }
                } catch (Exception e) {
                    MyLog.e("Exception when executing onClick on an adventCal Imageview", e);
                }
            }
        };
    }
}