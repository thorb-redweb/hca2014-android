package dk.redweb.Red_App.ViewControllers.Article.ImageArticleList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dk.redweb.Red_App.*;
import dk.redweb.Red_App.StaticNames.*;
import dk.redweb.Red_App.ViewControllers.BasePageFragment;
import dk.redweb.Red_App.ViewModels.ArticleVM;
import dk.redweb.Red_App.XmlHandling.XmlNode;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/17/13
 * Time: 3:13 PM
 */
public class ImageArticleListFragment extends BasePageFragment {

    ListView lstArticles;

    public ImageArticleListFragment(XmlNode page) {
        super(page);
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
            articles = _db.Articles.getPublishedVMListFromCatid(_page.getIntegerFromNode(PAGE.CATID));
        } catch (NoSuchFieldException e) {
            Log.e("RedEvent", "NoSuchFieldException for 'catid' in actImageArticleList:reloadListView", e);
        }
        ImageArticleListAdapter lstArticlesAdapter = new ImageArticleListAdapter(_view.getContext(), _net, articles, _xml, _page);
        lstArticles.setAdapter(lstArticlesAdapter);
    }

}
