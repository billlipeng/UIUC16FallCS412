import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zero on 10/28/16.
 */
public class Partitioning {

    private static void reOrganize() throws Exception{

        BufferedReader br;
        br = new BufferedReader(new FileReader("./result/word-assignments.dat"));
        String line;
        List<List<String> > lists = new ArrayList<>();
        for(int i=0; i<5; ++i){
            List<String> list = new ArrayList<>();
            lists.add(list);
        }

        while ( (line = br.readLine()) != null) {
            String[] l = line.trim().split(" ");

            // one strArr for one topic of line of each title
            String[] strArr = new String[5];
            Arrays.fill(strArr,"");
            for(int i=1; i<l.length; ++i){
                String[] str = l[i].split(":");
                int n = Integer.valueOf(str[1]);
                strArr[n] += strArr[n].length()<1 ? str[0] : " " + str[0];
            }
            // add one line to corresponding
            for(int i=0; i<5; ++i) {
                if(strArr[i].length()>0)
                    lists.get(i).add(strArr[i]);
            }
        }
        br.close();

        // save to file
        for(int i=0; i<5; ++i){
            output(i, lists.get(i));
        }
    }

    private static void output(int i, List<String> list) throws Exception{
        String fileName = "./topic-"+Integer.toString(i)+".txt";
        Path outputFile = Paths.get(fileName);
        Files.write(outputFile, list, Charset.forName("UTF-8"));
    }


    public static void main(String[] args) throws Exception {
        reOrganize();
    }
}
