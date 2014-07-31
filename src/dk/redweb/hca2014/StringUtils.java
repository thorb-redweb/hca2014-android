package dk.redweb.hca2014;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/19/13
 * Time: 9:21 AM
 */
public class StringUtils {

    public static String capitalizeFirstLetter(String rawString){
        if(rawString == null){
            return "";
        }
        return Character.toUpperCase(rawString.charAt(0)) + rawString.substring(1);
    }

    public static String capitalizeAll(String rawString){
        if(rawString == null){
            return "";
        }
        String[] wordList = rawString.split(" ");
        String returnString = "";
        for (String word : wordList){
            returnString += capitalizeFirstLetter(word) + " ";
        }
        returnString = returnString.substring(0, returnString.length()-2); //remove the last space
        return returnString;
    }

    public static String stripHtmlAndJoomla(String htmlString)
    {
        if(htmlString == null){
            return "";
        }
        htmlString = htmlString.replaceAll("\\<.*?\\>", "");    //Remove html tags
        htmlString = htmlString.replaceAll("&nbsp;", " ");      //Convert &nbsp; to space
        htmlString = stripJoomlaTags(htmlString);
        return htmlString;
    }

    public static String stripJoomlaTags(String htmlString)
    {
        if(htmlString == null){
            return "";
        }
        htmlString = htmlString.replaceAll("\\[.*?\\]\r\n", "");
        htmlString = htmlString.replaceAll("\\[.*?\\]", "");
        htmlString = htmlString.replaceAll("\\\\\"", "\"");         //Make \" into "
        return htmlString;
    }
}
