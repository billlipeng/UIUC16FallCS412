import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/3/16.
 */
public class MiningPhrasenessP {

    private static List<Set<String> > topicList = new ArrayList<>();

    private static List<Hashtable<String, Double>> patternsList = new ArrayList<>();

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



    private static void miningPhrasenessP(int fileNum) throws Exception {

        Hashtable<String, Double> purityTable = new Hashtable<>();
        Hashtable<String, Double> patternsTable = patternsList.get(fileNum);

        for(String s : patternsTable.keySet()){
            Double a = Math.log(patternsTable.get(s))/Math.log(2);
            Double b = findSum(fileNum, s);
            purityTable.put(s, a-b);
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(purityTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

//        System.out.println("purity size " + l.size());

        // save to file
        String fileName = "./phraseness/phraseness-"+Integer.toString(fileNum)+".txt";
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


    private static Double findSum(int fileNum, String s){

        Double sum = 0.0;

        String[] word = s.split(" ");

        for(int i=0; i<word.length; ++i){
            Integer wordCnt = 0;
            for(String title : topicList.get(fileNum)){
                if(strContains(title, word[i])){
                    ++wordCnt;
                }
            }
            sum += Math.log((double) wordCnt / (double) topicList.get(fileNum).size())/ Math.log(2);
        }

        return sum;
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
            System.out.println("Generating Phraseness of Pattern file " + i);
            miningPhrasenessP(i);
        }
    }
}
