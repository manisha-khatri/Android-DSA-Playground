package com.example.ds.interview.agoda;

/**
 *  Let me know if anything. You are managing a printing press that needs to print a sequence of books.
 *  Each book has a certain number of pages given by the array pages, and the books must be printed in order,
 *  no skipping or reordering. Each day, the press can print a contiguous sequence of books as long as the total
 *  number of pages printed that day does not exceed a daily limit. A single book must be printed on the same day,
 *  which means you are not allowed to split a book to be printed on two different days. Given pages an integer d,
 *  number of days, and an integer daily limit, maximum number of total pages that can be printed in a day, determine
 *  if it is possible to print all books with d days without exceeding daily limit on any day. Thank you for watching.
 *
 *
 * int books[] = {100, 200, 300, 400}
 * maxLimit = 500
 * days = 2
 * result = false
 * day 1: 100+200
 * day 2: 300
 * day 3: 400
 * day 3>2
 *
 * int books[] = {100, 200, 300, 400}
 *  maxLimit = 600
 *  days = 2
 *  result = true
 *  day 1: 100+200+300 = 600
 *  day 2: 400
 *  2 == 2
 *
 */
public class BookAllocation {

    static boolean bookAllocation(int pages[], int days, int maxLimit) {
        int collectedPages = 0;
        int spendDays = 1;
        for(int i=0; i<pages.length; i++) {
            if(pages[i]>maxLimit) return false;

            if(collectedPages+pages[i]<=maxLimit) {
                collectedPages+=pages[i];
            } else {
                collectedPages = pages[i];
                spendDays++;
            }
            if(spendDays>days) return false;
        }

        return days==spendDays;
    }


    public static void main(String[] args) {
        int[] pages = {100, 200, 300, 400};
        int maxLimit = 500;
        int day = 2;

        System.out.println(bookAllocation(pages, day, maxLimit));
    }
}
