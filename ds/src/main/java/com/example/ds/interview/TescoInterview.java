package com.example.ds.interview;

public class TescoInterview {
    public static void main(String[] args) {
        int[] nums1 = {3,1,2,5,1,1,2,3};
        int[] nums = {13,11,12,15,11,11,12,13};

        int k=6;

        int[] subArr = findLongestSubArrOpt(nums1, k);

        for(int i: subArr) {
            System.out.print(i + " ");
        }
    }

    /**
     *  Chatgpt solution: Sliding window optimized:
     *  TC = O(n)
     *  SC = O(k), k --> sub array
     */
    static int[] findLongestSubArrOpt(int[] nums, int k) {
        int start = 0, sum = 0;
        int maxLen = 0, maxStart = -1;

        for (int end = 0; end < nums.length; end++) {
            sum += nums[end];

            while (sum > k && start <= end) {
                sum -= nums[start];
                start++;
            }

            if (end - start + 1 > maxLen) {
                maxLen = end - start + 1;
                maxStart = start;
            }
        }

        if (maxLen > 0) {
            int[] res = new int[maxLen];
            System.arraycopy(nums, maxStart, res, 0, maxLen);
            return res;
        }

        return new int[0];
    }


    /**
     * Sliding window
     */
    static int[] findLongestSubArr(int[] nums, int k) {
        int i; //end
        int j=0; //start
        int maxSt=-1, maxEnd=-1; //Longest subArray
        int sum=0, ctr=0;

        for(i=0; i<nums.length; i++) {
            if(j==-1) j=i;
            sum += nums[i];

            while(sum>k && j>=0) {
                sum -= nums[j];
                j++;
                if(j>i) j=-1; // No current element is greater than k
            }
            if(j!=-1 && ctr<i-j+1) {
                ctr = i-j+1;
                maxSt = j;
                maxEnd = i;
            }
        }
        if(maxSt!=-1 && maxEnd!=-1) {
            int[] arr = new int[maxEnd-maxSt+1];
            for (i=0; i<arr.length; i++) {
                arr[i] = nums[maxSt];
                maxSt++;
            }
            return arr;
        }
        return new int[]{};
    }
}
