import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 10/31/16.
 */
public class MiningMaxCloP {
    private static Hashtable<String, Double> FPTable = new Hashtable<>();

    static void combinationUtil(String arr[], String data[], int start, int end, int index, int r, List<String> res) {
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

    static List<String> printCombination(String arr[], int n, int r) {
        List<String> res = new ArrayList<>();
        res.clear();
        String data[]=new String[r];
        combinationUtil(arr, data, 0, n-1, 0, r, res);
        return res;
    }

    private static void readFile(int fileNum) throws Exception {
        FPTable.clear();
        BufferedReader br;
        br = new BufferedReader(new FileReader("./patterns/pattern-"+Integer.toString(fileNum)+".txt"));
        String line;
        while ( (line = br.readLine()) != null) {
            String[] str = line.trim().split(" ");
            String tmp = "";
            for(int i=1; i<str.length; ++i){
                tmp += i==1 ? str[i] : " " + str[i];
            }
            FPTable.put(tmp, Double.valueOf(str[0]));
        }
//        System.out.println("FP size " + FPTable.size());
        br.close();
    }

    private static void miningClosedPattern(int fileNum) throws Exception{

        readFile(fileNum);
        Hashtable<String, Double> h = new Hashtable<>();
        for(String s : FPTable.keySet()){ h.put(s, FPTable.get(s)); }

        for(String s : h.keySet()){
            int len = s.split(" ").length;
            for(int i=1; i<len; ++i){
                List<String> stringList = printCombination(s.split(" "),len, i);
                for(String subString : stringList){
                    if(FPTable.containsKey(subString)
                            && (FPTable.get(subString) - h.get(s)<0.00001)){
                        FPTable.remove(subString);
                    }
                }
            }
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(FPTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});
        System.out.println("close size " + l.size());

        // save to file
        String fileName = "./closed/closed-"+Integer.toString(fileNum)+".txt";
        writeFile(fileName, l);
    }

    private static void miningMaximalPattern(int fileNum) throws Exception {
        readFile(fileNum);

        Set<String> set = new HashSet<>();
        set.addAll(FPTable.keySet());

        for(String s : set){
            int len = s.split(" ").length;
            for(int i=1; i<len; ++i){
                List<String> stringList = printCombination(s.split(" "),len, i);
                for(String subString : stringList){
                    if(FPTable.containsKey(subString)){
                        FPTable.remove(subString);
                    }
                }
            }
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(FPTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        System.out.println("max size " + l.size());

        // save to file
        String fileName = "./max/max-"+Integer.toString(fileNum)+".txt";
        writeFile(fileName, l);
    }

    private static void writeFile(String fileName, List<Map.Entry<String, Double> > list) throws Exception{
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        for(Map.Entry<String, Double> m : list) {
            writer.println(m.getValue() + " " + m.getKey());
        }
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        for(int i=0; i<5; ++i) {
            System.out.println("Generating Maximal Pattern file " + i);
            miningMaximalPattern(i);
        }
        for(int i=0; i<5; ++i) {
            System.out.println("Generating Closed Pattern file " + i);
            miningClosedPattern(i);
        }

    }
}
