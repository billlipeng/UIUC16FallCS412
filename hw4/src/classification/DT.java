package classification;

import java.util.*;

/**
 * Created by zero on 11/29/16.
 */
public class DT {

    private List<Edge> decisionTree = new ArrayList<>();
    private Integer root;
    private Integer maxDepth;
//    private Double reductionImpurity;

    // featureList is a list of feature that hasn't been assigned.
    private Integer growTree(List<Instance> instanceList, List<Integer> featureList, Integer depth){
        Integer instanceNum = instanceList.size();
        Map<Integer, Integer> labelMap = findLabel(instanceList);
//        Double gini = 1.0;
//        for (Integer key : labelMap.keySet()){
//            gini -= (double) (labelMap.get(key)*labelMap.get(key)) / (double) (instanceNum*instanceNum);
//        }
//        System.out.println( "gini = "+ gini);

        // Find the Gini Value for each feature list
        List<Double> featureGiniList = new ArrayList<>();
        for (Integer feature : featureList){
            Double featureGini = 0.0;
            Map<Integer, Integer> featureValueMap = countFeatureValue(instanceList, feature);
            Double product = 0.0;
            for(Integer featureValue : featureValueMap.keySet()){
                product = 1.0;
                for(Integer label : labelMap.keySet()){
                    Integer mr = 0;
                    for(Instance instance : instanceList){
                        if((instance.getLabel() == label) && (instance.getAttriMap().get(feature) == featureValue)){
                            ++mr;
                        }
                    }
                    product -= ((double) mr / (double) featureValueMap.get(featureValue) * (double) mr / (double) featureValueMap.get(featureValue));
//                    System.out.println(product+" " + mr + " " + featureValueMap.get(featureValue));
                }
                product = Math.abs(product);
//                System.out.println(product + "  " + featureMap.get(m) + "  "+instanceNum);
                product *= (double) featureValueMap.get(featureValue) / (double) instanceNum;
//                System.out.println("product =  " + product);
            }
            featureGini += product;
            System.out.println( "feature gini = "+ featureGini);
            featureGiniList.add(featureGini);
        }

        // remove the feature from feature list
        System.out.println(featureGiniList);
        Double min = Collections.min(featureGiniList);
        int indexOfMin = featureGiniList.indexOf(min);
        Integer selectedFeature = featureList.get(indexOfMin);
        List<Integer> newFeatureList = selectedFeature(indexOfMin, featureList);

        Map<Integer, Integer> selectedFeatureMap = countFeatureValue(instanceList, selectedFeature);

        // Add edge to the tree

        for(Integer value : selectedFeatureMap.keySet()){
            System.out.println("featureList = " + featureList);

            Edge edge = new Edge();
            edge.setHead(selectedFeature);
            edge.setEdge(value);

            List<Integer> pure = checkLabelPure(selectedFeature, value, instanceList);
            System.out.println("feature = " + selectedFeature + " value = " + value +"  pure = " + pure.get(0)+ "  depth = " + depth);
            if(pure.get(0) == 1) {
                edge.setTail(null);
                edge.setLabel(pure.get(1));
            } else if (newFeatureList.size()==0){
                edge.setTail(null);
                edge.setLabel(getMajorityLabel(selectedFeature, value, instanceList));
            } else {
                List<Instance> newInstanceList = selectInstance(selectedFeature, value, instanceList);
                edge.setTail(growTree(newInstanceList, newFeatureList, depth+1));
                edge.setLabel(null);
            }
            edge.setDepth(depth);
            decisionTree.add(edge);
        }

        return selectedFeature;
    }

    private List<Integer> selectedFeature(Integer indexOfMin, List<Integer> featureList){
        List<Integer> res = new ArrayList<>();
        for(int i=0; i<featureList.size(); ++i){
            if(i!=indexOfMin){
                res.add(featureList.get(i));
            }
        }
        return res;
    }


    private List<Instance> selectInstance(Integer selectedFeature, Integer value, List<Instance> instanceList){
        List<Instance> newInstanceList = new ArrayList<>();
        for(Instance instance : instanceList){
            if(instance.getAttriMap().get(selectedFeature) == value){
                newInstanceList.add(instance);
            }
        }
        return newInstanceList;
    }


