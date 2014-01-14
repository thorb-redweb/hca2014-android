package dk.redweb.Red_App.ViewControllers.Article.ImageArticleList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.Red_App.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.Red_App.MyLog;
import dk.redweb.Red_App.Network.NetworkInterface;
import dk.redweb.Red_App.R;
import dk.redweb.Red_App.StaticNames.LOOK;
import dk.redweb.Red_App.StaticNames.PAGE;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;
import dk.redweb.Red_App.XmlHandling.XmlStore;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/18/13
 * Time: 2:48 PM
 */
public class ImageArticleListAdapter extends ArrayAdapter<ArticleVM> {
    private XmlStore _xml;
    private XmlNode _parent;

    private final Context _context;
    private final ArticleVM[] _values;
    private final NetworkInterface _net;

    public ImageArticleListAdapter(Context context, NetworkInterface networkInterface, ArticleVM[] values, XmlStore xml, XmlNode parent){
        super(context, R.layout.listitem_imagearticlelist, values);
        _context = context;
        _values = values;
        _net = networkInterface;

        _xml = xml;
        _parent = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView = (LinearLayout)inflater.inflate(R.layout.listitem_imagearticlelist, parent, false);

        setAppearance(rowView);

        TextView txtTitle = (TextView)rowView.findViewById(R.id.imageArticleListItem_lblTitle);
        TextView txtBody = (TextView)rowView.findViewById(R.id.imageArticleListItem_lblBody);
        ImageView imgView = (ImageView)rowView.findViewById(R.id.imageArticleListItem_img);

        ArticleVM article = _values[position];
        txtTitle.setText(article.Title());
        txtBody.setText(article.IntroTextWithoutHtml());
        _net.fetchImageOnThread(article.IntroImagePath(), imgView);

        return rowView;
    }

    private void setAppearance(LinearLayout lineitem){
        try {
            XmlNode globalLook = _xml.getAppearanceForPage(LOOK.GLOBAL);
            XmlNode localLook = null;
            if(_xml.appearance.hasChild(_parent.getStringFromNode(PAGE.NAME)))
                localLook = _xml.getAppearanceForPage(_parent.getStringFromNode(PAGE.NAME));
            AppearanceHelper helper = new AppearanceHelper(_context, localLook, globalLook);

            helper.setViewBackgroundColor(lineitem, LOOK.IMAGEARTICLELIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);

            TextView txtTitle = (TextView)lineitem.findViewById(R.id.imageArticleListItem_lblTitle);
            helper.TextView.setTextColor(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setTextStyle(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setTextShadow(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtBody = (TextView)lineitem.findViewById(R.id.imageArticleListItem_lblBody);
            helper.TextView.setTextColor(txtBody, LOOK.IMAGEARTICLELIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setTextSize(txtBody, LOOK.IMAGEARTICLELIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setTextStyle(txtBody, LOOK.IMAGEARTICLELIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setTextShadow(txtBody, LOOK.IMAGEARTICLELIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in ImageArticleListAdapter:setAppearance", e);
        }
    }
}
