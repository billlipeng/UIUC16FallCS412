package classification;

import java.util.*;

/**
 * Created by zero on 11/29/16.
 */
public class DecisionTree {

    public void checkAccuracy(DT dt, List<Instance> exampleList){
        int cnt = 0;
        int labelSize = dt.getLabelSet().size();
        int[][] matrix = new int[labelSize][labelSize];
        for(int i=0; i<labelSize; ++i){
            Arrays.fill(matrix[i], 0);
        }
        Hashtable<Integer, Integer> labelTable = new Hashtable<>();
        int cntLabel = 0;
        for(Integer i: dt.getLabelSet()){
            labelTable.put(i, cntLabel++);
        }
        for(Instance instance : exampleList){
            Integer predict = dt.checkExample(instance);
            if( predict == instance.getLabel()){
                cnt++;
            }
            try {
                matrix[labelTable.get(instance.getLabel())][labelTable.get(predict)]++;
            } catch (Exception e) {
                System.out.println("label table = " + labelTable);
                System.out.println(" " + instance.getLabel()+ "  predict=  "+ predict);
                System.out.println(labelTable.get(instance.getLabel()) +" " + "  "+ labelTable.get(predict)+"  ");
            }
        }
        for(int i=0; i<labelSize; ++i){
            for(int j=0; j<labelSize; ++j) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
//        System.out.println("Accuracy = "+(double)cnt/ (double)exampleList.size());

//        calculation(matrix);
    }

    private void calculation(int[][] m){
        int classSize = m.length;
        for(int i=0; i<classSize; ++i){
            int matrix[][] = new int[2][2];

            for(int j=0; j<classSize; ++j){
                for(int k=0; k<classSize; ++k){
                    if(i==j && i==k){
                        matrix[1][1] = m[j][k];
                    } else if(i==j) {
                        matrix[0][1] += m[j][k];
                    } else if(i==k) {
                        matrix[1][0] += m[j][k];
                    } else {
                        matrix[0][0] += m[j][k];
                    }
                }
            }

            double  sensitivity = (double)(matrix[1][1]) / (double)(matrix[1][1] + matrix[1][0]),
                    specificity = (double)(matrix[0][0]) / (double)(matrix[0][0] + matrix[0][1]),
                    precision = (double)(matrix[1][1]) / (double)(matrix[1][1] + matrix[0][1]);
            double 	recall = sensitivity,
                    f1 = 2 * (precision * recall) / (precision + recall),
                    fhalf = (1 + 0.5*0.5) * (precision * recall) / (0.5*0.5 * precision + recall),
                    f2 = (1 + 2*2) * (precision * recall) / (2*2 * precision + recall);
            System.out.println();

            System.out.println("Class "+i+":");

            System.out.println(matrix[1][1]+"\t"+matrix[1][0]);
            System.out.println(matrix[0][1]+"\t"+matrix[0][0]);

            System.out.println("\tSensitivity: "+sensitivity);
            System.out.println("\tSpecificity: "+specificity);
            System.out.println("\tPrecision: "+precision);
            System.out.println("\tRecall: " + recall);
            System.out.println("\tF-1 Score: "+f1);
            System.out.println("\tF-0.5 Score: "+fhalf);
            System.out.println("\tF-2 Score: "+f2);
//             matrixList.add(matrix);

        }

    }

    public static void main(String[] args) throws Exception{
        ReadData d = new ReadData();
//        String test1 = "./data/balance-scale.test";
//        String train1 = "./data/balance-scale.train";
//        String test2 = "./data/led.test.new";
//        String train2 = "./data/led.train.new";
//        String test3 = "./data/nursery.data.test";
//        String train3 = "./data/nursery.data.train";
//        String test4 = "./data/poker.test";
//        String train4 = "./data/poker.train";
//
//        String t = train4;


        List<Instance> trainList = d.read(args[0]);
        List<Instance> testList = d.read(args[1]);


//        System.out.println("Filename: "+t);
//        System.out.println("Method: Decision Tree");

        DT dt = new DT();
        dt.setMaxDepth(100);
        dt.initDecisionTree(trainList);
        DecisionTree tt = new DecisionTree();
        tt.checkAccuracy(dt, testList);
    }
}
