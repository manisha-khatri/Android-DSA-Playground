package com.example.datastructure.arrays;

import java.util.*;

public class ThreeSum {
    public static void main(String[] args) {
        int[] nums = {-1,0,1,2,-1,-4};

        System.out.println("3Sum BruteForce Solution ---> ");
        List<List<Integer>> ans = threeSumBruteForce(nums);
        for(List list: ans) {
            System.out.println(list);
        }

        System.out.println("3Sum Optimized Solution ---> ");
        List<List<Integer>> ans2 = threeSumOptimized1(nums);
        for(List list2: ans2) {
            System.out.println(list2);
        }

        System.out.println("3Sum Optimized Solution 2 ---> ");
        List<List<Integer>> ans3 = threeSumOptimized2(nums);
        for(List list2: ans3) {
            System.out.println(list2);
        }

        System.out.println("3Sum Optimized Solution 3 ---> ");
        List<List<Integer>> ans4 = threeSumOptimized3(nums);
        for(List list4: ans4) {
            System.out.println(list4);
        }

        //
    }

    /**
     * Brute force -- O(n^3)
     */
    public static List<List<Integer>> threeSumBruteForce(int[] nums) {
      List<List<Integer>> ans = new ArrayList<>();
      Set<List<Integer>> seen = new HashSet<>();
      int n = nums.length;

      for(int i=0; i<n; i++) {
          for(int j=i; j<n; j++) {
              for(int k=j; k<n; k++) {
                  if(i != j && i != k && j != k && nums[i]+nums[j]+nums[k]==0)
                  {
                      List<Integer> elements = Arrays.asList(nums[i], nums[j], nums[k]);
                      Collections.sort(elements);

                      if(!seen.contains(elements)) {
                          seen.add(elements);
                          ans.add(elements);
                      }
                  }
              }
          }
      }
      return ans;
    }

    /**
     * Two Sum -- O(n^2)
     */
    static List<List<Integer>> threeSumOptimized1(int[] nums) {
        Set<List<Integer>> mainList = new HashSet<>();

        for(int i = 0; i < nums.length; i++) {
            twoSum(nums, i, mainList); // Don't overwrite mainList, just update it
        }

        return new ArrayList<>(mainList);
    }

    static void twoSum(int[] nums, int index, Set<List<Integer>> mainList) {
        Set<Integer> seen = new HashSet<>();
        int target = -nums[index];

        for (int j = 0; j < nums.length; j++) {
            if (j == index) continue; // Skip the same index

            int complement = target - nums[j];
            if (seen.contains(complement)) {
                List<Integer> triplet = Arrays.asList(nums[index], nums[j], complement);
                Collections.sort(triplet);
                mainList.add(triplet); // Set handles duplicates
            }
            seen.add(nums[j]);
        }
    }

    /**
     * -- O(n^2)
     * a + b = -c
     * a = -target - b
     */
    static List<List<Integer>> threeSumOptimized2(int[] nums) {
        Set<List<Integer>> mainList = new HashSet<>();

        for(int i = 0; i < nums.length; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i+1; j < nums.length; j++) {
                int c = - (nums[i] + nums[j]); // a+b = -c
                if (seen.contains(c)) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], c);
                    Collections.sort(triplet);
                    mainList.add(triplet); // Set handles duplicates
                }
                seen.add(nums[j]);
            }
        }
        return new ArrayList<>(mainList);
    }

    /**
     * 3 pointer
     * -- O(n^2)
     *
     * = 5 4 3 2 1
     * = 1 2 3 4 5
     *
     * i = 0:   left= 1 right: 5
     */

    static List<List<Integer>> threeSumOptimized3(int[] nums) {
        int left, right, i, n;
        List<List<Integer>> res = new ArrayList<>();
        n = nums.length;

        Arrays.sort(nums);

        for(i=0; i<n-2; i++) {
            if(i>0 && nums[i] == nums[i-1]) continue;

            left = i+1;
            right = n-1;

            while(left<right) {
                int sum = nums[left] + nums[right] + nums[i];
                if(sum == 0) {
                    res.add(new ArrayList<>(Arrays.asList(nums[left], nums[right], nums[i])));

                    while(left<right && nums[left] == nums[left+1]) left++;
                    while(left<right && nums[right] == nums[right-1]) right--;

                    left++;
                    right--;
                } else if(sum<0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return res;
    }




}
