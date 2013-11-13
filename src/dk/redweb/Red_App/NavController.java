package dk.redweb.Red_App;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TYPE;
import dk.redweb.Red_App.ViewControllers.Navigation.SwipeView.SwipeViewFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

import java.lang.reflect.Constructor;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 3:09 PM
 */
public class NavController {

    public static void changePageWithXmlNode(XmlNode page, FragmentActivity activity) throws NoSuchFieldException {
        changePageWithXmlNode(page, activity, true);
    }

    public static void changePageWithXmlNode(XmlNode page, FragmentActivity activity, boolean addToBackStack) throws NoSuchFieldException {

        RedEventApplication app = (RedEventApplication)activity.getApplication();
        XmlStore xml = app.getXmlStore();

        boolean parentIsSwipeview = false;
        if(page.hasChild(PAGE.PARENT)){
            try {
                String parentName = page.getStringFromNode(PAGE.PARENT);
                XmlNode parentPage =  xml.getPage(parentName);
                String parentType = parentPage.getStringFromNode(PAGE.TYPE);
                parentIsSwipeview = parentType.equals(TYPE.SWIPEVIEW);
            } catch (Exception e) {
                MyLog.e("Exception when determining whether parentPage is SwipeView", e);
            }
        }

        if(parentIsSwipeview){
            try{
                XmlNode swipeViewPage = xml.getPage(page.getStringFromNode(PAGE.PARENT));
                SwipeViewFragment swipefragment = (SwipeViewFragment)createPageFragmentFromPage(swipeViewPage);

                changePageWithFragment(swipefragment, activity, addToBackStack);
                swipefragment.setFirstLeaf(page.getStringFromNode(PAGE.NAME));
            } catch (Exception e) {
                MyLog.e("Exception when setting up SwipeView fragment, instead of child of SwipeView Fragment", e);
            }
        }
        else{
            Fragment fragment = createPageFragmentFromPage(page);
            changePageWithFragment(fragment, activity, addToBackStack);
        }

        if(page.hasChild(PAGE.CHILDPAGE)){
            XmlNode childPage = page.getChildFromNode(PAGE.CHILDPAGE);
            changePageWithXmlNode(childPage, activity, true);
        }
    }

    public static void changePageWithFragment(Fragment pageFragment, FragmentActivity activity, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        doThePageChange(pageFragment,fragmentTransaction,R.id.frag_container,addToBackStack);
    }

    public static void changeChildPageWithFragment(Fragment pageFragment, Fragment parentFragment, int fragmentcontainerId, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = parentFragment.getChildFragmentManager().beginTransaction();
        doThePageChange(pageFragment,fragmentTransaction, fragmentcontainerId, addToBackStack);
    }

    private static void doThePageChange(Fragment fragment, FragmentTransaction fragmentTransaction, int fragmentContainerId, boolean addToBackStack){
        fragmentTransaction.replace(fragmentContainerId, fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    public static Fragment createPageFragmentFromPage(XmlNode page){
        String classname = null;
        try {
            classname = getClassNameForTypeString(page.getStringFromNode("type"));
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting type to inflate into fragment page", e);
        }
        assert classname != null;
        Class cl = null;
        try {
            cl = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            MyLog.e("Exception when getting class from name to inflate into fragment page", e);
        }
        assert cl != null;
        Constructor constructor = null;
        try {
            constructor = cl.getConstructor(new Class[] {XmlNode.class});
        } catch (NoSuchMethodException e) {
            MyLog.e("Exception when creating constructor for fragment page", e);
        }
        assert constructor != null;
        Fragment pageFragment = null;
        try {
            pageFragment = (Fragment)constructor.newInstance(page);
        } catch (Exception e) {
            MyLog.e("Exception when creating fragment page", e);
        }
        return pageFragment;
    }

    public static String getClassNameForTypeString(String type){
        String rootString = "dk.redweb.Red_App.";
        if(type.equals(TYPE.ADVENTCAL)){
            return rootString + "ViewControllers.Article.AdventCal.AdventCalFragment";
        } else if(type.equals(TYPE.ADVENTWINDOW)){
            return rootString + "ViewControllers.Article.AdeventWindow.AdventWindowFragment";
        } else if(type.equals(TYPE.ARTICLEDETAIL)){
            return rootString + "ViewControllers.Article.ArticleDetail.ArticleDetailFragment";
        } else if(type.equals(TYPE.BUTTONGALLERY)){
            return rootString + "ViewControllers.Navigation.ButtonGallery.ButtonGalleryActivity";
        } else if(type.equals(TYPE.DAILYSESSIONLIST)){
            return rootString + "ViewControllers.Session.DailySessionList.DailySessionListFragment";
        } else if (type.equals(TYPE.IMAGEARTICLELIST)){
            return rootString + "ViewControllers.Article.ImageArticleList.ImageArticleListFragment";
        } else if(type.equals(TYPE.NEWSTICKER)){
            return rootString + "ViewControllers.FrontPageComponents.NewsTicker";
        } else if(type.equals(TYPE.OVERVIEWMAP)){
            return rootString + "ViewControllers.Map.OverviewMap.OverviewMapActivity";
        } else if (type.equals(TYPE.PUSHMESSAGEDETAIL)){
            return rootString + "ViewControllers.PushMessages.PushMessageDetail.PushMessageDetailFragment";
        } else if (type.equals(TYPE.PUSHMESSAGEGROUPSETTINGS)){
            return rootString + "ViewControllers.Settings.PushMessageGroupSettings.PushMessageGroupSettingsFragment";
        } else if (type.equals(TYPE.PUSHMESSAGEINITIALIZER)){
            return rootString + "ViewControllers.Settings.PushMessageInitializer.PushMessageInitializerFragment";
        } else if (type.equals(TYPE.PUSHMESSAGELIST)){
            return rootString + "ViewControllers.PushMessages.PushMessageList.PushMessageListFragment";
        } else if(type.equals(TYPE.SESSIONDETAIL)){
            return rootString + "ViewControllers.Session.SessionDetail.SessionDetailFragment";
        } else if(type.equals(TYPE.SESSIONMAP)){
            return rootString + "ViewControllers.Map.SessionMap.SessionMapFragment";
        } else if (type.equals(TYPE.SPLITVIEW)){
            return rootString + "ViewControllers.Navigation.SplitView.SplitViewActivity";
        } else if(type.equals(TYPE.STATICARTICLE)){
            return rootString + "ViewControllers.Article.StaticArticle.StaticArticleFragment";
        } else if(type.equals(TYPE.SWIPEVIEW)){
            return rootString + "ViewControllers.Navigation.SwipeView.SwipeViewFragment";
        } else if(type.equals(TYPE.UPCOMINGSESSIONS)){
            return rootString + "ViewControllers.FrontPageComponents.UpcomingSessions";
        } else if(type.equals(TYPE.VENUEDETAIL)){
            return rootString + "ViewControllers.Venue.VenueDetail.VenueDetailFragment";
        } else if(type.equals(TYPE.VENUEMAP)){
            return rootString + "ViewControllers.Map.VenueMap.VenueMapFragment";
        }
        return null;
    }
}
