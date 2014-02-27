package dk.redweb.hca2014.ViewControllers.Navigation.SwipeView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.viewpagerindicator.LinePageIndicator;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 1:25 PM
 */
public class SwipeViewFragment extends BasePageFragment {
    SwipeViewAdapter _adapter;
    ViewPager _viewPager;
    String firstLeaf;

    public int firstIndex(){
        if (firstLeaf != null){
            int index = 0;
            while(index < _adapter._pages.length){
                if(_adapter._pages[index].equals(firstLeaf)){
                    return index;
                }
                index++;
            }
        }
        return 0;
    }

    public SwipeViewFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_swipeview);

        XmlNode[] leafNodes = _page.getAllChildNodesWithName(PAGE.SECTION);

        String[] leafNames = new String[leafNodes.length];
        for (int i = 0; i < leafNodes.length; i++){
            leafNames[i] = (String)leafNodes[i].value();
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        _adapter = new SwipeViewAdapter(leafNames, _xml, fragmentManager);
        _viewPager = (ViewPager)findViewById(R.id.swipeview_vpViewPager);
        _viewPager.setAdapter(_adapter);

        setCurrentLeaf(firstLeaf);

        LinePageIndicator pageIndicator = (LinePageIndicator)findViewById(R.id.swipeview_tpiTitlePageIndicator);
        pageIndicator.setViewPager(_viewPager);
        pageIndicator.setCurrentItem(firstIndex());

        setAppearance();

        return _view;
    }

    private void setAppearance(){
        try{
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(),_locallook,_globallook);

            RelativeLayout rltBackground = (RelativeLayout)findViewById(R.id.swipeview_rltMainView);
            helper.setViewBackgroundTileImageOrColor(rltBackground, LOOK.SWIPEVIEW_BACKGROUNDIMAGE, LOOK.SWIPEVIEW_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            //LinePageIndicator Appearance Code
            LinePageIndicator pageIndicator = (LinePageIndicator)findViewById(R.id.swipeview_tpiTitlePageIndicator);
            final float density = getResources().getDisplayMetrics().density;

            if (_locallook != null && _locallook.hasChild(LOOK.SWIPEVIEW_SELECTEDPAGECOLOR)){
                pageIndicator.setSelectedColor(Color.parseColor(_locallook.getStringFromNode(LOOK.SWIPEVIEW_SELECTEDPAGECOLOR)));
            }

            if (_locallook != null && _locallook.hasChild(LOOK.SWIPEVIEW_UNSELECTEDPAGECOLOR)){
                pageIndicator.setUnselectedColor(Color.parseColor(_locallook.getStringFromNode(LOOK.SWIPEVIEW_UNSELECTEDPAGECOLOR)));
            }

            pageIndicator.setGapWidth(15 * density);
            pageIndicator.setStrokeWidth(3 * density);
            pageIndicator.setLineWidth(20 * density);

        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when setting appearance on SwipeView", e);
        }
    }

    public void setFirstLeaf(String name){
        firstLeaf = name;
    }

    public void setCurrentLeaf(String name){
        _viewPager.setCurrentItem(firstIndex());
    }
}