package dk.redweb.Red_App;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TYPE;
import dk.redweb.Red_App.ViewControllers.Article.AdventCal.AdventCalFragment;
import dk.redweb.Red_App.ViewControllers.Article.AdventCal.AdventWindowFragment;
import dk.redweb.Red_App.ViewControllers.Article.ArticleDetail.ArticleDetailFragment;
import dk.redweb.Red_App.ViewControllers.Article.ImageArticleList.ImageArticleListFragment;
import dk.redweb.Red_App.ViewControllers.Article.StaticArticle.StaticArticleFragment;
import dk.redweb.Red_App.ViewControllers.Contest.BikeTracking.BikeTrackingFragment;
import dk.redweb.Red_App.ViewControllers.Contest.StairTracking.StairTrackingFragment;
import dk.redweb.Red_App.ViewControllers.Map.OverviewMap.OverviewMapFragment;
import dk.redweb.Red_App.ViewControllers.Map.SessionMap.SessionMapFragment;
import dk.redweb.Red_App.ViewControllers.Map.VenueMap.VenueMapFragment;
import dk.redweb.Red_App.ViewControllers.Misc.CameraIntent.CameraIntentFragment;
import dk.redweb.Red_App.ViewControllers.Misc.ImageUploader.ImageUploaderFileBrowserFragment;
import dk.redweb.Red_App.ViewControllers.Misc.ImageUploader.ImageUploaderFragment;
import dk.redweb.Red_App.ViewControllers.Misc.WebView.WebViewFragment;
import dk.redweb.Red_App.ViewControllers.Navigation.ButtonGallery.ButtonGalleryFragment;
import dk.redweb.Red_App.ViewControllers.Navigation.SwipeView.SwipeViewFragment;
import dk.redweb.Red_App.ViewControllers.Navigation.TableNavigator.TableNavigatorFragment;
import dk.redweb.Red_App.ViewControllers.PushMessages.PushMessageDetail.PushMessageDetailFragment;
import dk.redweb.Red_App.ViewControllers.PushMessages.PushMessageList.PushMessageListFragment;
import dk.redweb.Red_App.ViewControllers.Session.DailySessionList.DailySessionListFragment;
import dk.redweb.Red_App.ViewControllers.Session.SessionDetail.SessionDetailFragment;
import dk.redweb.Red_App.ViewControllers.Settings.PushMessageGroupSettings.PushMessageGroupSettingsFragment;
import dk.redweb.Red_App.ViewControllers.Settings.PushMessageSubscriber.PushMessageSubscriberFragment;
import dk.redweb.Red_App.ViewControllers.Settings.PushMessageUnsubscriber.PushMessageUnsubscriberFragment;
import dk.redweb.Red_App.ViewControllers.System.PushMessageAutoSubscriber.PushMessageAutoSubscriberFragment;
import dk.redweb.Red_App.ViewControllers.Venue.VenueDetail.VenueDetailFragment;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

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

        String type = page.getStringFromNode(PAGE.TYPE);
        if(parentIsSwipeview){ //Transition where the view is put inside a swipeview
            try{
                XmlNode swipeViewPage = xml.getPage(page.getStringFromNode(PAGE.PARENT));
                SwipeViewFragment swipefragment = (SwipeViewFragment)createPageFragmentFromPage(swipeViewPage);

                changePageWithFragment(swipefragment, activity, addToBackStack);
                swipefragment.setFirstLeaf(page.getStringFromNode(PAGE.NAME));
            } catch (Exception e) {
                MyLog.e("Exception when setting up SwipeView fragment, instead of child of SwipeView Fragment", e);
            }
        }
        else if(type.equals(TYPE.PUSHMESSAGEAUTOSUBSCRIBER)){ //Transition without being placed in backstack
            Fragment fragment = createPageFragmentFromPage(page);
            changePageWithFragment(fragment, activity, false);
        }
        else { //Standard Transition
            Fragment fragment = createPageFragmentFromPage(page);
            changePageWithFragment(fragment, activity, addToBackStack);
        }

        //Sometimes a childview will be called directly, but we still want the parent view before it in the backstack
        //(for example when a view is opened from a notification). The childview is then put as a childpage of the
        //parent page. The parent page is instantiated above, and the childview is instantiated below.
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

    public static void popPage(FragmentActivity parentActivity){
        FragmentManager manager = parentActivity.getSupportFragmentManager();
        manager.popBackStack();
    }

    public static Fragment createPageFragmentFromPage(XmlNode page){
        try {
            String type = page.getStringFromNode(PAGE.TYPE);
            if(type.equals(TYPE.ADVENTCAL)){
                return new AdventCalFragment(page);
            } else if(type.equals(TYPE.ADVENTWINDOW)){
                return new AdventWindowFragment(page);
            } else if(type.equals(TYPE.ARTICLEDETAIL)){
                return new ArticleDetailFragment(page);
            } else if(type.equals(TYPE.BIKETRACKING)){
                return new BikeTrackingFragment(page);
            } else if(type.equals(TYPE.BUTTONGALLERY)){
                return new ButtonGalleryFragment(page);
            } else if(type.equals(TYPE.CAMERAINTENT)){
                return new CameraIntentFragment(page);
            } else if(type.equals(TYPE.DAILYSESSIONLIST)){
                return new DailySessionListFragment(page);
            } else if (type.equals(TYPE.FILEBROWSER)){
                return new ImageUploaderFileBrowserFragment(page);
            } else if (type.equals(TYPE.IMAGEARTICLELIST)){
                return new ImageArticleListFragment(page);
            } else if (type.equals(TYPE.IMAGEUPLOADER)){
                return new ImageUploaderFragment(page);
            } else if(type.equals(TYPE.NEWSTICKER)){
                throw new NotImplementedException("Newsticker has not been updated to the modern framework");
            } else if(type.equals(TYPE.OVERVIEWMAP)){
                return new OverviewMapFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGEAUTOSUBSCRIBER)){
                return new PushMessageAutoSubscriberFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGEDETAIL)){
                return new PushMessageDetailFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGEGROUPSETTINGS)){
                return new PushMessageGroupSettingsFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGESUBSCRIBER)){
                return new PushMessageSubscriberFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGELIST)){
                return new PushMessageListFragment(page);
            } else if (type.equals(TYPE.PUSHMESSAGEUNSUBSCRIBER)){
                return new PushMessageUnsubscriberFragment(page);
            } else if(type.equals(TYPE.SESSIONDETAIL)){
                return new SessionDetailFragment(page);
            } else if(type.equals(TYPE.SESSIONMAP)){
                return new SessionMapFragment(page);
            } else if (type.equals(TYPE.SPLITVIEW)){
                throw new NotImplementedException("SplitView has not been updated to the modern framework");
            } else if(type.equals(TYPE.STAIRTRACKING)){
                return new StairTrackingFragment(page);
            } else if(type.equals(TYPE.STATICARTICLE)){
                return new StaticArticleFragment(page);
            } else if(type.equals(TYPE.SWIPEVIEW)){
                return new SwipeViewFragment(page);
            } else if(type.equals(TYPE.TABLENAVIGATOR)){
                return new TableNavigatorFragment(page);
            } else if(type.equals(TYPE.UPCOMINGSESSIONS)){
                throw new NotImplementedException("UpcomingSessions has not been updated to the modern framework");
            } else if(type.equals(TYPE.VENUEDETAIL)){
                return new VenueDetailFragment(page);
            } else if(type.equals(TYPE.VENUEMAP)){
                return new VenueMapFragment(page);
            } else if(type.equals(TYPE.WEBVIEW)){
                return new WebViewFragment(page);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("Exception when getting ViewController from page type", e);
        }
        throw new IllegalArgumentException("A page of the given type does not exist or is not implemented");
    }

    public static String getClassNameForTypeString(String type){

        return null;
    }
}
