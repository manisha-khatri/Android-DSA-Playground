package com.example.ds.arrays;

public class MedianOfTwoSortedArraysBrute {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        int i,j;
        i = j = 0;
        int n3 = n1+n2;
        int counter = 0;
        int index1, index1Ele, index2, index2Ele;
        index2 = n3/2;
        index1 = index2-1;
        index1Ele = index2Ele = Integer.MIN_VALUE;

        while(i<n1 && j<n2) {
            int min;

            if(nums1[i] <= nums2[j]) {
                min = nums1[i];
                i++;
            } else {
                min = nums2[j];
                j++;
            }
            if(counter == index2) {
                index2Ele = min;
                break;
            }
            if(counter == index1) {
                index1Ele = min;
            }
            counter++;
        }

        while(i<n1 && index2Ele == Integer.MIN_VALUE) {
            if(counter == index2) {
                index2Ele = nums1[i];
                break;
            }
            if(counter == index1) {
                index1Ele = nums1[i];
            }
            i++;
            counter++;
        }

        while(j<n2 && index2Ele == Integer.MIN_VALUE) {
            if(counter == index2) {
                index2Ele = nums2[j];
                break;
            }
            if(counter == index1) {
                index1Ele = nums2[j];
            }
            j++;
            counter++;
        }

        double res = 0.0;

        if(n3%2 == 0) {
            res = (double) (index2Ele + index1Ele) /2;
        } else {
            res = (double) index2Ele;
        }

        return res;
    }

    public static void main(String[] args) {
        MedianOfTwoSortedArraysBrute obj = new MedianOfTwoSortedArraysBrute();

        System.out.println(obj.findMedianSortedArrays(new int[]{1, 3}, new int[]{2})); // 2.0
        System.out.println(obj.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4})); // 2.5
    }
}
