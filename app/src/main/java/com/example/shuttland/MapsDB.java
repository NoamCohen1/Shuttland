package com.example.shuttland;

import java.util.List;
import java.util.Map;

public class MapsDB {
    private Map<String, Float> distances;

    public void setStationsDistances() {
        distances.put("0->1", Float.valueOf("127"));
        distances.put("1->2", Float.valueOf("200"));
        distances.put("2->3", Float.valueOf("279"));
        distances.put("3->4", Float.valueOf("190"));
        distances.put("4->5", Float.valueOf("160"));
        distances.put("5->6", Float.valueOf("230"));
        distances.put("6->7", Float.valueOf("120"));
        distances.put("7->8", Float.valueOf("325"));
        distances.put("8->9", Float.valueOf("160"));
        distances.put("9->10", Float.valueOf("190"));
        distances.put("10->11", Float.valueOf("300"));
        distances.put("11->12", Float.valueOf("140"));
        distances.put("12->13", Float.valueOf("247"));
        distances.put("13->14", Float.valueOf("190"));
        distances.put("14->15", Float.valueOf("99"));
        distances.put("15->16", Float.valueOf("265"));
        distances.put("16->0", Float.valueOf("168"));
    }
}
