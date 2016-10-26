import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static List<String> transaction = new ArrayList<>();
    private static Hashtable<String, Integer> patterns = new Hashtable<>();

    private static void readFile() throws Exception {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("./data.transaction"));
            try {
                String x;
                while ( (x = br.readLine()) != null) {
                    transaction.add(x.replaceAll("\\s+",""));
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private static void findPattern() {
        for ( int i = 0; i < transaction.size(); ++i ) {
            String s = transaction.get(i);
            Integer len = s.length();
//            System.out.println(s + "\t" + len);
            for(int j = 1; j <= len; ++j ) {
                Permutation p = new Permutation();
                List<String> combinatorList = p.printCombination(s.toCharArray(), len, j);
//                System.out.println(j + " " + combinatorList);
                for(int k = 0; k < combinatorList.size(); ++k) {
                    String tmp = combinatorList.get(k);
                    if (patterns.get(tmp) == null){
                        patterns.put(tmp,1);
                    } else {
                        patterns.put(tmp,patterns.get(tmp)+1);
                    }
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {
        readFile();
        findPattern();
        Set<String> keys = patterns.keySet();

        Integer c = 0;
        Hashtable<String, Integer> m = new Hashtable<>();
        for(String key: keys){
            double percent = (double) patterns.get(key) / (double) transaction.size();
            if( percent >= 0.1) {
                m.put(key,patterns.get(key));
                System.out.println(++c + "  "+percent+ "  key = " + key + " cnt = " + patterns.get(key));
            }
        }

        int cntCE = 0;
        int cntCEA = 0;

        for(String key: transaction){
            if(key.contains("C") && key.contains("E")){
                System.out.println(key);

                ++cntCE;
                if(key.contains("A")){
                    ++cntCEA;
                }
            }
        }

        System.out.println((double) cntCEA / cntCE  + "   " + cntCE + "  "+cntCEA);


        /*Count the number of max patterns.*/
//        Set<String> k = new HashSet<>();
//
//        for(String s : m.keySet()){
//            k.add(s);
//        }
//
//        for(String s : k){
//            int len = s.length();
//            for(int i=1; i<len; ++i){
//                Permutation permutation = new Permutation();
//                List<String> stringList = permutation.printCombination(s.toCharArray(),len, i);
//                for(String subString : stringList){
//                    if(keys.contains(subString)){
//                        m.remove(subString);
//                        System.out.println( "k size = " + k.size());
//                    }
//                }
//
//            }
//
//        }
//        c = 0;
//        for(String ss: m.keySet()){
//            System.out.println(++c + "  "+ "  key = " + ss + " cnt = " + m.get(ss));
//        }


    }
}
