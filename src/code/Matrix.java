package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    public static String solve(String grid, String strategy, boolean visualize) throws IOException, ClassNotFoundException {
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
    public int ASHeuristic1(MatrixState s)
    {
    	int cost = 0;
    	Location tBooth =s.getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = s.getHostages(); //get hostages
    	
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
     * to a relaxed problem with less restrictions on the operators.
     * This is done by ignoring the cost of movements (since the cost
     * of movement is difficult to estimate as it depends of the path 
     * that the agent chooses to take and where a pad is located. In the
     * relaxed problem, only the cost of carry, drop, kill agents and
     * take pills is considered. The cost of the carry is estimated 
     * to be the number of unsaved hostages since each hostage is
     * located at a different cell (thus it is admissable). The cost
     * of the drop operation is estimated to be the number of drop 
     * operations required if Neo is currently not carrying any hostage
     * and he decides to carry the max possible hostages before dropping
     * them at booth (i.e ceil(number of unsaved/maxCapacity)). It can
     * never be less than that since Neo can't carry more than his max capacity
     * and thus it's admissable. The cost of kill operation is considered 
     * to be the number of agents that Neo is REQUIRED to kill (i.e those
     * who were initially hostages) divided by 4. We divide by 4 since at best
     * there will be a hostage turned agent at each adjacent cell. Thus, with
     * each kill operation we are executing 4 of the REQUIRED kills at once.
     * It can never be less than that since you cant kill more than 4 hostages
     * at a time (since no two hosatge are in the same cell initially). Finally,
     * the number of pills taken is estimated as the number of REQUIRED pills 
     * that neo must take to stay alive. Each time Neo executes a REQUIRED kill
     * operation, damage is increased by 20. Thus, Neo must take at least enough
     * pills to maintain the damage below 100 after executing the mandatory kill
     * operation. The total cost is the SUM of the four mentioned costs (i.e 
     * total cost = cost of carry + cost of drop + cost of killing hostages + 
     * cost of taking pills).
     */
    public int ASHeuristic2(MatrixState s)
    {
    	int cost = 0;
    	Location tBooth =s.getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = s.getHostages(); //get hostages
    	int nCarryOp = 0; //number of carry operations needed to save all unsaved and alive hostages
    	int minKillOp = 0; //minimum number of kill operations required (to kill the hostages turned into agents)
    	for(Hostage h: hostages)
    	{
    		if(!h.getLocation().equals(tBooth) && h.getDamage()<100) //check if this hostage is alive and unsaved
    		{
    			nCarryOp++; //hostage requires a carry operation
    		}
    		if(!h.getLocation().equals(tBooth) && h.getDamage()==100) //check if this hostage has turned into agent
    		{
    			minKillOp++; //must kill agent
    		}
    	}
    	minKillOp = minKillOp/4; //since at best you will kill 4 agents at once (one at each adjacent cell)
    	int minTakePillOp = 0; //minimum number of pills required to be taken in order for neo to remain alive
    	int neoDamage = s.getNeo().getDamage() + minKillOp*20;
    	if(neoDamage>=100)
    	{
    		neoDamage-=100;
    		minTakePillOp = neoDamage/20 + 1; //calculate the minimum dumber of pills needed
    	}
    	int neoFullCap = s.getNeo().getOriginalCapacity(); // get max number of hostages that Neo can carry
    	int minDropOp = nCarryOp/neoFullCap + nCarryOp%neoFullCap==0?0:1; //calculate minimum number of drop operations
    	cost = nCarryOp + minDropOp + minKillOp +minTakePillOp; // the total cost is the sum of the 4 individual estimated costs
    	return cost;
    }
    
    public int GreedHeuristic1(MatrixState s)
    {
    	int cost = 0;
    	Location tBooth =s.getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = s.getHostages(); //get hostages
    	
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
