package com.example.ds.arrays;

public class MoveZerosToEnd {

    static int[] moveZerosToEnd(int[] nums) {
        int n = nums.length;

        int pos =0;

        for(int i=0; i<n; i++) {
            if(nums[i]!=0) {
                nums[pos++] = nums[i];
            }
        }

        while(pos<n) {
            nums[pos++] = 0;
        }

        return nums;
    }

    public static void main(String[] args) {
        int[] example = {0, 1, 0, 3, 12};
        moveZerosToEnd(example);
        for (int num : example) {
            System.out.print(num + " ");
        }
    }
}
