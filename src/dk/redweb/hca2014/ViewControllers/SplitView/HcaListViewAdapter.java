package dk.redweb.hca2014.ViewControllers.SplitView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.ViewModels.SessionVM;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 2/25/14
 * Time: 13:23
 */
public class HcaListViewAdapter extends ArrayAdapter<SessionVM> {
    private final Context context;
    public final SessionVM[] sessions;

    public HcaListViewAdapter(Context context, SessionVM[] values){
        super(context, R.layout.listitem_hcasplitview, values);
        this.context = context;
        this.sessions = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_hcasplitview, parent, false);
        TextView txtTimeAndEvent = (TextView)rowView.findViewById(R.id.hcalistview_txtTitle);
        ImageView imgType = (ImageView)rowView.findViewById(R.id.hcalistview_imgType);
        RelativeLayout rightBorder = (RelativeLayout)rowView.findViewById(R.id.hcalistview_rightborder);

        SessionVM session = sessions[position];
        String time = session.StartTimeAsString() + ": " + session.Title();
        txtTimeAndEvent.setText(time);
        imgType.setImageResource(session.TypeImage());
        rightBorder.setBackgroundColor(session.TypeColor());

        return rowView;
    }
}
