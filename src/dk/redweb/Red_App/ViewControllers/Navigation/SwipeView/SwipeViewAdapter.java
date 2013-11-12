package dk.redweb.Red_App.ViewControllers.Navigation.SwipeView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/17/13
 * Time: 1:36 PM
 */
public class SwipeViewAdapter extends FragmentPagerAdapter {
    XmlStore _xml;

    String[] _pages;

    public SwipeViewAdapter(String[] pages, XmlStore xml, FragmentManager fm) {
        super(fm);
        _pages = pages;
        _xml = xml;
    }

    //Change some stuff

    @Override
    public Fragment getItem(int i) {
        XmlNode nextPage = null;
        try {
            nextPage = _xml.getPage(_pages[i]);
        } catch (Exception e) {
            MyLog.e("Exception when getting next page for SwipeView", e);
        }
        Fragment newFragment = NavController.createPageFragmentFromPage(nextPage);
        return newFragment;
    }

    @Override
    public int getCount() {
        return _pages.length;
    }
}
