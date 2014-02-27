package dk.redweb.hca2014.XmlHandling;

import android.content.Context;
import dk.redweb.hca2014.StaticNames.LOOK;
import dk.redweb.hca2014.StaticNames.PAGE;
import dk.redweb.hca2014.StaticNames.TEXT;

import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:31 PM                                xml
 */
public class XmlStore {
    XmlImporter xmlImporter;

    public XmlNode appearance;
    public XmlNode pages;
    public XmlNode text;

    public String appDataPath;
    public String joomlaPath;
    public String css;
    public String projectId;

    public XmlStore(Context context) throws NoSuchFieldException {
        xmlImporter = new XmlImporter(context);

        appearance = xmlImporter.getNodesFromResource(LOOK.APPEARANCEFILENAME);
        pages = xmlImporter.getNodesFromResource(PAGE.PAGESFILENAME);
        text = xmlImporter.getNodesFromResource(TEXT.TEXTFILENAME);

        XmlNode joomlaSite = xmlImporter.getNodesFromResource("joomlasite.xml");
        appDataPath = joomlaSite.getChildFromNode("datafolder").getStringFromNode("path");
        joomlaPath = joomlaSite.getChildFromNode("joomla").getStringFromNode("path");
        css = joomlaSite.getChildFromNode("joomla").getStringFromNode("css");
        projectId = joomlaSite.getChildFromNode("project").getStringFromNode("id");
    }

    public XmlNode getPage(String name) throws Exception {
        for (XmlNode page : pages){
            if(page.hasChild(PAGE.NAME) && page.getStringFromNode(PAGE.NAME).equals(name)){
                return page;
            }
        }
        for (XmlNode page : pages){
            if(page.hasChild(PAGE.TYPE) && page.getStringFromNode(PAGE.TYPE).equals(name)){
                return page;
            }
        }
        throw new Exception("Page " + name + " not found in pages.");
    }

    public XmlNode getOfficialParentOf(XmlNode child) throws Exception {
        if(!child.hasChild(PAGE.PARENT))
            return getFrontPage();

        String parentName = child.getStringFromNode(PAGE.PARENT);
        return getPage(parentName);
    }

    public XmlNode getFrontPage() throws Exception {
        for (XmlNode page : pages){
            if(page.hasChild(PAGE.FRONTPAGE) && page.getBoolFromNode(PAGE.FRONTPAGE)){
                return page;
            }
        }
        throw new Exception("No page tagged as frontpage found.");
    }

    public XmlNode getAppearanceForPage(String name) throws Exception {
        for (XmlNode item : appearance){
            if(item.name().equals(name)){
                return item;
            }
        }
        throw new Exception("Appearance for page " + name + " not found in file.");
    }

    public boolean pageHasAppearance(String name){
        return appearance.hasChild(name);
    }

    public boolean pageHasAppearance(XmlNode page) throws NoSuchFieldException {
        return appearance.hasChild(page.getStringFromNode(PAGE.NAME));
    }

    public XmlNode getTextForPage(String name) throws Exception {
        for (XmlNode item : text){
            if(item.name().equals(name)){
                return item;
            }
        }
        throw new Exception("Text for page " + name + " not found in textfile.");
    }

    public boolean pageHasText(String name){
        return text.hasChild(name);
    }

    public XmlNode getEmptyNode(){
        return new XmlNode("Empty Node",new ArrayList<XmlNode>());
    }
}
