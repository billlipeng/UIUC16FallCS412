package classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by zero on 11/29/16.
 */
public class ReadData {
    public static List<Instance> read(String fileName) throws Exception {
        BufferedReader br;
        br = new BufferedReader(new FileReader(fileName));
        String line;
        List<Instance> instanceList = new ArrayList<>();

        while ( (line = br.readLine()) != null) {
            if(line.length() > 2) {
                Map<Integer, Integer> attriMap = new TreeMap<>();
                String[] str = line.trim().split(" ");
                Integer label = Integer.valueOf(str[0]);

                for (int i = 1; i < str.length; ++i) {
                    String[] tmp = str[i].split(":");
                    attriMap.put(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]));
                }
                instanceList.add(new Instance(label, attriMap));
            }
        }

        br.close();

        return instanceList;
    }

}
