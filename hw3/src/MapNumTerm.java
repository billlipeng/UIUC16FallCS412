import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zero on 11/3/16.
 */
public class MapNumTerm {

    private static Hashtable<Integer, String> dic = new Hashtable<>();

    private static void readFile(String fileName, List<String> list) throws Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(fileName));
        String line;
        while ( (line = br.readLine()) != null) {
            list.add(line.trim());
        }
        br.close();
    }

    private static void mapNumTerm(String dirName, String fileName, int fileNum) throws Exception {

        String inputFile = "./" + dirName + "/"+ fileName + "-"+Integer.toString(fileNum)+".txt";
        List<String> inputList  = new ArrayList<>();
        List<String> outputList  = new ArrayList<>();

        readFile(inputFile, inputList);

        for(String s : inputList){
            String[] wordArr = s.split(" ");
            String newStr = wordArr[0];
            for(int i=1; i<wordArr.length; ++i){
                newStr += " "+dic.get(Integer.valueOf(wordArr[i]));
            }
            outputList.add(newStr);
        }

        // save to file
        String outputName = "./" + dirName + "/"+ fileName + "-"+Integer.toString(fileNum)+".txt.phrase";
        Path outputFile = Paths.get(outputName);
        Files.write(outputFile, outputList, Charset.forName("UTF-8"));
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
