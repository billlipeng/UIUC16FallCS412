import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/3/16.
 */
public class MapNumTerm {

    private static Hashtable<Integer, String> dic = new Hashtable<>();

    private static void readFile(String fileName, Object o) throws Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(fileName));
        String line;
        while ( (line = br.readLine()) != null) {
            String[] str = line.trim().split(" ");
            String tmp = "";
            for(int i=1; i<str.length; ++i){
                tmp += i==1 ? str[i] : " " + str[i];
            }
            ((Hashtable) o).put(tmp, Double.valueOf(str[0]));
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



    private static void mapNumTerm(String dirName, String fileName, int fileNum) throws Exception {

        String inputFile = "./" + dirName + "/"+ fileName + "-"+Integer.toString(fileNum)+".txt";
        Hashtable<String, Double> inputputT  = new Hashtable<>();
        Hashtable<String, Double> outputT  = new Hashtable<>();

        readFile(inputFile, inputputT);

        for(String s : inputputT.keySet()){
            String[] wordArr = s.split(" ");
            String newStr = "";
            for(int i=0; i<wordArr.length; ++i){
                newStr += i==0? dic.get(Integer.valueOf(wordArr[i])) : " "+dic.get(Integer.valueOf(wordArr[i]));
            }

            outputT.put(newStr, inputputT.get(s));
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(outputT.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        // save to file
        String outputFile = "./" + dirName + "/"+ fileName + "-"+Integer.toString(fileNum)+".txt.phrase";
        writeFile(outputFile, l);
    }


    public static void main(String[] args) throws Exception {


        BufferedReader br;
        br = new BufferedReader(new FileReader("vocab.txt"));
        String line;
        int cnt = 0;
        while ( (line = br.readLine()) != null) {
            dic.put(cnt++, line.trim());
        }
        br.close();

        String[] dirName = {"closed", "combinedRanking", "completeness", "max", "patterns", "phraseness", "purity" };
        String[] fileName = {"closed", "combinedRanking", "completeness", "max", "pattern", "phraseness", "purity" };

        for(int i=0; i<fileName.length; ++i){
            for(int j=0; j<5; ++j) {
                System.out.println("Mapping number to phrase " + fileName[i] + " " + j);
                mapNumTerm(dirName[i], fileName[i], j);
            }
            System.out.println();
        }
    }
}
