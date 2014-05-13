package dk.redweb.hca2014.ViewControllers.Article.ImageArticleList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.hca2014.*;
import dk.redweb.hca2014.Helper.AppearanceHelper.AppearanceHelper;
import dk.redweb.hca2014.Helper.TextHelper.TextHelper;
import dk.redweb.hca2014.StaticNames.*;
import dk.redweb.hca2014.ViewControllers.BasePageFragment;
import dk.redweb.hca2014.ViewModels.ArticleVM;
import dk.redweb.hca2014.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 3:13 PM
 */
public class ImageArticleListFragment extends BasePageFragment {

    ListView lstArticles;

    public ImageArticleListFragment(){
        super(null);
    }

    public ImageArticleListFragment(XmlNode page) {
        super(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        My.saveXmlPageInBundle(_page, outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(_page == null){
            _page = My.loadXmlPageFromBundle(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.page_imagearticlelist);
        setAppearance();
        setText();

        lstArticles = (ListView)findViewById(R.id.imageArticleList_lstArticles);

        setupNewsList();
        reloadListView();

        return _view;
    }

    private void setAppearance(){
        try {
            AppearanceHelper helper = new AppearanceHelper(_view.getContext(), _locallook, _globallook);

            FrameLayout mainViewLayout = (FrameLayout)findViewById(R.id.imageArticleList_sessionlistlayout);
            helper.setViewBackgroundTileImageOrColor(mainViewLayout, LOOK.IMAGEARTICLELIST_BACKGROUNDIMAGE, LOOK.IMAGEARTICLELIST_BACKGROUNDCOLOR, LOOK.GLOBAL_BACKCOLOR);
        } catch (Exception e) {
            MyLog.e("Exception in ArticleDetailActivity:setAppearance", e);
        }
    }

    private void setText(){
        try {
            TextHelper helper = new TextHelper(_view, _name,_xml);
            helper.setText(R.id.imageArticleList_lblEmptyList, TEXT.IMAGEARTICLELIST_EMPTYLIST, DEFAULTTEXT.IMAGEARTICLELIST_EMPTYLIST);
        } catch (Exception e) {
            MyLog.e("Exception when setting static text", e);
        }
    }

    private void setupNewsList(){
        lstArticles.setEmptyView(findViewById(R.id.imageArticleList_lnrEmptyList));

        lstArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter adapter = lstArticles.getAdapter();

                ArticleVM selectedArticle = (ArticleVM) adapter.getItem(position);
                try {
                    XmlNode selectedPage = _xml.getPage(_childname);
                    XmlNode childPage = selectedPage.deepClone();
                    childPage.addChildToNode(EXTRA.ARTICLEID, String.valueOf(selectedArticle.ArticleId()));

                    android.support.v4.app.Fragment pageFragment = NavController.createPageFragmentFromPage(childPage);
                    changePageTo(pageFragment);
                } catch (Exception e) {
                    MyLog.e("Executing ImageArticleListActivity:onClickListener", e);
                }
            }
        });
    }

    private void reloadListView()
    {
        ArticleVM[] articles = new ArticleVM[0];
        try {
            if(_page.hasChild(PAGE.CATID)){
                articles = _db.Articles.getPublishedVMListFromCatid(_page.getIntegerFromNode(PAGE.CATID));
            }
            else{
                int[] catids = _page.getIntegerArrayFromNode(PAGE.CATIDS);
                articles = _db.Articles.getPublishedVMListFromCatids(catids);
            }
        } catch (NoSuchFieldException e) {
            MyLog.e("NoSuchFieldException for 'catid' in actImageArticleList:reloadListView", e);
        }
        ImageArticleListAdapter lstArticlesAdapter = new ImageArticleListAdapter(_view.getContext(), _net, articles, _xml, _page);
        lstArticles.setAdapter(lstArticlesAdapter);
    }

}
