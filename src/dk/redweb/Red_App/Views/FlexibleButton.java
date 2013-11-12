package dk.redweb.Red_App.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dk.redweb.Red_App.R;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 10/11/13
 * Time: 11:22 AM
 */
public class FlexibleButton extends RelativeLayout{

    TextView txtButton;
    ImageView imgButton;

    public FlexibleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //LayoutInflater inflater = ;
        LayoutInflater.from(context).inflate(R.layout.view_flexiblebutton, this);

        txtButton = (TextView)findViewById(R.id.flexibleButton_lblButton);
        imgButton = (ImageView)findViewById(R.id.flexibleButton_imgButton);
    }

//    public void setBackground(Drawable background){
//        rltButton.setBackground(background);
//    }
//
//    public void setBackgroundColor(int color){
//        rltButton.setBackgroundColor(color);
//    }

    public void setImageDrawable(Drawable image){
        imgButton.setImageDrawable(image);
        imgButton.setVisibility(VISIBLE);
    }

    public void setImageVisibility(int visibility){
        imgButton.setVisibility(visibility);
    }

    public void setText(String text){
        txtButton.setText(text);
        txtButton.setVisibility(VISIBLE);
    }

    public void setTextColor(int color){
        txtButton.setTextColor(color);
    }

    public void setTextSize(int unit, float size){
        txtButton.setTextSize(unit, size);
    }

    public void setTypeface(Typeface tf, int style){
        txtButton.setTypeface(tf, style);
    }

    public void setTextShadowLayer(float radius, float dx, float dy, int color){
        txtButton.setShadowLayer(radius,dx,dy,color);
    }
}
