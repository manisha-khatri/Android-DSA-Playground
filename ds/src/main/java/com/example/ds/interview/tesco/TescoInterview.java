package com.example.ds.interview.tesco;

public class TescoInterview {
    public static void main(String[] args) {
        int[] nums1 = {1,2,1,0,1,1,0};
        int k=4;

        int[] subArr = findLongestSubArrOpt(nums1, k);
        System.out.println("Method 1:-");
        for(int i: subArr) {
            System.out.print(i + " ");
        }

        System.out.println();

        subArr = longestSubArrayAtMostK(nums1, k);
        System.out.println("Method 2:-");
        for(int i: subArr) {
            System.out.print(i + " ");
        }
    }

    /**
     *
     */
    static int[] longestSubArrayAtMostK(int[] nums, int k) {
        int n = nums.length;
        int left=0, sum=0, right;
        int maxLen = Integer.MIN_VALUE;
        int bestStart=0;

        for(right=0; right<n; right++) {
            sum += nums[right];
            while(sum>k && left<=right) {
                sum-=nums[left];
                left++;
            }
            if(right-left+1> maxLen) {
                maxLen = right - left + 1;
                bestStart = left;
            }
        }

        int[] result = new int[maxLen];
        for(int i=0; i<maxLen; i++) {
            result[i] = nums[bestStart];
            bestStart++;
        }

        return result;
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
