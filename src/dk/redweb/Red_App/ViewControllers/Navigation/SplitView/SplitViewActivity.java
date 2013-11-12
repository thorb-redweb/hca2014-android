package dk.redweb.Red_App.ViewControllers.Navigation.SplitView;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.NavController;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewControllers.BaseActivity;
import dk.redweb.Red_App.ViewControllers.FrontPageComponents.*;
import dk.redweb.Red_App.XmlHandling.XmlNode;

import java.lang.reflect.Constructor;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:48 PM
 */
public class SplitViewActivity extends BaseActivity {
    private GestureDetector _gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splitview);

        fillHalfView(PAGE.TOP,R.id.frontpage_lnrTopBox);
        fillHalfView(PAGE.BOTTOM,R.id.frontpage_lnrBottomBox);
    }

    private void fillHalfView(String half, int boxId){
        try {
            XmlNode halfNode = _page.getChildFromNode(half);

            String type = halfNode.getStringFromNode(PAGE.COMPONENTTYPE);
            String className = NavController.getClassNameForTypeString(type);
            Class<?> viewClass = Class.forName(className);

            Constructor<?> constructor = viewClass.getConstructor(Activity.class, XmlNode.class);

            View view = (View)constructor.newInstance(this, halfNode);

            LinearLayout halfBox = (LinearLayout)findViewById(boxId);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            halfBox.addView(view, params);
            if(type.equals("NewsTicker")){
                NewsTicker newsTicker = (NewsTicker)view;
                _gestureDetector = new GestureDetector(this, newsTicker);
                newsTicker.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return _gestureDetector.onTouchEvent(motionEvent);
                    }
                });
                newsTicker.setClickable(true);
            }

        } catch (Exception e) {
            MyLog.e("Exception in FrontPageActivity:fillHalfView when getting and inflating halfview", e);
        }
    }
}
