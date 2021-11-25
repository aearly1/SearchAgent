package code;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        String grid = Matrix.genGrid();
//        String grid = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
        String grid0 = "5,5;2;3,4;1,2;0,3,1,4;2,3;4,4,0,2,0,2,4,4;2,2,91,2,4,62";

        String[] strats = new String[]{"BF", "DF", "ID", "UC", "GR1", "GR2", "AS1", "AS2"};

        System.out.println(Matrix.solve(grid0, strats[0], true));
    }
}
