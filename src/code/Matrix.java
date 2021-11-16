package code;

import java.io.IOException;
import java.util.ArrayList;

/**
 * class representing the matrix problem.
 */

public class Matrix extends SearchProblem<MatrixState, MatrixOperator, int[]> {

    /**
     * Initialize a matrix problem with a given initial state
     *
     * @param i an initial state
     */
    public Matrix(MatrixState i) {
        super();
        initialState = i;
    }

    /**
     * Default Constructor. needs grid assignment.
     */

    public Matrix() {
        super();
        initialState = null;
    }

    // ==========================Main-Methods==========================

    public static String genGrid() {
        StringBuilder grid = new StringBuilder();

        //TODO: Ahmed

        return grid.toString();
    }

    /**
     * takes the grid, strategy, and a boolean for visualization. Solves the grid
     * with the given strategy.
     *
     * @param grid      string representing the grid
     * @param strategy  search method can be: [BF, DF, ID, UC, GR1, GR2, AS1, AS2]
     * @param visualize if true prints a visual presentation of the grid as it
     *                  undergoes the different steps of the discovered solution
     * @return a String of the following format: plan;deaths;kills;nodes where
     */
    public static String solve(String grid, String strategy, boolean visualize) {
        MatrixState currentState = Helpers.parseGrid(grid); //initial state
        Matrix problem = new Matrix(currentState); //initialize problem

        //solve the problem
        Node<MatrixState, MatrixOperator> foundGoal = Search.searchProcedure(problem, strategy);

        //output string
        StringBuilder ret = new StringBuilder();

        //TODO: Ahmed | Get output string

        if(visualize){
            //TODO: Ahmed | visualize solution steps
        }

        return ret.toString();
    }

    @Override
    public ArrayList<MatrixOperator> actions(MatrixState s) {
        ArrayList<MatrixOperator> operators = new ArrayList<>();

        //TODO: Moataz

        return operators;
    }

    @Override
    public MatrixState result(MatrixState s, MatrixOperator a) throws IOException, ClassNotFoundException {
        MatrixState res = s.copy();

        //TODO: Moataz

        return res;
    }

    @Override
    public boolean isGoal(MatrixState s) {
        boolean goal = false;

        //TODO: Ali

        return goal;
    }

    @Override
    public int[] stepCost(MatrixState s1, MatrixOperator a, MatrixState s2) {
        int[] cost = new int[]{0, 0};

        //TODO: Ali

        return cost;
    }

    // ==========================Getters-and-Setters==========================

    public MatrixState getInitialState() {
        return initialState;
    }

    public void setInitialState(MatrixState i) {
        initialState = i;
    }
}
