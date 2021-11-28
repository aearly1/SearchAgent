package code;

import java.io.IOException;
import java.util.*;

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

        // The dimensions of the grid
        Location gridDims = new Location(Helpers.randInt(5, 15), Helpers.randInt(5, 15));
        grid.append(gridDims.getX()).append(",").append(gridDims.getY()).append(";");


        // Carry capacity
        int c = Helpers.randInt(1, 4);
        grid.append(c).append(";");

        // All possible locations;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < gridDims.getX(); i++) {
            for (int j = 0; j < gridDims.getY(); j++) {
                locations.add(new Location(i, j));
            }
        }

        // Shuffle locations
        Collections.shuffle(locations);

        // Add the shuffled locations to a queue
        Queue<Location> locQ = new LinkedList<>(locations);

        // Neo location
        Location neoLoc = locQ.poll();
        grid.append(neoLoc.getX()).append(",").append(neoLoc.getY()).append(";");

        // Telephone booth location
        Location tbLoc = locQ.poll();
        grid.append(tbLoc.getX()).append(",").append(tbLoc.getY()).append(";");

        // Number of hostages
        int hostageNum = Helpers.randInt(3, 10);

        // Number of pills
        int pillNum = Helpers.randInt(0, hostageNum);

        // Number of objects allowed on the grid (should be at least 3)
        int numAllowed = gridDims.getX() * gridDims.getY() - 2 - pillNum - hostageNum;

        // Agents
        int agentNum = Helpers.randInt(0, numAllowed);
        numAllowed = Math.max(0, numAllowed - agentNum);

        for (int i = 0; i < agentNum; i++) {
            Location agentLoc = locQ.poll();
            grid.append(agentLoc.getX()).append(",").append(agentLoc.getY());

            if (i < agentNum - 1)
                grid.append(",");
        }
        grid.append(";");

        // Pills
        for (int i = 0; i < pillNum; i++) {
            Location pillLoc = locQ.poll();
            grid.append(pillLoc.getX()).append(",").append(pillLoc.getY());

            if (i < pillNum - 1)
                grid.append(",");
        }
        grid.append(";");


        // Pads. Has to be an even number
        int padNum = Helpers.randInt(0, numAllowed);
        padNum -= padNum % 2;

        for (int i = 0; i < padNum / 2; i++) {
            Location padSrc = locQ.poll();
            Location padDst = locQ.poll();
            grid.append(padSrc.getX()).append(",").append(padSrc.getY()).append(",");
            grid.append(padDst.getX()).append(",").append(padDst.getY()).append(",");

            grid.append(padSrc.getX()).append(",").append(padSrc.getY()).append(",");
            grid.append(padDst.getX()).append(",").append(padDst.getY());

            if (i < (padNum / 2) - 1)
                grid.append(",");
        }
        grid.append(";");

        //Hostages
        for (int i = 0; i < hostageNum; i++) {
            Location hostageLoc = locQ.poll();
            int damage = Helpers.randInt(1, 99);

            grid.append(hostageLoc.getX()).append(",").append(hostageLoc.getY())
                    .append(",").append(damage);

            if (i < hostageNum - 1)
                grid.append(",");
        }

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
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public static String solve(String grid, String strategy, boolean visualize) throws IOException, ClassNotFoundException {

        MatrixState currentState = Helpers.parseGrid(grid); //initial state
        Matrix problem = new Matrix(currentState); //initialize problem

        //solve the problem
        Object[] searchRes = Search.searchProcedure(problem, strategy);
        Node<MatrixState, MatrixOperator> goalNode = null;
        int expandedNodes = (int) searchRes[1];
        if (searchRes[0] != null) goalNode = (Node<MatrixState, MatrixOperator>) searchRes[0];


        //output string
        String ret = Helpers.solutionStr(goalNode, expandedNodes);

        if (visualize) {
            //TODO: Ahmed | visualize solution steps
        }

        return ret;
    }

    @Override
    public ArrayList<MatrixOperator> actions(Node<MatrixState, MatrixOperator> n) {
        MatrixState s = n.getState();
        ArrayList<MatrixOperator> operators = new ArrayList<>();

        //TODO: Moataz
        if(s.getNeo().getDamage()>=100)return operators;
        Location neo_loc = s.getNeo().getLocation();
        Location dims = s.getGridDims();   //////////////// dims should be the location of the last cell in the grid (top right cell)
        ArrayList<Location> agents = s.getAgentLocs();  /// locations of the remaining agents in the matrix
        ArrayList<Hostage> hostages = s.getHostages(); //=== current hostages
        ArrayList<Location> pil_loc = s.getPillLocs(); //== pills locations
        HashMap<Location, Location> fly_loc = s.getPadLocs(); //== fly pads locations
        //=============movement=============


        if (n.getAction() != MatrixOperator.LEFT && neo_loc.getY() < dims.getY() - 1 && !agents.contains(new Location(neo_loc.getX(), neo_loc.getY() + 1))
                && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.RIGHT)) {
            operators.add(MatrixOperator.RIGHT);
        }
        if (n.getAction() != MatrixOperator.RIGHT && neo_loc.getY() > 0 && !agents.contains(new Location(neo_loc.getX(), neo_loc.getY() - 1))
                && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.LEFT)) {
            operators.add(MatrixOperator.LEFT);
        }
        if (n.getAction() != MatrixOperator.UP && neo_loc.getX() < dims.getX() - 1 && !agents.contains(new Location(neo_loc.getX() + 1, neo_loc.getY())) && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.DOWN)) {
            operators.add(MatrixOperator.DOWN);
        }
        if (n.getAction() != MatrixOperator.DOWN && neo_loc.getX() > 0 && !agents.contains(new Location(neo_loc.getX() - 1, neo_loc.getY())) && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.UP)) {
            operators.add(MatrixOperator.UP);
        }
        //================== hostage==============
        for (Hostage h : hostages) {
            if (h.getLocation().equals(neo_loc) && h.getDamage() < 100 && !h.isCarried() && s.getNeo().getCurrentCapacity() > 0 && !neo_loc.equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.CARRY)) {
                operators.add(MatrixOperator.CARRY);

            }

            if (h.isCarried() && neo_loc.equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.DROP)) {
                operators.add(MatrixOperator.DROP);

            }

            if (h.getDamage() >= 100 && !h.isCarried() && h.getLocation().adjacent(neo_loc) && !h.getLocation().equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.KILL)) {
                operators.add(MatrixOperator.KILL);
            }
        }

        //=================kill all agents in neighbouring cells===============
        for (Location l : agents) {
        	if (neo_loc.adjacent(l) && !operators.contains(MatrixOperator.KILL)) {
        		operators.add(MatrixOperator.KILL);
        		break;
        	}
        }
        //================ take a pill=======================================

        for (Location l : pil_loc) {
            if (neo_loc.equals(l)) {
                operators.add(MatrixOperator.TAKE_PILL);
                break;
            }
        }
        //================ fly =======================================

        if (fly_loc.containsKey(neo_loc) && n.getAction() != MatrixOperator.FLY) {
            operators.add(MatrixOperator.FLY);
        }
        return operators;
    }

    @Override
    public MatrixState result(MatrixState s, MatrixOperator a) throws IOException, ClassNotFoundException {
        //=====updates the state of the whole world
        //movement neo + carried hostages
        //carry // to know which hostage and update the hostage state
        //drop // to know which hostages and to remove them from the world + update c of neo
        //kill all agents in neighbouring cells to know which agents and reduce neo health //Player damage +20 after agent kill
        //take a pill to know which pill and update neo health + all alive carried hostages
        //fly to know which pad and update neo location with all the carried hostages
        MatrixState res = s.copy();
        Location new_loc;
        ArrayList<Hostage> host_loc = res.getHostages();
        switch (a) {
            case UP:
                new_loc = new Location(res.getNeo().getLocation().getX() - 1, res.getNeo().getLocation().getY());
                res.getNeo().setLocation(new_loc);
                for (Hostage h : host_loc) {
                    if (h.isCarried()) {
                        h.setLocation(new_loc);
                    }

                }
                break;
            case DOWN:
                new_loc = new Location(res.getNeo().getLocation().getX() + 1, res.getNeo().getLocation().getY());
                res.getNeo().setLocation(new_loc);
                for (Hostage h : host_loc) {
                    if (h.isCarried()) {
                        h.setLocation(new_loc);
                    }
                }
                break;
            case LEFT:
                new_loc = new Location(res.getNeo().getLocation().getX(), res.getNeo().getLocation().getY() - 1);
                res.getNeo().setLocation(new_loc);
                for (Hostage h : host_loc) {
                    if (h.isCarried()) {
                        h.setLocation(new_loc);
                    }
                }
                break;
            case RIGHT:
                new_loc = new Location(res.getNeo().getLocation().getX(), res.getNeo().getLocation().getY() + 1);
                res.getNeo().setLocation(new_loc);
                for (Hostage h : host_loc) {
                    if (h.isCarried()) {
                        h.setLocation(new_loc);
                    }
                }
                break;
            case CARRY:
                // i can carry a hostage if
                // he is alive *
                // he is same location as neo *
                // he is not at the TB *
                // neo can carry -> in actions
                // is not carried *
                for (Hostage h : host_loc) {
                    if (h.getLocation().equals(res.getNeo().getLocation()) && !h.getLocation().equals(res.getTeleBoothLoc()) && !h.isCarried() && h.getDamage() < 100) {
                        h.setCarried(true);
                        res.getNeo().decCurrentCapacity();
                        break;
                    }
                }
                break;
            case DROP:
                // i can dropp if
                // i a at the TB
                // i am carrying hostages
                //what will happen
                // all carried hostages will be dropped AND neo c will increment
                if (res.getNeo().getLocation().equals(res.getTeleBoothLoc())) {
                    for (Hostage h : host_loc) {
                        if (h.isCarried()) {
                            h.setCarried(false);
                            res.getNeo().incCurrentCapacity();

                        }
                    }
                }

                break;
            case TAKE_PILL:
                //i can take pill if
                // i am at the same location of the bill
                // what will happen
                // my health AND the Health of all alive hostages will increment
                // remove the pill from the world
                ArrayList<Location> pillLocs = res.getPillLocs();
                for (int i = pillLocs.size() - 1; i >= 0; i--) {
                    Location l = pillLocs.get(i);
                    if (l.equals(res.getNeo().getLocation())) {
                    	int newDamageN= res.getNeo().getDamage() - 20 <0?0:res.getNeo().getDamage() - 20;
                        res.getNeo().setDamage(newDamageN);//should not be below zero

                        for (Hostage h : host_loc) {
                            if (!h.getLocation().equals(res.getTeleBoothLoc()) && h.isAlive()) {
                            	int newDamageH= h.getDamage() - 20 <0?0:h.getDamage() - 20;
                                h.setDamage(newDamageH-2);

                            }
                        }
                        res.removePill(l);
                    }
                }
                break;

            case KILL:
                //i can kill a someone if
                //he is an adjacent agent
                //he is adjacent turned hostage
                //FOR ALL ADJECENT AGENTS AND TURNED HOSTAGEE  remove these agents+hostages form the game and update neo health
                res.getNeo().setDamage(res.getNeo().getDamage() + 20);

                ArrayList<Location> agentLocs = res.getAgentLocs();
                for (int i = agentLocs.size() - 1; i >= 0; i--) {
                    Location l = agentLocs.get(i);

                    if (l.adjacent(res.getNeo().getLocation())) {
                        res.removeAgent(l);
                    }
                }
//			  for(Location l : res.getAgentLocs())
//			    {
//			    	if(l.adjacent(res.getNeo().getLocation()))
//			    	{
//			    		res.removeAgent(l);
//			    	}
//			    }

                ArrayList<Hostage> hostages = res.getHostages();
                for (int i = hostages.size() - 1; i >= 0; i--) {
                    Hostage h = hostages.get(i);
                    if (!h.isAlive() && h.getLocation().adjacent(res.getNeo().getLocation()) && !h.getLocation().equals(res.getTeleBoothLoc())) {
                        res.removeHostage(h);
                    }
                }
                break;
            case FLY:
                HashMap<Location, Location> pads = res.getPadLocs();
                if (pads.containsKey(res.getNeo().getLocation())) {
                    Location loc = pads.get(res.getNeo().getLocation());
                    res.getNeo().setLocation(loc);
                    for (Hostage h : host_loc) {
                        if (h.isCarried()) {
                            h.setLocation(loc);
                        }
                    }

                }
                break;
            default:
        }

        //================================ updating the world =============================
        // update the damage of a hostage iff
        // he is not at the TB
        // he is a not a turned hostage
        // not dead and carried by neo
        //Equivalent to
        // all alive hostages that are not at the TB
        for (Hostage h : res.getHostages())//Hostage damage +2 after action
        {
            if (!h.getLocation().equals(res.getTeleBoothLoc()) && h.isAlive() || (h.getLocation().equals(res.getTeleBoothLoc()) && h.isAlive() && h.isCarried())) {
                h.setDamage(h.getDamage() + 2);
            }

        }


        return res;
    }

    @Override
    public boolean isGoal(MatrixState s) {
        //TODO: Ali
        int neoDamage = s.getNeo().getDamage(); //get neo damage
        boolean damageLess = neoDamage < 100; // check damage is less than 100
        Location neoLoc = s.getNeo().getLocation(); // get neo location
        Location telephoneBooth = s.getTeleBoothLoc(); //get booth location
        boolean nAtBooth = neoLoc.equals(telephoneBooth); // check if neo is at booth
        boolean hostagesAtBooth = true;

        for (Hostage hostage : s.getHostages()) {
            Location hostageLoc = hostage.getLocation(); //get location of hostage
            if (!(hostageLoc.equals(telephoneBooth)&& !hostage.isCarried()))//check is a hostage is at the booth
            {
                hostagesAtBooth = false;
                break; //there exists a hostage that hasn't been saved
            }
        }

        return damageLess && nAtBooth && hostagesAtBooth;
    }

    @Override
    public int[] stepCost(MatrixState s1, MatrixOperator a, MatrixState s2) {
        int[] cost = new int[]{0, 0};
        //TODO: Ali
        if (a != (MatrixOperator.TAKE_PILL))//check if the agent didn't take pill (if he did no hostage will die)
        {
            for (Hostage h : s1.getHostages()) {
                if ((h.getDamage() == 98 || h.getDamage() == 99) && (a != MatrixOperator.DROP || !h.isCarried()))// check if the damage will become 100 in the new state
                {
                    cost[0]++; // increment the number of killed hostages
                }
            }
        }
        int killedHostages=0;
        if(a==MatrixOperator.KILL)
        {
        	for (Hostage h : s1.getHostages()) {
                if (s1.getNeo().getLocation().adjacent(h.getLocation()) && !h.isAlive() && (!h.getLocation().equals(s1.getTeleBoothLoc())))// check if the damage will become 100 in the new state
                {
                    killedHostages++; // increment the number of killed hostages
                }
            }
        }
        int s1AgentsCount = s1.getAgentLocs().size(); //get number of ALIVE agents in old state(s1)
        int s2AgentsCount = s2.getAgentLocs().size(); //get number of ALIVE agents in new state(s2)
        cost[1] = s1AgentsCount - s2AgentsCount + killedHostages; // number of agents killed as result of doing action a
        return cost;
    }

    /**
     * The first A* heuristic assigns a cost value that corresponds
     * to the total number of unsaved (and ALIVE) hostages since
     * you will need at leaost ne action to save each hostage.
     * Thus, this is guaranteed to be admissable.
     */
    public int ASHeuristic1(MatrixState s) {
    	int cost = 0;
        Location tBooth = s.getTeleBoothLoc(); //get telephone booth location
        ArrayList<Hostage> hostages = s.getHostages(); //get hostages
        int nCarryOp = 0; //number of carry operations needed to save all unsaved and alive hostages
        int minKillOp = 0; //minimum number of kill operations required (to kill the hostages turned into agents)
        for (Hostage h : hostages) {
            if (!h.getLocation().equals(tBooth) && h.getDamage() < 100) //check if this hostage is alive and unsaved
            {
                nCarryOp++; //hostage requires a carry operation
            }
            if (!h.getLocation().equals(tBooth) && h.getDamage() == 100) //check if this hostage has turned into agent
            {
                minKillOp++; //must kill agent
            }
        }
        minKillOp = minKillOp / 4; //since at best you will kill 4 agents at once (one at each adjacent cell)
        cost = nCarryOp + minKillOp;
        return cost;
    }


    public int GreedHeuristic2(MatrixState s) {
        ArrayList<Hostage> hostages = s.getHostages();
        Location neo_loc = s.getNeo().getLocation();
        Location TB = s.getTeleBoothLoc();
        return gh2helper(hostages, neo_loc, TB);//pass by value
    }

    private int gh2helper(ArrayList<Hostage> hostages, Location neo_loc, Location TB) {
        if (hostages.size() == 0) {
//    		Location last=hostages.get(0).getLocation();
            int manhatten = Math.abs(neo_loc.getX() - TB.getX()) + Math.abs(neo_loc.getY() - TB.getY());

            return manhatten;
        }

        if (hostages.size() == 1) {
            Location last = hostages.get(0).getLocation();
            int manhatten = Math.abs(neo_loc.getX() - last.getX()) + Math.abs(neo_loc.getY() - last.getY()) + Math.abs(last.getX() - TB.getX()) + Math.abs(last.getY() - TB.getY());

            return manhatten;
        } else {
            int indx = 0;
            int min_distance = Integer.MAX_VALUE;
            for (int i = 0; i < hostages.size(); i++) {
                Hostage h = hostages.get(i);
                int manhatten = Math.abs(neo_loc.getX() - h.getLocation().getX()) + Math.abs(neo_loc.getY() - h.getLocation().getY());
                if (manhatten < min_distance) {
                    min_distance = manhatten;
                    indx = i;
                }
            }
            neo_loc = hostages.get(indx).getLocation();
            hostages.remove(indx);
            return min_distance + gh2helper(hostages, neo_loc, TB);

        }
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
    public float ASHeuristic2(MatrixState s) {
        float cost = 0;
        Location tBooth = s.getTeleBoothLoc(); //get telephone booth location
        ArrayList<Hostage> hostages = s.getHostages(); //get hostages
        //int nCarryOp = 0; //number of carry operations needed to save all unsaved and alive hostages
<<<<<<< HEAD
        float minKillOp = 0; //minimum number of kill operations required (to kill the hostages turned into agents)
=======
        int minKillOp = 0; //minimum number of kill operations required (to kill the hostages turned into agents)
>>>>>>> a96fde327073c3a394c75bf042991c68a1c4747b
        for (Hostage h : hostages) {
            /*if (!h.getLocation().equals(tBooth) && h.getDamage() < 100) //check if this hostage is alive and unsaved
            {
                nCarryOp++; //hostage requires a carry operation
            }*/
            if (!h.getLocation().equals(tBooth) && h.getDamage() == 100) //check if this hostage has turned into agent
            {
                minKillOp++; //must kill agent
            }
        }
<<<<<<< HEAD
        minKillOp = minKillOp / (float)4.0; //since at best you will kill 4 agents at once (one at each adjacent cell)
=======
        minKillOp = minKillOp / 4; //since at best you will kill 4 agents at once (one at each adjacent cell)
>>>>>>> a96fde327073c3a394c75bf042991c68a1c4747b
        /*int minTakePillOp = 0; //minimum number of pills required to be taken in order for neo to remain alive
        int neoDamage = s.getNeo().getDamage() + minKillOp * 20;
        if (neoDamage >= 100) {
            neoDamage -= 100;
            minTakePillOp = neoDamage / 20 + 1; //calculate the minimum dumber of pills needed
<<<<<<< HEAD
        }*/
        //int neoFullCap = s.getNeo().getOriginalCapacity(); // get max number of hostages that Neo can carry
        //int minDropOp = nCarryOp / neoFullCap + nCarryOp % neoFullCap == 0 ? 0 : 1; //calculate minimum number of drop operations
        // = nCarryOp + minDropOp + minKillOp + minTakePillOp; // the total cost is the sum of the 4 individual estimated costs
=======
        }
        int neoFullCap = s.getNeo().getOriginalCapacity(); // get max number of hostages that Neo can carry
        int minDropOp = nCarryOp / neoFullCap + nCarryOp % neoFullCap == 0 ? 0 : 1; //calculate minimum number of drop operations
        cost = nCarryOp + minDropOp + minKillOp + minTakePillOp; // the total cost is the sum of the 4 individual estimated costs*/
>>>>>>> a96fde327073c3a394c75bf042991c68a1c4747b
        cost = minKillOp;
        return cost;
    }

    public int GreedHeuristic1(MatrixState s) {
        int cost = 0;
        Location tBooth = s.getTeleBoothLoc(); //get telephone booth location
        ArrayList<Hostage> hostages = s.getHostages(); //get hostages

        for (Hostage h : hostages) {
            if (!h.getLocation().equals(tBooth) && h.getDamage() < 100) //check if this hostage is alive and unsaved
            {
                cost += Math.abs(h.getLocation().getX() - tBooth.getX()) + Math.abs(h.getLocation().getY() - tBooth.getY()); //add Manhattan distance to total cost
            }
        }
        return cost;
    }
    
    public int GreedyHeuristic3(MatrixState s)
    {
    	int cost = 0;
        Location tBooth = s.getTeleBoothLoc(); //get telephone booth location
        ArrayList<Hostage> hostages = s.getHostages(); //get hostages
        int nCarryOp = 0; //number of carry operations needed to save all unsaved and alive hostages
        int minKillOp = 0; //minimum number of kill operations required (to kill the hostages turned into agents)
        for (Hostage h : hostages) {
            /*if (!h.getLocation().equals(tBooth) && h.getDamage() < 100) //check if this hostage is alive and unsaved
            {
                nCarryOp++; //hostage requires a carry operation
            }*/
            if (!h.getLocation().equals(tBooth) && h.getDamage() == 100) //check if this hostage has turned into agent
            {
                minKillOp++; //must kill agent
            }
        }
        minKillOp = minKillOp / 4; //since at best you will kill 4 agents at once (one at each adjacent cell)
        int minTakePillOp = 0; //minimum number of pills required to be taken in order for neo to remain alive
        int neoDamage = s.getNeo().getDamage() + minKillOp * 20;
        if (neoDamage >= 100) {
            neoDamage -= 100;
            minTakePillOp = neoDamage / 20 + 1; //calculate the minimum dumber of pills needed
        }
        cost = minTakePillOp;
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
