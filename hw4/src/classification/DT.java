package classification;

import java.util.*;

/**
 * Created by zero on 11/29/16.
 */
public class DT {

    private Tree<Integer> decisionTree = new Tree<>();
//    private List<Edge> decisionTree = new ArrayList<>();
    private TreeSet<Integer> labelSet;
    private Integer maxDepth;
    private Integer depth;



    private Boolean forestRI;


//    private Double reductionImpurity;

    // featureList is a list of feature that hasn't been assigned.
    private void growTree(List<Instance> instanceList, List<Integer> featureList, Node<Integer> node){
        this.depth = Math.max(this.depth, node.getDepth());
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
//            System.out.println("FeatureMap = " + featureValueMap);
            Double product = 0.0;
            for(Integer featureValue : featureValueMap.keySet()){
                product = 1.0;
//                System.out.println(" featureValue = " + featureValue);
                for(Integer label : labelMap.keySet()){
//                    System.out.println(" label value = " + label);

                    Integer mr = 0;
                    for(Instance instance : instanceList){
                        if((instance.getLabel() == label) && (instance.getAttriMap().get(feature) == featureValue)){
                            ++mr;
                        }
                    }
//                    System.out.println( "mr =  " + mr + " " + featureValueMap.get(featureValue));

                    product -= ((double) mr / (double) featureValueMap.get(featureValue) * (double) mr / (double) featureValueMap.get(featureValue));
//                    if(product < 0.0)
//                        System.out.println("after product  = "+product);
                }
//                System.out.println(product + "  " + featureValueMap.get(featureValue) + "  "+instanceNum);
                product *= (double) featureValueMap.get(featureValue) / (double) instanceNum;
//                System.out.println("product =  " + product);
            }
            featureGini += product;
//            System.out.println( "feature gini = "+ featureGini);
            featureGiniList.add(featureGini);
        }

        // remove the feature from feature list
//        System.out.println(featureGiniList);
        Double min = Collections.min(featureGiniList);
        int indexOfMin = featureGiniList.indexOf(min);
//        System.out.println("index of min = " + indexOfMin);
        Integer selectedFeature = featureList.get(indexOfMin);
        List<Integer> newFeatureList = selectedFeature(indexOfMin, featureList);

        Map<Integer, Integer> selectedFeatureMap = countFeatureValue(instanceList, selectedFeature);
        node.setData(selectedFeature);

        // find the maximum number of value
        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : selectedFeatureMap.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                maxEntry = entry;
        }
        node.setMaxEdge(maxEntry.getKey());

        for(Integer value : selectedFeatureMap.keySet()){
//            System.out.println("featureList = " + featureList);

            Integer[] pure = checkLabelPure(selectedFeature, value, instanceList);

            if(pure[0] == 1) {
                Node<Integer> childNode = new Node<>();
                childNode.setDepth(node.getDepth()+1);
                childNode.setLabel(pure[1]);
                node.addChild(value, childNode);
            } else if (newFeatureList.size()==0 || node.getDepth() >= this.getMaxDepth()){
                Node<Integer> childNode = new Node<>();
                childNode.setDepth(node.getDepth()+1);
                childNode.setLabel(getMajorityLabel(selectedFeature, value, instanceList));
                node.addChild(value, childNode);
            } else {
                Node<Integer> childNode = new Node<>();
                childNode.setDepth(node.getDepth()+1);
                List<Instance> newInstanceList = selectInstance(selectedFeature, value, instanceList);
                growTree(newInstanceList, newFeatureList, childNode);
                node.addChild(value, childNode);
            }
        }
        decisionTree.addNode(node);
    }

    private List<Integer> selectedFeature(Integer indexOfMin, List<Integer> featureList){
        List<Integer> res = new ArrayList<>();

        for(int i=0; i<featureList.size(); ++i) {
            if (i != indexOfMin) {
                res.add(featureList.get(i));
            }
        }
        if(this.forestRI != null){
            if (this.forestRI) {
                long seed = System.nanoTime();
                Collections.shuffle(res, new Random(seed));
                List<Integer> featureListRI = new ArrayList<>();
                for (int i = 0; i < Math.sqrt(res.size()); ++i) {
                    featureListRI.add(res.get(i));
                }
                return featureListRI;
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
                        labelMap.put(label, 1);
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

    private Integer[] checkLabelPure(Integer selectedFeature, Integer value, List<Instance> instanceList){
        Integer[] res = new Integer[2];
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
                        res[0] = 0;
                        return res;
                    }
                }
            }
            res[0] = 1;
            res[1] = initLabel;
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
                res.put(example, 1);
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
                res.put(label, 1);
            } else {
                res.put(label, res.get(label) + 1);
            }
        }
        return res;
    }



    public void initDecisionTree(List<Instance> instanceList) {
        this.labelSet = new TreeSet<>(findLabel(instanceList).keySet());
        this.depth = Integer.MIN_VALUE;
        if(this.maxDepth == null){
            maxDepth = Integer.MAX_VALUE;
        }
        List<Integer> featureList = new ArrayList<>();
        if(instanceList != null){
            featureList = new ArrayList<>(instanceList.get(0).getAttriMap().keySet());
        }
        Node<Integer> rootNode = new Node<>();
        rootNode.setDepth(0);
        growTree(instanceList, featureList, rootNode);
        decisionTree.setRootNode(rootNode);

//        Stack<Node> stack = new Stack<>();
//        stack.push(rootNode);
//        while(!stack.empty()){
//            Node cur = stack.pop();
//            Map<Integer, Node> childrenMap = cur.getChildren();
//            for(Integer edge : childrenMap.keySet()){
//                stack.push(childrenMap.get(edge));
//            }
//        }
    }

    public Integer checkExample (Instance instance) {
        if (decisionTree == null) {
            System.out.println("Please initialize tree first!");
        } else {
            Node<Integer> node = decisionTree.getRootNode();
            while(node.getData() != null){
                Integer value = instance.getAttriMap().get(node.getData());
                if(node.getChildren().containsKey(value))
                    node = node.getChild(value);
                else {
                    node = node.getChild(node.getMaxEdge());
                }
            }

            return node.getLabel();
        }
        return -1;
    }

    public Integer getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public TreeSet<Integer> getLabelSet(){
        return this.labelSet;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setForestRI(Boolean forestRI) {
        this.forestRI = forestRI;
    }

}
