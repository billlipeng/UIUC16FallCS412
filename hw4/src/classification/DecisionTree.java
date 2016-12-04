package classification;

import java.util.*;

/**
 * Created by zero on 11/29/16.
 */
public class DecisionTree {

    public static void checkAccuracy(DT dt, List<Instance> exampleList){
        int cnt = 0;
        for(Instance instance : exampleList){
            if(dt.checkExample(instance)){
                cnt++;
            }
        }
        System.out.println((double)cnt/ (double)exampleList.size());
    }

    public static void main(String[] args) throws Exception{
        ReadData d = new ReadData();
        String train = "./data/train";
        String test1 = "./data/balance-scale.test";
        String train1 = "./data/balance-scale.train";
        String test2 = "./data/led.test.new";
        String train2 = "./data/led.train.new";
        String test3 = "./data/nursery.data.test";
        String train3 = "./data/nursery.data.train";
        String test4 = "./data/poker.test";
        String train4 = "./data/poker.train";
        List<Instance> trainList = d.read(train1);
        List<Instance> testList = d.read(train1);

        DT dt = new DT();
        dt.setRoot(trainList);
        checkAccuracy(dt, testList);


//        // Print input data
//        for(Instance instance : instanceList){
//            System.out.print(instance.getLabel());
//            System.out.println(instance.getAttriMap());
//        }
//        System.out.println(instanceList.size());


    }
}