    private Integer getMajorityLabel(Integer selectedFeature, Integer value, List<Instance> instanceList){
        Map<Integer, Integer> labelMap = new HashMap<>();
        Integer maxLabel = Integer.MIN_VALUE;
        Integer max = Integer.MIN_VALUE;
        if(instanceList != null) {
            for (Instance instance : instanceList) {
                if(instance.getAttriMap().get(selectedFeature) == value ) {
                    Integer label = instance.getLabel();
                    if(!labelMap.containsKey(label)){
                        labelMap.put(label, 0);
                    } else {
                        labelMap.put(label, labelMap.get(label) + 1);
                    }
                }
            }
        }
        for(Integer label : labelMap.keySet()){
            Integer tmp = Math.max(max, labelMap.get(label));
            if(max != tmp) {
                max = tmp;
                maxLabel = label;
            }
        }

        return maxLabel;
    }

    private List<Integer> checkLabelPure(Integer selectedFeature, Integer value, List<Instance> instanceList){
        List<Integer> res = new ArrayList<>();
        if(instanceList != null) {
            Integer initLabel = 0;
            boolean flag = true;
            for (Instance instance : instanceList) {
                if(flag && instance.getAttriMap().get(selectedFeature) == value){
                    initLabel = instance.getLabel();
                    flag = false;
                }
                else{
                    if(instance.getAttriMap().get(selectedFeature) == value && initLabel != instance.getLabel()) {
                        res.add(0);
                        return res;
                    }
                }
            }
            res.add(1);
            res.add(initLabel);
            return res;
        } else{
            System.out.println("InstanceList is Null!");
        }
        return res;
    }

    private Map<Integer, Integer> countFeatureValue(List<Instance> instanceList, Integer feature){
        Map<Integer, Integer> res = new TreeMap<>();
        for (Instance instance: instanceList) {
            Integer example = instance.getAttriMap().get(feature);
            if(!res.containsKey(example)){
                res.put(example, 0);
            } else {
                res.put(example, res.get(example) + 1);
            }
        }
        return res;
    }

    private Map<Integer, Integer> findLabel(List<Instance> instanceList){
        Map<Integer, Integer> res = new TreeMap<>();
        for (Instance instance: instanceList) {
            Integer label = instance.getLabel();
            if(!res.containsKey(label)){
                res.put(label, 0);
            } else {
                res.put(label, res.get(label) + 1);
            }
        }
        return res;
    }

    public List<Edge> getDecisionTree() {
        return decisionTree;
    }

    public void initDecisionTree(List<Instance> instanceList) {
        setRoot(instanceList);
    }

    public Integer getRoot() {
        return root;
    }

    public void setRoot(List<Instance> instanceList) {
        System.out.println("=====Start building DT=====");
        List<Integer> featureList = new ArrayList<>();
        if(instanceList != null){
            featureList = new ArrayList<>(instanceList.get(0).getAttriMap().keySet());
        }
        this.root = growTree(instanceList, featureList, 1);
        System.out.println("=====Finish DT=====");
        System.out.println("=====Printing DT=====");
        System.out.println("Tree Root = " + root);
        for(Edge edge : decisionTree){
            System.out.println("head = " + edge.getHead() + "  tail = " + edge.getTail() + "  edge = " + edge.getEdge() + " label = " + edge.getLabel());
        }
        System.out.println("=====End of DT=====");


    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Boolean checkExample (Instance instance) {
        if (decisionTree.size() < 1) {
            System.out.println("Please initialize tree first!");
        } else if (findLeaf(instance, root) == instance.getLabel()) {
//            System.out.println("instance = " + instance.getAttriMap() + " label = "+ instance.getLabel());

            return true;
        }
//        System.out.println("Wrong instance = " + instance.getAttriMap() + " label = "+ instance.getLabel());

        return false;
    }

    private Integer findLeaf (Instance instance, Integer node){

        for(Edge edge : decisionTree){
            if(edge.getHead() == node && edge.getEdge() == instance.getAttriMap().get(node)){
                if(edge.getTail() == null){
                    return edge.getLabel();
                } else{
                    return findLeaf(instance, edge.getTail());
                }
            }
        }
        return -1;
    }


    public class Edge{

        private Integer head;
        // If tail is the leaf of the tree, then set to -1. The edge will be the actual decision
        private Integer tail;
        private Integer edge;
        private Integer depth;
        private Integer label;

        public Integer getLabel() {
            return label;
        }

        public void setLabel(Integer label) {
            this.label = label;
        }


        public Integer getHead() {
            return head;
        }

        public void setHead(Integer head) {
            this.head = head;
        }

        public Integer getTail() {
            return tail;
        }

        public void setTail(Integer tail) {
            this.tail = tail;
        }

        public Integer getEdge() {
            return edge;
        }

        public void setEdge(Integer edgeInfo) {
            this.edge = edgeInfo;
        }

        public Integer getDepth() {
            return depth;
        }

        public void setDepth(Integer depth) {
            this.depth = depth;
        }

    }
}
