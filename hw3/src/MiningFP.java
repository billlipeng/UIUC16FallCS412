import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zero on 10/30/16.
 */
public class MiningFP {

    private static double MIN_SUP = 0.0003;
    private static List<String> topicList = new ArrayList<>();
    private static Hashtable<String, Integer> patterns = new Hashtable<>();

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
        BufferedReader br;
        br = new BufferedReader(new FileReader("topic-"+Integer.toString(fileNum)+".txt"));
        String line;
        while ( (line = br.readLine()) != null) {
            topicList.add(line.trim());
        }
        br.close();
        return;
    }

    private static void miningFP(int fileNum) throws Exception{

        readFile(fileNum);

        for ( int i = 0; i < topicList.size(); ++i ) {
            String s = topicList.get(i);
            String[] line = s.split(" ");
            Integer len = line.length;

            for(int j = 1; j <= len; ++j ) {
                List<String> combinatorList = printCombination(line, len, j);
                for(String combinator : combinatorList) {
                    if (!patterns.containsKey(combinator)){
                        patterns.put(combinator,1);
                    } else {
                        patterns.put(combinator,patterns.get(combinator)+1);
                    }
                }
            }
        }

        Hashtable<String, Double> m = new Hashtable<>();
        for(String key: patterns.keySet()){
            Double percent = (double) patterns.get(key) / (double) topicList.size();
            if( percent >= MIN_SUP) {
                m.put(key, percent);
            }
        }

        //Transfer as List and sort it
        ArrayList<Map.Entry<String, Double>> l = new ArrayList(m.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        List<String> outputList = new ArrayList<>();
        for(int i=0; i<l.size(); ++i){
            outputList.add(l.get(i).getKey());
        }

        // save to file
        writeFile(fileNum, outputList);
    }

    private static void writeFile(int fileNum, List<String> list) throws Exception{
        String fileName = "pattern-"+Integer.toString(fileNum)+".txt";
        Path outputFile = Paths.get(fileName);
        Files.write(outputFile, list, Charset.forName("UTF-8"));
    }

    public static void main(String[] args) throws Exception {
        for(int i=0; i<5; ++i) {
            miningFP(i);
        }
    }
}
