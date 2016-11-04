import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zero on 10/28/16.
 */
public class Preprocessing {

    private static List<String> readFile() throws Exception {
        BufferedReader br;
        List<String> res = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("./paper.txt"));
            try {
                String line;
                while ( (line = br.readLine()) != null) {
                    res.add(line.trim());
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return res;
    }

    //Generate a Dictionary
    private static void genDic() throws Exception{
        List<String> file = readFile();
        Set<String> keySet = new HashSet<>();
        for(String line: file) {
            String[] separateLine = line.split("\t");
            if (separateLine.length > 1) {
                String[] key = separateLine[1].split(" ");
                for (int i = 0; i < key.length; ++i) {
                    keySet.add(key[i]);
                }
            }
        }
        Path outputFile = Paths.get("./vocab.txt");
        Files.write(outputFile, keySet, Charset.forName("UTF-8"));
    }

    // Tokenize Plain Text by Dictionary
    private static void tokenizePlainText() throws Exception{
        // read key list from vocab
        Hashtable<String, Integer> keyTable = new Hashtable<>();
        BufferedReader br;
        br = new BufferedReader(new FileReader("./vocab.txt"));
        String line;
        Integer cnt = 0;
        while ( (line = br.readLine()) != null) {
            keyTable.put(line.trim(), cnt);
            ++cnt;
        }
        br.close();

        // read title from paper.txt
        List<String> file = readFile();
        // output array
        List<String> res = new ArrayList<>();

        for (String str: file) {
            String[] separateLine = str.split("\t");
            if (separateLine.length > 1) {
                String[] key = separateLine[1].split(" ");
                Hashtable<String, Integer> m = new Hashtable<>();
                for (String s : key) {
                    if(!m.containsKey(s)) {
                        m.put(s, 1);
                    } else{
                        m.replace(s,m.get(s)+1);
                    }
                }
                if(m.size()>0) {
                    String output = Integer.toString(m.size());
                    for (String s : m.keySet()) {
                        output += " " + Integer.toString(keyTable.get(s)) + ":" + Integer.toString(m.get(s));
                    }
                    res.add(output);
                }
            }
        }
        Path outputFile = Paths.get("./title.txt");
        Files.write(outputFile, res, Charset.forName("UTF-8"));
    }

    public static void main(String[] args) throws Exception {
        genDic();
        tokenizePlainText();
    }
}
