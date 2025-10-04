package com.example.ds.interview.zeta;

class Solution {
    // Return true if S can be transformed into T by repeatedly replacing
    // any adjacent pair with '+' (zero or more times).

    static boolean conversion(String origin, String destiny) {
        int n = origin.length();
        int m = destiny.length();
        int i=0,j=0;

        if(n<m) return false;

        while(i<n && j<m) {
            char s = origin.charAt(i); // s(add +) --> t(need)
            char t = destiny.charAt(j);

            if(t == '-') {
                if(s!='-') return false;
                i++;
                j++;
            } else {
                if(s=='+') {
                    i++;
                    j++;
                } else { // -,
                    if(i+1>=n) return false;
                    i+=2;
                    j++;
                }

            }
        }

        return i == n && j == m;
    }

    public boolean canTransform(String S, String T) {
        int n = S.length(), m = T.length();
        if (m > n) return false; // can't increase length

        int i = 0, j = 0;
        while (i < n && j < m) {
            char s = S.charAt(i);
            char t = T.charAt(j);

            if (t == '-') {
                // must match exactly one '-' in S
                if (s != '-') return false;
                i++; j++;
            } else { // t == '+'
                if (s == '+') {
                    // match single '+'
                    i++; j++;
                } else {
                    // s == '-' -- we need at least two characters to collapse into a '+'
                    if (i + 1 >= n) return false;
                    // consume a group of size >= 2; greedy: take minimal size 2
                    i += 2;
                    j++;
                }
            }
        }

        // both should be fully consumed
        return i == n && j == m;
    }

    // small helper for quick testing
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.conversion("-+--+", "-+++")); // true (your example)
        System.out.println(sol.canTransform("-+--+", "-+++")); // true (your example)
        System.out.println(sol.canTransform("++", "+"));       // true (collapse)
        System.out.println(sol.canTransform("--", "+"));       // true (collapse)
        System.out.println(sol.canTransform("-+-", "++"));     // true (group [-, +] -> '+', left '-' -> cannot match '+', so depends)
        System.out.println(sol.canTransform("-+-", "-+"));     // true
        System.out.println(sol.canTransform("- - -".replace(" ",""), "++")); // false (not enough chars)
    }
}
