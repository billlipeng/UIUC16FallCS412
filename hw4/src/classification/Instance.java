package classification;

import java.util.Map;

/**
 * Created by zero on 11/29/16.
 */
public class Instance {
    private Integer label;
    private Map<Integer, Integer> attriMap;
    private Integer attriNum;

    public Instance(Integer label, Map<Integer, Integer> attriMap){
        this.label = label;
        this.attriMap = attriMap;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Map<Integer, Integer> getAttriMap() {
        return attriMap;
    }

    public void setAttriMap(Map<Integer, Integer> attriMap) {
        this.attriMap = attriMap;
    }

    public Integer getAttriNum() {
        return this.attriMap.size();
    }
}
