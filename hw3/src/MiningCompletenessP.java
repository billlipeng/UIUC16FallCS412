import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/3/16.
 */
public class MiningCompletenessP {
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



    private static void miningCompletenessP(int fileNum) throws Exception {

        Hashtable<String, Double> completenessTable = new Hashtable<>();
        Hashtable<String, Double> patternsTable = patternsList.get(fileNum);

        for(String s : patternsTable.keySet()){
            Integer cnt = 0;
            for(String title : topicList.get(fileNum)){
                if(strContains(title, s)){
                    ++cnt;
                }
            }
            Integer a = findMaxWord(fileNum, s);
//            System.out.println("   s= "+ s + "  "+ cnt + "  "+ a + "  com " + (double)a/(double)cnt );
            if(a == cnt){
                completenessTable.put(s, 0.0);
            } else {
                completenessTable.put(s, 1.0 - (double) a / (double) cnt);
            }
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(completenessTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

//        System.out.println("purity size " + l.size());

        // save to file
        String fileName = "./completeness/completeness-"+Integer.toString(fileNum)+".txt";
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


    private static Integer findMaxWord(int fileNum, String s){

        System.out.println(s);

        Integer max = 0;

        Set<String> set = new HashSet<>();
        for(String word : s.split(" ")){
            set.add(word);
        }
        for(String title : topicList.get(fileNum)){
            if(title != s && strContains(title, s)){
                for(String titleStr : title.split(" ")){
                    Integer cnt = 0;
                    if(!set.contains(titleStr)) {
                        set.add(titleStr);
                        String sTmp = s + " " + titleStr;
                        for (String tmp : topicList.get(fileNum)) {
                            if (strContains(tmp, sTmp)){
                                ++cnt;
                            }
                        }
                    }
                    max = Math.max(cnt, max);
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
            System.out.println("Generating Completeness of Pattern file " + i);
            miningCompletenessP(i);
        }
    }
}
