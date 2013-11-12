package dk.redweb.Red_App.XmlHandling;

import java.io.*;
import java.util.*;

/**
 * Created by Redweb with IntelliJ IDEA.
 * Date: 9/16/13
 * Time: 4:30 PM
 */
public class XmlNode implements Iterable<XmlNode>, Serializable {
    private String name;
    private Object value;

    public String name(){ return name; }
    public void setName(String name){ this.name = name; }
    public Object value(){ return value; }
    public void setValue(Object value){ this.value = value; }

    public int childCount(){
        if(value instanceof ArrayList){
            return ((ArrayList)value).size();
        }
        return -1;
    }

    public String getStringFromNode(String name) throws NoSuchFieldException {
        for (XmlNode child : (ArrayList<XmlNode>)value){
            if(child.name().equals(name) && child.value instanceof String){
                return (String)child.value;
            }
        }
        throw new NoSuchFieldException("Field " + name + " does not exist on node " + this.name);
    }

    public boolean getBoolFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        String expression = getStringFromNode(name);
        if(expression.equalsIgnoreCase("YES") || expression.equalsIgnoreCase("TRUE"))
            return true;
        else if(expression.equalsIgnoreCase("NO") || expression.equalsIgnoreCase("FALSE"))
            return false;
        throw new NumberFormatException("Field " + name + " on node " + this.name + " is not a recognized boolean expression");
    }

    public boolean getBoolWithNoneAsFalseFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        if (!hasChild(name))
            return false;
        return getBoolFromNode(name);
    }

    public double getDoubleFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        try{
            return Double.parseDouble(getStringFromNode(name));
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Field " + name + " on node " + this.name + " is not a double");
        }
    }

    public float getFloatFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        try{
            return Float.parseFloat(getStringFromNode(name));
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Field " + name + " on node " + this.name + " is not a float");
        }
    }

    public int getIntegerFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        try{
            return Integer.parseInt(getStringFromNode(name));
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Field " + name + " on node " + this.name + " is not an integer");
        }
    }

    public int[] getIntegerArrayFromNode(String name) throws NoSuchFieldException, NumberFormatException {
        try{
            String[] stringNums = getStringFromNode(name).split(",");
            int[] intArray = new int[stringNums.length];
            for (int i = 0; i < stringNums.length; i++){
                intArray[i] = Integer.parseInt(stringNums[i]);
            }
            return intArray;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Field " + name + " on node " + this.name + " is not an integer array");
        }
    }

    public XmlNode addChildToNode(String name, Object value) throws InvalidPropertiesFormatException {
        if(this.value instanceof ArrayList){
            ((ArrayList)this.value).add(new XmlNode(name, value));
            return this;
        }
        throw new InvalidPropertiesFormatException("The node " + this.name + " does not contain an array, and thus can't be added to");
    }

    public XmlNode getChildFromNode(String name) throws NoSuchFieldException {
        for (XmlNode child : (ArrayList<XmlNode>)value){
            if(child.name().equals(name)){
                return child;
            }
        }
        throw new NoSuchFieldException("Integer value " + name + " does not exist on node " + this.name);
    }

    public XmlNode[] getAllChildNodesWithName(String name){
        ArrayList<XmlNode> childNodes = new ArrayList<XmlNode>();
        int i = 0;
        for (XmlNode child : (ArrayList<XmlNode>)value){
            if(child.name().equals(name)){
                childNodes.add(child);
                i++;
            }
        }
        return childNodes.toArray(new XmlNode[i]);
    }

    public Map<String, Object> convertToMap(){
        return getDictionaryFromChildNode(this);
    }

    private Map<String, Object> getDictionaryFromChildNode(XmlNode node){

        Map<String, Object> dictionary = new HashMap<String, Object>();

        for (XmlNode child : (ArrayList<XmlNode>)node.value) {
            if(child.value instanceof ArrayList){
                Map<String, Object> childDictionary = getDictionaryFromChildNode(child);
                dictionary.put(child.name, childDictionary);
            }
            else{
                dictionary.put(child.name, child.value);
            }
        }
        return dictionary;
    }

    public boolean hasChild(String name){
        for(XmlNode node : (ArrayList<XmlNode>)value){
            if(node.name().equals(name)){
                return true;
            }
        }
        return false;
    }

    public XmlNode(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public XmlNode deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (XmlNode) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public Iterator<XmlNode> iterator(){
        return new XmlNodeIterator(this);
    }

    public static class XmlNodeIterator implements Iterator<XmlNode>{
        private int current;

        private boolean isArray;
        private ArrayList<XmlNode> nodeArray = null;

        XmlNodeIterator(XmlNode node) {
            current = 0;
            nodeArray = (ArrayList<XmlNode>)node.value;
        }

        @Override
        public boolean hasNext(){
            return current < nodeArray.size();
        }

        @Override
        public XmlNode next(){
            if(!hasNext()) throw new NoSuchElementException();
            return nodeArray.get(current++);
        }

        @Override
        public void remove() {
            // Choose exception or implementation:
            throw new UnsupportedOperationException();
            // or
            //// if (! hasNext())   throw new NoSuchElementException();
            //// if (currrent + 1 < myArray.end) {
            ////     System.arraycopy(myArray.arr, current+1, myArray.arr, current, myArray.end - current-1);
            //// }
            //// myArray.end--;
        }
    }
}

