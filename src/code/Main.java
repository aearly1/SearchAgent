package code;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        String grid = Matrix.genGrid();
        String grid = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
        String[] strats = new String[]{"BF", "DF", "ID", "UC", "GR1", "GR2", "AS1", "AS2"};

        System.out.println(Matrix.solve(grid, strats[3], true));
    }
}
