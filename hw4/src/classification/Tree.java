package classification;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 12/4/16.
 */

public class Tree<T> {
    private Node<T> rootNode;
    private ArrayList<Node<T>> nodes;

    public Tree() {
        nodes = new ArrayList<>();
    }

    public void addNode(Node<T> node){
        nodes.add(node);
    }

    public Node<T> getRootNode() {
        return this.rootNode;
    }

    public void setRootNode(Node<T> rootNode) {
        this.rootNode = rootNode;
    }

}