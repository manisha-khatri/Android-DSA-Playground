package com.example.ds.arrays;

public class ArraysBasics {

    static int maxConsecutiveOnes(int[] prices) {
        int maxCount=0, count=0;

        for(int i=0; i<prices.length; i++) {
            if(prices[i] == 1) {
                count++;
            } else {
                count=0;
            }
            maxCount = Math.max(count, maxCount);
        }
        return maxCount;
    }

    static void moveZeroesToEnd(int[] nums) {
        // 1 ,0 ,2 ,3 ,0 ,4 ,0 ,1
        int pos=0;
        for(int i=0; i<nums.length; i++) {
            if(nums[i]!=0) {
                nums[pos] = nums[i];
                pos++;
            }
        }
        while(pos<nums.length) {
            nums[pos] = 0;
            pos++;
        }
        for(int val: nums) {
            System.out.print(val + " ");
        }
    }

    static void rotateArrayByK(int[] nums, int k) {
        // {1,2,3,4,5,6,7}
        int n = nums.length;
        k=k%n;
        reverse(0,n-1, nums);
        reverse(0,k-1, nums);
        reverse(k, n, nums);
    }

    static void reverse(int start, int end, int[] nums) {
        while(start<end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }


    public static void main(String[] args) {
        int[] prices = {1, 1, 0, 1, 1, 1};
        System.out.println(maxConsecutiveOnes(prices));

        moveZeroesToEnd(prices);


      
    }
}
