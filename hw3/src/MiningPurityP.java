import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by zero on 11/1/16.
 */
public class MiningPurityP {

    private static List<Hashtable<String, Integer> > lists = new ArrayList<>();

    private static List<Hashtable<String, Double> > patternsList = new ArrayList<>();

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
            writer.println(m.getKey());
        }
        writer.close();
    }

    private static void reOrganize() throws Exception{
        BufferedReader br;
        br = new BufferedReader(new FileReader("./result/word-assignments.dat"));
        String line;
        for(int i=0; i<5; ++i) {
            Hashtable<String, Integer> table = new Hashtable<>();
            lists.add(table);
        }

        int cnt = 0;
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
                if(strArr[i].length()>0) {
                    List<String> list = Arrays.asList(strArr[i].trim().split(" "));

                    Collections.sort(list, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            if (o1.length() > o2.length()) {
                                return 1;
                            } else if (o1.length() < o2.length()) {
                                return -1;
                            }
                            return o1.compareTo(o2);
                        }
                    });

                    String newStr = "";
                    for(int j=0; j<list.size(); ++j){
                        newStr += j==0 ? list.get(j) : " " + list.get(j);
                    }
                    lists.get(i).put(newStr, cnt);
                }
            }
            ++cnt;
        }
        br.close();
    }

    private static void miningPurityP(int fileNum) throws Exception {

        Hashtable<String, Double> purityTable = new Hashtable<>();
        Hashtable<String, Double> tmpTable = new Hashtable<>();
        Hashtable<String, Double> patternsTable = patternsList.get(fileNum);

        Double max = Double.NEGATIVE_INFINITY;
        Double min = Double.POSITIVE_INFINITY;
        for(String s : patternsTable.keySet()){
            Double a = Math.log(patternsTable.get(s))/Math.log(2);
            Double b = Math.log(findMax(fileNum, s))/Math.log(2);
            Double purity = a - b;
            max = Math.max(max, purity);
            min = Math.min(min, purity);
            tmpTable.put(s, purity);
        }
        for(String s : tmpTable.keySet()){
            purityTable.put(Double.toString(tmpTable.get(s))+" "+s, (tmpTable.get(s)-min)/(max - min) * patternsTable.get(s));
        }

        //Transfer as List and sort it
        List<Map.Entry<String, Double> > l = new ArrayList(purityTable.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});

        // save to file
        String fileName = "./purity/purity-"+Integer.toString(fileNum)+".txt";
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

    private static Double findMax(int fileNum, String s){
        Double max = Double.NEGATIVE_INFINITY;
        for(int i=0; i<5; ++i){
            Integer cnt0 = 0;
            Integer cnt1 = 0;
            Set<Integer> unionSet = new HashSet<>();
            if(i != fileNum) {
                for(String tmp : lists.get(i).keySet()) {
                    unionSet.add(lists.get(i).get(tmp));
                    if (strContains(tmp, s)) {
                        ++cnt0;
                    }
                }
                for (String tmp : lists.get(fileNum).keySet()) {
                    unionSet.add(lists.get(fileNum).get(tmp));
                    if (strContains(tmp, s)) {
                        ++cnt1;
                    }
                }
                max = Math.max(max, ((double) (cnt0 + cnt1)) / (double) unionSet.size());
            }
        }
        return max;
    }

    public static void main(String[] args) throws Exception {

        // Load data into memory
        reOrganize();

        for(int i=0; i<5; ++i){
            Hashtable<String, Double> hashtable = new Hashtable<>();
            String patternFile = "./patterns/pattern-"+Integer.toString(i)+".txt";
            readFile(patternFile, hashtable);
            patternsList.add(hashtable);
        }

        // Process data
        for(int i=0; i<5; ++i) {
            System.out.println("Generating Purity of Pattern file " + i);
            miningPurityP(i);
        }
    }

}
