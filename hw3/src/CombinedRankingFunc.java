import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/3/16.
 */
public class CombinedRankingFunc {

    private static double GAMMA = 0.5;
    private static double OMEGA = 0.5;


    private static List<Hashtable<String, Double>> patternsList = new ArrayList<>();
    private static List<Hashtable<String, Double>> purityList = new ArrayList<>();
    private static List<Hashtable<String, Double>> phrasenessList = new ArrayList<>();
    private static List<Hashtable<String, Double>> completenessList = new ArrayList<>();


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



    private static void combinedRanking(int fileNum) throws Exception {

        Hashtable<String, Double> combinedRankingT  = new Hashtable<>();
        Hashtable<String, Double> patternsTable = patternsList.get(fileNum);

        for(String s : patternsTable.keySet()){
            Double res;
            if(completenessList.get(fileNum).get(s) < GAMMA) {
                res = 0.0;
            }else{
                res = patternsTable.get(s) * (
                        (1-OMEGA) * purityList.get(fileNum).get(s)
                                + OMEGA * phrasenessList.get(fileNum).get(s));
            }
            combinedRankingT.put(s, res);
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(combinedRankingT.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});


        // save to file
        String fileName = "./combinedRanking/combinedRanking-"+Integer.toString(fileNum)+".txt";
        writeFile(fileName, l);
    }


    public static void main(String[] args) throws Exception {
        // Load data into memory
        for(int i=0; i<5; ++i){
            Hashtable<String, Double> patternT = new Hashtable<>();
            Hashtable<String, Double> purityT = new Hashtable<>();
            Hashtable<String, Double> phrasenessT = new Hashtable<>();
            Hashtable<String, Double> completenessT = new Hashtable<>();

            String patternFile = "./patterns/pattern-"+Integer.toString(i)+".txt";
            String purityFile = "./purity/purity-"+Integer.toString(i)+".txt";
            String phrasenessFile = "./phraseness/phraseness-"+Integer.toString(i)+".txt";
            String completenessFile = "./completeness/completeness-"+Integer.toString(i)+".txt";

            readFile(patternFile, patternT);
            readFile(purityFile, purityT);
            readFile(phrasenessFile, phrasenessT);
            readFile(completenessFile, completenessT);

            patternsList.add(patternT);
            purityList.add(purityT);
            phrasenessList.add(phrasenessT);
            completenessList.add(completenessT);
        }

        // Process data
        for(int i=0; i<5; ++i) {
            System.out.println("Generating Combined Ranking of Pattern file " + i);
            combinedRanking (i);
        }
    }

}
