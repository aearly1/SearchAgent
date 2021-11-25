package code;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String grid = Matrix.genGrid();
        String[] strats = new String[]{"BF", "DF", "ID", "UC", "GR1", "GR2", "AS1", "AS2"};

        Matrix.solve(grid, strats[3], true);
    }
}
