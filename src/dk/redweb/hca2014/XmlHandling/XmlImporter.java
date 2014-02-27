package dk.redweb.hca2014.XmlHandling;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:29 PM
 */
public class XmlImporter {
    private Resources _resources;
    private String _packageName;

    public XmlImporter(Context context){
        _resources = context.getResources();
        _packageName = context.getPackageName();
    }

    public XmlNode getNodesFromResource(String filename){
        XmlNode rootNode = null;

        try {
            String[] partArray = filename.split("\\.");
            String resourceName = partArray[0];

            int resourceId = _resources.getIdentifier(resourceName, "xml", _packageName);
            XmlResourceParser parser = _resources.getXml(resourceId);

            rootNode = convertParserToNodeTree(parser);

        } catch (XmlPullParserException e) {
            Log.e("RedEvent","XmlPullParserException in XmlImporter:getNodesFromResource", e);
        } catch (IOException e) {
            Log.e("RedEvent","IOException in XmlImporter:getNodesFromResource", e);
        }

        return rootNode;
    }

    private XmlNode convertParserToNodeTree(XmlResourceParser parser) throws XmlPullParserException, IOException {
        XmlNode rootNode = null;

        int eventType = parser.getEventType();
        String currentTag = null;

        while (true) {
            if(eventType == XmlResourceParser.START_TAG) {
                if(currentTag == null){
                    currentTag = parser.getName();
                } else {
                    rootNode = new XmlNode(currentTag, convertChildToNodeTree(parser, currentTag));
                    currentTag = null;
                }
            } else if (eventType == XmlResourceParser.END_DOCUMENT){
                return rootNode;
            }
            eventType = parser.next();
        }
    }

    private ArrayList<XmlNode> convertChildToNodeTree(XmlResourceParser parser, String parentName) throws IOException, XmlPullParserException {

        ArrayList<XmlNode> nodeArray = new ArrayList<XmlNode>();
        int eventType = parser.getEventType();
        String currentTag = null;
        String endTag;

        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if(eventType == XmlResourceParser.START_TAG) {
                if(currentTag == null){
                    currentTag = parser.getName();
                } else {
                    ArrayList<XmlNode> childArray = convertChildToNodeTree(parser, currentTag);
                    if(childArray.size() > 0){
                        nodeArray.add(new XmlNode(currentTag, childArray));
                    }
                    currentTag = null;
                }
            } else if(eventType == XmlResourceParser.TEXT && !parser.getText().substring(0,1).equalsIgnoreCase("\n")) {
                nodeArray.add(new XmlNode(currentTag, parser.getText()));
                currentTag = null;
            } else if(eventType == XmlResourceParser.END_TAG) {
                endTag = parser.getName();
                if(endTag.equalsIgnoreCase(parentName))
                    return nodeArray;
                if(endTag.equalsIgnoreCase(currentTag))
                    currentTag = null;
            }
            eventType = parser.next();
        }
        return nodeArray;
    }
}
