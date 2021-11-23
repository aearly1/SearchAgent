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
        //TODO: Ali
        int neoDamage = s.getNeo().getDamage(); //get neo damage
        boolean damageLess = neoDamage<100; // check damage is less than 100
        Location neoLoc = s.getNeo().getLocation(); // get neo location
        Location telephoneBooth = s.getTeleBoothLoc(); //get booth location
        boolean nAtBooth = neoLoc.equals(telephoneBooth); // check if neo is at booth
        boolean hostagesAtBooth= true; 
        
        for(Hostage hostage: s.getHostages())
        {
        	Location hostageLoc = hostage.getLocation(); //get location of hostage
        	if(!hostageLoc.equals(telephoneBooth))//check is a hostage is at the booth 
        	{
        		hostagesAtBooth= false; break; //there exists a hostage that hasn't been saved
        	}
        }
        
        return damageLess && nAtBooth && hostagesAtBooth;
    }

    @Override
    public int[] stepCost(MatrixState s1, MatrixOperator a, MatrixState s2) {
        int[] cost = new int[]{0, 0};
        //TODO: Ali
        if(a!=(MatrixOperator.TAKE_PILL))//check if the agent didn't take pill (if he did no hostage will die)
        {
        	for(Hostage h: s1.getHostages())
        	{
        		if(h.getDamage()==1 || h.getDamage()==2)// check if the damage will become 0 in the new state
        		{
        			cost[0]++; // increment the number of killed hostages
        		}
        	}
        }
        int s1AgentsCount=s1.getAgentLocs().size(); //get number of ALIVE agents in old state(s1)
        int s2AgentsCount=s2.getAgentLocs().size(); //get number of ALIVE agents in new state(s2)
        cost[1] = s1AgentsCount-s2AgentsCount; // number of agents killed as result of doing action a

        return cost;
    }
    
    /**
     * The first A* heuristic assigns a cost value that corresponds
     * to the total number of unsaved (and ALIVE) hostages since
     * you will need at least one action to save each hostage.
     * Thus, this is guaranteed to be admissable.
     */
    public int ASHeuristic1(Node n)
    {
    	int cost = 0;
    	Location tBooth =((MatrixState) n.getState()).getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = ((MatrixState) n.getState()).getHostages(); //get hostages
    	
    	for(Hostage h: hostages)
    	{
    		if(!h.getLocation().equals(tBooth) && h.getDamage()<100) //check if this hostage is alive and unsaved
    		{
    			cost++; //increment cost since this is an unsaved hostage
    		}
    	}
    	return cost;
    }
    
    /**
     * The second A* heuristic assigns a cost value that corresponds
     * to the sum of the Manhattan distance between unsaved (and ALIVE) hostages 
     * and the telephone booth since you will need at least this number of moves
     * to carry hostage from initial location to booth. You cannot carry them in
     * less moves. The cost may be more than that if the agents does other moves
     * such as kill or take pill to save. Thus, this is guaranteed to be admissable.
     */
    public int ASHeuristic2(Node n)
    {
    	int cost = 0;
    	Location tBooth =((MatrixState) n.getState()).getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = ((MatrixState) n.getState()).getHostages(); //get hostages
    	
    	for(Hostage h: hostages)
    	{
    		if(!h.getLocation().equals(tBooth) && h.getDamage()<100) //check if this hostage is alive and unsaved
    		{
    			cost+=(h.getLocation().getX()-tBooth.getX())^2 + (h.getLocation().getY()-tBooth.getY())^2; //add Manhattan distance to total cost 
    		}
    	}
    	return cost;
    }
    
    public int GreedyHeuristic1(Node n)
    {
    	int cost = 0;
    	Location tBooth =((MatrixState) n.getState()).getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = ((MatrixState) n.getState()).getHostages(); //get hostages
    	
    	for(Hostage h: hostages)
    	{
    		if(!h.getLocation().equals(tBooth) && h.getDamage()<100) //check if this hostage is alive and unsaved
    		{
    			cost+=(h.getLocation().getX()-tBooth.getX())^2 + (h.getLocation().getY()-tBooth.getY())^2; //add Manhattan distance to total cost 
    		}
    	}
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
