package com.example.ds.arrays;

import java.util.HashMap;
import java.util.Map;

public class LengthOfLongestSubstring {

    static public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int maxLen=0, left=0;
        char[] ch = s.toCharArray();

        for(int right=0; right<s.length(); right++) {
            while(map.containsKey(ch[right])) {
                map.remove(ch[left]);
                left++;
            }
            map.put(ch[right],1);
            maxLen = Math.max(maxLen, right-left+1);
        }
        return maxLen;
    }

    public static void main(String[] args) {
      String s = "pwwkew";
        System.out.println(lengthOfLongestSubstring(s));
    }
}
