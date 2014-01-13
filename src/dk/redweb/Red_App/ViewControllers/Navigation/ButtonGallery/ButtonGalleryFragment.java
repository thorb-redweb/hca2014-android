package dk.redweb.Red_App.ViewControllers.Navigation.ButtonGallery;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.Helper.TextHelper.TextHelper;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.StaticNames.TEXT;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.Views.FlexibleButton;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/11/13
 * Time: 9:23 AM
 */
public class ButtonGalleryFragment extends BasePageFragment {

    FlexibleButton btn1;
    FlexibleButton btn2;
    FlexibleButton btn3;
    FlexibleButton btn4;
    FlexibleButton btn5;
    FlexibleButton btn6;
    FlexibleButton btn7;
    FlexibleButton btn8;

    public ButtonGalleryFragment(XmlNode page) {
        super(page);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_buttongallery);

        setupButtons();
        setAppearance();
        setText();

        return _view;
    }

    private void setupButtons(){
        try {
            btn1 = (FlexibleButton)findViewById(R.id.buttongallery_btn1);
            btn2 = (FlexibleButton)findViewById(R.id.buttongallery_btn2);
            btn3 = (FlexibleButton)findViewById(R.id.buttongallery_btn3);
            btn4 = (FlexibleButton)findViewById(R.id.buttongallery_btn4);
            btn5 = (FlexibleButton)findViewById(R.id.buttongallery_btn5);
            btn6 = (FlexibleButton)findViewById(R.id.buttongallery_btn6);
            btn7 = (FlexibleButton)findViewById(R.id.buttongallery_btn7);
            btn8 = (FlexibleButton)findViewById(R.id.buttongallery_btn8);

            if(_page.hasChild(PAGE.BUTTON1CHILD)){
                btn1.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON1CHILD));
                btn1.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON2CHILD)){
                btn2.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON2CHILD));
                btn2.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON3CHILD)){
                btn3.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON3CHILD));
                btn3.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON4CHILD)){
                btn4.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON4CHILD));
                btn4.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON5CHILD)){
                btn5.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON5CHILD));
                btn5.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON6CHILD)){
                btn6.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON6CHILD));
                btn6.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON7CHILD)){
                btn7.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON7CHILD));
                btn7.setOnClickListener(buttonListener(page));
            }
            if(_page.hasChild(PAGE.BUTTON8CHILD)){
                btn8.setVisibility(View.VISIBLE);
                XmlNode page = _xml.getPage(_page.getStringFromNode(PAGE.BUTTON8CHILD));
                btn8.setOnClickListener(buttonListener(page));
            }
        } catch (Exception e) {
            MyLog.e("Exception when attempting to set up button visibility and listeners", e);
        }
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = _appearanceHelper;

            View lnrBackground = findViewById(R.id.buttongallery_lnrBackground);
            helper.setViewBackgroundImageOrColor(lnrBackground, LOOK.BUTTONGALLERY_BACKGROUNDIMAGE, LOOK.BUTTONGALLERY_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            helper.FlexButton.setImage(btn1, LOOK.BUTTONGALLERY_BUTTON1IMAGE);
            helper.FlexButton.setImage(btn2, LOOK.BUTTONGALLERY_BUTTON2IMAGE);
            helper.FlexButton.setImage(btn3, LOOK.BUTTONGALLERY_BUTTON3IMAGE);
            helper.FlexButton.setImage(btn4, LOOK.BUTTONGALLERY_BUTTON4IMAGE);
            helper.FlexButton.setImage(btn5, LOOK.BUTTONGALLERY_BUTTON5IMAGE);
            helper.FlexButton.setImage(btn6, LOOK.BUTTONGALLERY_BUTTON6IMAGE);
            helper.FlexButton.setImage(btn7, LOOK.BUTTONGALLERY_BUTTON7IMAGE);
            helper.FlexButton.setImage(btn8, LOOK.BUTTONGALLERY_BUTTON8IMAGE);


            FlexibleButton[] flexButtons = {btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8};
            helper.setViewBackgroundImageOrColor(flexButtons, LOOK.BUTTONGALLERY_BUTTONBACKIMAGE, LOOK.BUTTONGALLERY_BUTTONCOLOR,LOOK.GLOBAL_ALTCOLOR);
            helper.FlexButton.setTextColor(flexButtons, LOOK.BUTTONGALLERY_BUTTONTEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.FlexButton.setTextSize(flexButtons, LOOK.BUTTONGALLERY_BUTTONTEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.FlexButton.setTextStyle(flexButtons, LOOK.BUTTONGALLERY_BUTTONTEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.FlexButton.setTextShadow(flexButtons, LOOK.BUTTONGALLERY_BUTTONTEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.BUTTONGALLERY_BUTTONTEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            if(_locallook != null && _locallook.hasChild(LOOK.BUTTONGALLERY_BUTTONCORNERRADIUS)){
                for(FlexibleButton flexButton : flexButtons){
                    flexButton.setBackground(makeRoundedBackground(_locallook,_globallook));
                }
            }

        } catch (Exception e) {
            MyLog.e("Exception when setting appearance", e);
        }
    }

    private Drawable makeRoundedBackground(XmlNode localLook, XmlNode globalLook) throws NoSuchFieldException {
        float radius = localLook.getFloatFromNode(LOOK.BUTTONGALLERY_BUTTONCORNERRADIUS);
        int color = Color.parseColor(localLook.getStringFromNode(LOOK.BUTTONGALLERY_BUTTONCOLOR));
        float[] radii = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
//        float[] topHalfOuterRadii = new float[] {radius, radius, radius, radius, 0, 0, 0, 0};
//        float[] bottomHalfOuterRadii = new float[] {0, 0, 0, 0, radius, radius, radius, radius};
//        int topColor = Color.BLUE;
//        int bottomColor = Color.CYAN;
//        int cellHeight = 40;

//        RoundRectShape top_round_rect = new RoundRectShape(topHalfOuterRadii, null, null);
//        ShapeDrawable top_shape_drawable = new ShapeDrawable(top_round_rect);
//        top_shape_drawable.getPaint().setColor(topColor);
//
//        RoundRectShape bottom_round_rect = new RoundRectShape(bottomHalfOuterRadii, null, null);
//        ShapeDrawable bottom_shape_drawable = new ShapeDrawable(bottom_round_rect);
//        bottom_shape_drawable.getPaint().setColor(bottomColor);
//
//        Drawable[] drawarray = {top_shape_drawable, bottom_shape_drawable};
//        LayerDrawable layerDrawable = new LayerDrawable(drawarray);
//
//        int halfOfCellHeight = cellHeight/2;
//        layerDrawable.setLayerInset(0,0,0,0,halfOfCellHeight);
//        layerDrawable.setLayerInset(1,0,halfOfCellHeight,0,0);

        RoundRectShape roundRectShape = new RoundRectShape(radii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shapeDrawable});

        return layerDrawable;
    }

    private void setText(){
        try {
            TextHelper helper = _textHelper;

            helper.tryFlexibleButtonText(R.id.buttongallery_btn1, TEXT.BUTTONGALLERY_BUTTON1);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn2, TEXT.BUTTONGALLERY_BUTTON2);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn3, TEXT.BUTTONGALLERY_BUTTON3);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn4, TEXT.BUTTONGALLERY_BUTTON4);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn5, TEXT.BUTTONGALLERY_BUTTON5);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn6, TEXT.BUTTONGALLERY_BUTTON6);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn7, TEXT.BUTTONGALLERY_BUTTON7);
            helper.tryFlexibleButtonText(R.id.buttongallery_btn8, TEXT.BUTTONGALLERY_BUTTON8);
        } catch (Exception e) {
            MyLog.e("Exception when setting text", e);
        }
    }

    public View.OnClickListener buttonListener(final XmlNode targetPage){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NavController.changePageWithXmlNode(targetPage, getActivity());
                } catch (NoSuchFieldException e) {
                    MyLog.e("Exception in onClick when attempting to change page in ButtonGalleryFragment", e);
                }
            }
        };
    }
}