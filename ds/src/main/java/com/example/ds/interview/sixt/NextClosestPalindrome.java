package com.example.ds.interview.sixt;

/**
 * Odd Case:
 * 12932
 * 1. left=12   mid=9   right=32
 * 2. res = left --> right 12921
 * 3. if res<cur, 12921<12932
 * 4. mid ++ if(carry) then add at the left 9++ = 10 --> 13031
 *
 * Even Case:
 * 2133
 * 1. left = 21 right=33
 * 2. res = left --> right 2112 < 2133
 * 3. left last++, 1++ = 2 --> 2222
 */

public class NextClosestPalindrome {

    public static String nextPalindrome(String num) {
        int n = num.length();
        char[] s = num.toCharArray();

        // Case 1: All 9's → special case (e.g. 9, 99, 999 → 11, 101, 1001)
        boolean all9 = true;
        for (char c : s) {
            if (c != '9') {
                all9 = false;
                break;
            }
        }
        if (all9) {
            StringBuilder sb = new StringBuilder();
            sb.append('1');
            for (int i = 0; i < n - 1; i++) sb.append('0');
            sb.append('1');
            return sb.toString();
        }

        // Step 1: Mirror left → right
        char[] mirrored = s.clone();
        int left, right;
        left = n/2 - 1;

        while (left >= 0) {
            mirrored[n - 1 - left] = mirrored[left];
            left--;
        }

        String mirroredStr = new String(mirrored);

        // Step 2: If mirrored > original, return
        if (mirroredStr.compareTo(num) > 0) {
            return mirroredStr;
        }

        // Step 3: Otherwise, increment the middle (odd) or left half (even)
        mirrored = s.clone();
        int carry = 1;

        if (n % 2 == 1) {
            // Odd → increment middle first
            int mid = n / 2;
            int d = (mirrored[mid] - '0') + carry;
            mirrored[mid] = (char) ('0' + (d % 10));
            carry = d / 10;
            left = mid - 1;
            right = mid + 1;
        } else {
            // Even → start from left half
            left = (n / 2) - 1;
            right = (n / 2);
        }

        // Propagate carry into the left half, mirroring to right
        while (left >= 0 && carry > 0) {
            int d = (mirrored[left] - '0') + carry;
            mirrored[left] = (char) ('0' + (d % 10));
            carry = d / 10;
            mirrored[right] = mirrored[left]; // mirror change
            left--;
            right++;
        }

        // Mirror remaining unchanged digits
        while (left >= 0) {
            mirrored[right] = mirrored[left];
            left--;
            right++;
        }

        return new String(mirrored);
    }

    // Quick tests
    public static void main(String[] args) {
        String[] tests = {
                "123",   // odd → 131
                "808",   // odd → 818
                "2133",  // even → 2222
                "999",   // all 9's → 1001
                "12932", // odd → 13031
                "1221",  // even → 1331
                "9",     // single digit → 11
                "88",    // even → 99
                "1991",  // even → 2002
                "54321"  // odd → 54345
        };

        for (String t : tests) {
            System.out.printf("nextPalindrome(%s) = %s%n", t, nextPalindrome(t));
        }

    }
}
