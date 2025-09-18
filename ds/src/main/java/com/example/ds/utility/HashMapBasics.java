package com.example.ds.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapBasics {
    public static void main(String[] args) {

        Map<String, Integer> map = new HashMap<>();
        map.put("Apple", 1);
        map.put("Banana", 1);
        map.put("Orange", 3);
        map.put("Beetroot", 4);

        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            System.out.println( entry.getKey() + " --> " + entry.getValue());
        }

        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            if(entry.getKey().equals("Orange")) {
                entry.setValue(10);
            }
        }

        System.out.println("Updated Map: " + map);

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a,b) -> {
            if(a.getValue()!=b.getValue())
                return b.getValue()-a.getValue(); // Descending order (b-a)

            return a.getKey().compareTo(b.getKey()); // Ascending order of string, lex order (a-b)
        });

        System.out.println("Result --> ");
        for(int i=0; i<list.size(); i++) {
            System.out.println(list.get(i).getKey());
        }
    }
}
