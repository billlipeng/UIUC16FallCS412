package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zero on 12/4/16.
 */

/*
* Class : Node
* ------------------------------------------------------------
* Represents a single Node of a tree, holds in data of type T.
*/
public class Node<T> {
    // Data for the node
    private T data;
    private T depth;
    private T label;
    private T maxEdge;

    // A single node also has references to all its children
    public Map<T, Node<T>> children;




    /*
    * Default constructor
    */
    public Node() {
        children = new HashMap<>();
        label = null;
    }

    /*
    * Convenience constructor
    */
    public Node(T data) {
        setData(data);
        children = new HashMap<>();
        label = null;

    }

    public T getMaxEdge() {
        return maxEdge;
    }

    public void setMaxEdge(T maxEdge) {
        this.maxEdge = maxEdge;
    }

    public T getLabel() {
        return label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

    public void setDepth(T depth){
        this.depth = depth;
    }

    public T getDepth(){
        return this.depth;
    }
    /*
    * Function : setData()
    * ------------------------
    */
    public void setData(T data) {
        this.data = data;
    }

    /*
    * Function : getData()
    * ------------------------
    */
    public T getData() {
        return this.data;
    }

    /*
   * Function : getChild()
   * -------------------------------------------------------
   * Return the children of the current Node<T>. A Tree<T>
   * is represented by a single root Node<T> and its children
   * these children in turn can have children of their own.
   * The getChildren() method will return the children of Node<T>
   */
    public Node<T> getChild(Integer edge) {
        return this.children.get(edge);
    }

    /*
    * Function : getChildren()
    * -------------------------------------------------------
    * Return the children of the current Node<T>. A Tree<T>
    * is represented by a single root Node<T> and its children
    * these children in turn can have children of their own.
    * The getChildren() method will return the children of Node<T>
    */
    public Map<T, Node<T>> getChildren() {
        return this.children;
    }

    /*
    * Function : setChildren()
    * --------------------------------------------------------
    * Sets the children of the current node to the list of nodes
    * that is supplied as the argument.
     */
    public void setChildren(Map<T, Node<T>> children) {
        this.children = children;
    }

    /*
    * Function : addChild()
    * ----------------------------------------------------------
    * Adds a new child to the collection(List<Node<T>>) of children
    * of the current node.
    */
    public void addChild(T edge, Node<T> newChild) {
        children.put(edge, newChild);
    }

    /*
    * Function : numChildren()
    * -----------------------------------------------------------
    * Returns the number of children current node has.
     */
    public int numChildren() {
        return children.size();
    }
}