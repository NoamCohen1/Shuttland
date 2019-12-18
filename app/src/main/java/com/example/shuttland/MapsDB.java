package com.example.shuttland;

import java.util.List;
import java.util.Map;

public class MapsDB {
    private Map<String, Float> distances;

    public void setStationsDistances() {
        distances.put("0->1", new Float(127));
        distances.put("1->2", new Float(200));
        distances.put("2->3", new Float(279));
        distances.put("3->4", new Float(190));
        distances.put("4->5", new Float(160));
        distances.put("5->6", new Float(230));
        distances.put("6->7", new Float(120));
        distances.put("7->8", new Float(325));
        distances.put("8->9", new Float(160));
        distances.put("9->10", new Float(190));
        distances.put("10->11", new Float(300));
        distances.put("11->12", new Float(140));
        distances.put("12->13", new Float(247));
        distances.put("13->14", new Float(190));
        distances.put("14->15", new Float(99));
        distances.put("15->16", new Float(265));
        distances.put("16->0", new Float(168));
    }
}
