package dk.redweb.hca2014.ViewControllers.Article.ImageArticleList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.MyLog;
import dk.redweb.hca2014.Network.NetworkInterface;
import dk.redweb.hca2014.R;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;
import dk.redweb.hca2014.XmlHandling.XmlStore;

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
        TextView txtPublished = (TextView)rowView.findViewById(R.id.imageArticleListItem_lblPublished);
        ImageView imgView = (ImageView)rowView.findViewById(R.id.imageArticleListItem_img);

        ArticleVM article = _values[position];
        txtTitle.setText(article.Title());
        txtBody.setText(article.IntroTextWithoutHtml());
        txtPublished.setText(article.PublishDateWithPattern("dd. MMMM yyyy"));
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
            helper.TextView.setColor(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLECOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESIZE, LOOK.GLOBAL_ITEMTITLESIZE);
            helper.TextView.setStyle(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESTYLE, LOOK.GLOBAL_ITEMTITLESTYLE);
            helper.TextView.setShadow(txtTitle, LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_ITEMTITLESHADOWOFFSET, LOOK.GLOBAL_ITEMTITLESHADOWOFFSET);

            TextView txtBody = (TextView)lineitem.findViewById(R.id.imageArticleListItem_lblBody);
            helper.TextView.setColor(txtBody, LOOK.IMAGEARTICLELIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtBody, LOOK.IMAGEARTICLELIST_TEXTSIZE, LOOK.GLOBAL_TEXTSIZE);
            helper.TextView.setStyle(txtBody, LOOK.IMAGEARTICLELIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtBody, LOOK.IMAGEARTICLELIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

            TextView txtPublished = (TextView)lineitem.findViewById(R.id.imageArticleListItem_lblPublished);
            helper.TextView.setColor(txtPublished, LOOK.IMAGEARTICLELIST_TEXTCOLOR, LOOK.GLOBAL_BACKTEXTCOLOR);
            helper.TextView.setSize(txtPublished, LOOK.IMAGEARTICLELIST_SMALLTEXTSIZE, LOOK.GLOBAL_SMALLTEXTSIZE);
            helper.TextView.setStyle(txtPublished, LOOK.IMAGEARTICLELIST_TEXTSTYLE, LOOK.GLOBAL_TEXTSTYLE);
            helper.TextView.setShadow(txtPublished, LOOK.IMAGEARTICLELIST_TEXTSHADOWCOLOR, LOOK.GLOBAL_BACKTEXTSHADOWCOLOR,
                    LOOK.IMAGEARTICLELIST_TEXTSHADOWOFFSET, LOOK.GLOBAL_TEXTSHADOWOFFSET);

        } catch (Exception e) {
            MyLog.e("Exception in ImageArticleListAdapter:setAppearance", e);
        }
    }
}
