package com.example.ds.interview.kredivo;

public class CheckIfItIsAStraightLine {
    public boolean checkStraightLine(int[][] coordinates) {
        /**
         dy      (y-y1)
         --   =   -----  --> Slope
         dx      (x-x1)

         if dx == 0, so to avoid that

         => dy * (x-x1) = dx * (y-y1)
         */

        if(coordinates.length == 2) return true;

        int dx = coordinates[1][0] - coordinates[0][0];
        int dy = coordinates[1][1] - coordinates[0][1];

        for(int i=2; i<coordinates.length; i++) {
            int x = dx * (coordinates[i][1] - coordinates[0][1]);
            int y = dy * (coordinates[i][0] - coordinates[0][0]);
            if(x!=y) return false;
        }

        return true;
    }
}
