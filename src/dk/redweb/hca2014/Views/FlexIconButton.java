package dk.redweb.hca2014.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import dk.redweb.hca2014.R;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 1/24/14
 * Time: 16:05
 */
public class FlexIconButton extends FlexibleButton {
    public FlexIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void inflateLayout(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_flexiconbutton, this);

        txtButton = (TextView)findViewById(R.id.flexibleButton_lblButton);
        imgButton = (ImageView)findViewById(R.id.flexibleButton_imgButton);
    }
}
