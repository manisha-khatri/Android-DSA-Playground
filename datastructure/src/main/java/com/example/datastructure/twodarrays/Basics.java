package com.example.datastructure.twodarrays;

public class Basics {

    public static void main(String[] args) {
        int[][] ar = new int[2][3]; // 2 rows, 3 columns

        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[i].length; j++) {
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println();

        for(int row[]: matrix) {
            for(int val: row){
                System.out.print(val + ", ");
            }
            System.out.println();
        }

        System.out.println("Printing column wise top to bottom?? -->");

        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[i].length; j++) {
                System.out.print(matrix[j][i] + ", ");
            }
            System.out.println();
        }

        int n=2;



    }
}
