import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/1/16.
 */
public class MiningPurityP {

    private static List<Set<String> > topicList = new ArrayList<>();

    private static List<Hashtable<String, Double> > patternsList = new ArrayList<>();

    private static void combinationUtil(String arr[], String data[], int start, int end, int index, int r, List<String> res) {
        if (index == r) {
            String a = "";
            for (int j=0; j<r; j++){
                a += (j==0) ? data[j] : " "+data[j];
            }
            res.add(a);
            return;
        }
        for (int i=start; i<=end && end-i+1 >= r-index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r, res);
        }
    }

    private static List<String> printCombination(String arr[], int n, int r) {
        List<String> res = new ArrayList<>();
        res.clear();
        String data[]=new String[r];
        combinationUtil(arr, data, 0, n-1, 0, r, res);
        return res;
    }

    private static void readFile(String fileName, Object o) throws Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(fileName));
        String line;
        while ( (line = br.readLine()) != null) {
            if(o instanceof Set) {
                ((Set) o).add(line.trim());
            }
            else if(o instanceof Hashtable){
                String[] str = line.trim().split(" ");
                String tmp = "";
                for(int i=1; i<str.length; ++i){
                    tmp += i==1 ? str[i] : " " + str[i];
                }
                ((Hashtable) o).put(tmp, Double.valueOf(str[0]));
            }
        }
        br.close();
    }

    private static void writeFile(String fileName, List<Map.Entry<String, Double> > list) throws Exception{
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        for(Map.Entry<String, Double> m : list) {
            writer.println(m.getValue() + " " + m.getKey());
        }
        writer.close();
    }



    private static void miningPurityP(int fileNum) throws Exception {

        Hashtable<String, Double> purityTable = new Hashtable<>();
        Hashtable<String, Double> patternsTable = patternsList.get(fileNum);

        for(String s : patternsTable.keySet()){
            Double a = Math.log(patternsTable.get(s))/Math.log(2);
            Double b = Math.log(findMax(fileNum, s))/Math.log(2);
            purityTable.put(s, a-b);
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(purityTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        System.out.println("purity size " + l.size());

        // save to file
        String fileName = "./purity/purity-"+Integer.toString(fileNum)+".txt";
        writeFile(fileName, l);
    }

    private static boolean strContains(String longer, String shorter){
        if(longer.length()<1 || shorter.length()<1)
            System.out.println("strContains input len < 1");
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(longer.split(" ")));
        String[] shorterArr = shorter.split(" ");

        for(String s : shorterArr){
            if(!set.contains(s))
                return false;
        }
        return true;
    }


    private static Double findMax(int fileNum, String s){
        Double max = Double.NEGATIVE_INFINITY;
        for(int i=0; i<5; ++i){
            Integer cnt0 = 0;
            Integer cnt1 = 0;
            Set<String> unionSet = new HashSet<>();
            if(i != fileNum) {
                for(String tmp : topicList.get(i)) {
                    unionSet.add(tmp);
                    if (strContains(tmp, s)) {
                        ++cnt0;
                    }
                }
                if(cnt0 > 0) {
                    for (String tmp : topicList.get(fileNum)) {
                        unionSet.add(tmp);
                        if (strContains(tmp, s)) {
                            ++cnt1;
                        }
                    }
                    if(cnt0 == 1)
                        System.out.println(max + " " + cnt0 + " " + cnt1 + " " + s);
                    max = Math.max(max, ((double) (cnt0 + cnt1)) / (double) unionSet.size());
                } else {
                    max = Math.max(max, patternsList.get(fileNum).get(s));

                }
            }
        }
        return max;
    }

    public static void main(String[] args) throws Exception {

        // Load data into memory
        for(int i=0; i<5; ++i){
            Set<String> set = new HashSet<>();
            Hashtable<String, Double> hashtable = new Hashtable<>();

            String topicFile = "./topic-"+Integer.toString(i)+".txt";
            String patternFile = "./patterns/pattern-"+Integer.toString(i)+".txt";

            readFile(topicFile, set);
            readFile(patternFile, hashtable);

            topicList.add(set);
            patternsList.add(hashtable);
        }

        // Process data
        for(int i=0; i<5; ++i) {
            System.out.println("Generating Purity of Pattern file " + i);
            miningPurityP(i);
        }
    }
}
