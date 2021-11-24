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
     * @throws IOException 
     * @throws ClassNotFoundException 
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
        Location neo_loc=s.getNeo().getLocation();
        Location dims=s.getGridDims();   //////////////// dims should be the location of the last cell in the grid (top right cell)
        ArrayList<Location> agents=s.getAgentLocs();  /// locations of the remaining agents in the matrix
        ArrayList<Hostage> hostages = s.getHostages(); //=== current hostages
        ArrayList <Location>pil_loc = s.getPillLocs(); //== pills locations
        HashMap<Location, Location> fly_loc = s.getPadLocs(); //== fly pads locations
        //=============movement=============
        
        
        if(neo_loc.getX()<dims.getX() && !agents.contains(new Location(neo_loc.getX()+1,neo_loc.getY())) && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.RIGHT))
        {
        	operators.add(MatrixOperator.RIGHT);
        }
        if(neo_loc.getX()>1 && !agents.contains(new Location(neo_loc.getX()-1,neo_loc.getY()))  && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.LEFT))
        {
        	operators.add(MatrixOperator.LEFT);
        }
        if(neo_loc.getY()<dims.getY() && !agents.contains(new Location(neo_loc.getX(),neo_loc.getY()+1))  && !Helpers.hostage98(neo_loc, hostages, MatrixOperator.UP))
        {
        	operators.add(MatrixOperator.UP);
        }
        if(neo_loc.getY()>1 && !agents.contains(new Location(neo_loc.getX(),neo_loc.getY()-1))  && !Helpers.hostage98(neo_loc, hostages , MatrixOperator.DOWN))
        {
        	operators.add(MatrixOperator.DOWN);
        }
        //================== hostage==============
        for (Hostage h : hostages)
        {
        	if(h.getLocation().equals(neo_loc) && h.getDamage()<100 && !h.isCarried() && s.getNeo().getCurrentCapacity()>0 && !neo_loc.equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.CARRY))
        	{
        		operators.add(MatrixOperator.CARRY);
        		
        	}
        	
        	if(h.isCarried() && neo_loc.equals(s.getTeleBoothLoc())&& !operators.contains(MatrixOperator.DROP))
        	{
        		operators.add(MatrixOperator.DROP);
        		
        	}
        	
        	if(h.getDamage()>=100 && !h.isCarried() && h.getLocation().adjacent(neo_loc) && !h.getLocation().equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.KILL))
        	{
        		operators.add(MatrixOperator.KILL);
        		
        	}
        }

        //=================kill all agents in neighbouring cells===============
        for (Location l:agents)
        {
        	if(neo_loc.adjacent(l) && !operators.contains(MatrixOperator.KILL))
        	{
        		operators.add(MatrixOperator.KILL);
        		break;
        	}
        }
        //================ take a pill=======================================
        
        for(Location l : pil_loc)
        {
        	if(neo_loc.equals(l))
        	{
        		operators.add(MatrixOperator.TAKE_PILL);
        		break;
        	}
        }
        //================ fly =======================================
        
        if (fly_loc.containsKey(neo_loc))
        {
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
		ArrayList<Hostage> host_loc=res.getHostages();
		switch(a) {
		  case UP:
			new_loc=new Location(res.getNeo().getLocation().getX(),res.getNeo().getLocation().getY()+1);
		    res.getNeo().setLocation(new_loc);
		    for(Hostage h : host_loc)
			{
		    	if(h.isCarried())
		    	{
		    		h.setLocation(new_loc);
		    	}
				
			}
		    break;
		  case DOWN:
			  new_loc=new Location(res.getNeo().getLocation().getX(),res.getNeo().getLocation().getY()-1);
			  res.getNeo().setLocation(new_loc);
		      for(Hostage h : host_loc)
			  {
		    	  if(h.isCarried())
			    	{
			    		h.setLocation(new_loc);
			    	}
			  }
		    break;
		  case LEFT:
			  new_loc=new Location(res.getNeo().getLocation().getX()-1,res.getNeo().getLocation().getY());
			  res.getNeo().setLocation(new_loc);
		      for(Hostage h : host_loc)
			  {
		    	  if(h.isCarried())
			    	{
			    		h.setLocation(new_loc);
			    	}
			  }
		    break;
		  case RIGHT:
			  new_loc=new Location(res.getNeo().getLocation().getX()+1,res.getNeo().getLocation().getY());
			  res.getNeo().setLocation(new_loc);
		      for(Hostage h : host_loc)
			  {
		    	  if(h.isCarried())
			    	{
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
		      for(Hostage h : host_loc)
			  {
				  if(h.getLocation().equals(res.getNeo().getLocation()) && !h.getLocation().equals(res.getTeleBoothLoc()) && !h.isCarried() && h.getDamage()<100)
				  {
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
			  if(res.getNeo().getLocation().equals(res.getTeleBoothLoc()))
			  {
				  for(Hostage h : host_loc)
				  {
					  if(h.isCarried())
					  {
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
		     for(Location l : res.getPillLocs())
		     {
		    	 if(l.equals(res.getNeo().getLocation()))
		    	 {
		    		 res.getNeo().setDamage(res.getNeo().getDamage()-20);//should not be below zero

		    		 for(Hostage h:host_loc)
		    		 {
		    			 if(!h.getLocation().equals(res.getTeleBoothLoc()) && h.isAlive())
		    			 {
		    					 h.setDamage(h.getDamage()-22);
		    				
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
			  res.getNeo().setDamage(res.getNeo().getDamage()+20);
			  for(Location l : res.getAgentLocs())
			    {
			    	if(l.adjacent(res.getNeo().getLocation()))
			    	{
			    		res.removeAgent(l);
			    	}
			    }
			    for(Hostage h:host_loc)
			    {
			    	if(!h.isAlive() && h.getLocation().adjacent(res.getNeo().getLocation()) && !h.getLocation().equals(res.getTeleBoothLoc()))
			    	{
			    		res.removeHostage(h);
			    	}
			    }
			    break;
		  case FLY:
			  HashMap<Location, Location> pads= res.getPadLocs();
			  if(pads.containsKey(res.getNeo().getLocation()))
			  {
				  Location loc=pads.get(res.getNeo().getLocation());
				  res.getNeo().setLocation(loc);
				  for(Hostage h: host_loc)
				  {
					  if(h.isCarried())
					  {
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
		for(Hostage h : res.getHostages())//Hostage damage +2 after action
		{
			if(!h.getLocation().equals(res.getTeleBoothLoc()) && h.isAlive())
			{
				h.setDamage(h.getDamage()+2);
			}
			
		}
       

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
    

    public float GreedHeuristic2(MatrixState s)
    {
    	ArrayList<Hostage> hostages= s.getHostages();
    	Location neo_loc=s.getNeo().getLocation();
    	Location TB=s.getTeleBoothLoc();
    	return gh2helper(hostages,neo_loc,TB);//pass by value
    }
    private  float gh2helper(ArrayList<Hostage> hostages,Location neo_loc,Location TB)
    {
    	if(hostages.size()==1)
    	{
    		Location last=hostages.get(0).getLocation();
    		int manhatten=(neo_loc.getX()-last.getX())^2+(neo_loc.getY()-last.getY())^2  +  (last.getX()-TB.getX())^2+(last.getY()-TB.getY())^2;
    		
    		return manhatten;
    	}
    	else
    	{
    		int indx=0;
        	int min_distance=Integer.MAX_VALUE;
        	for(int i=0;i<hostages.size();i++)
        	{
        		Hostage h=hostages.get(i);
        		int manhatten=(neo_loc.getX()-h.getLocation().getX())^2+(neo_loc.getY()-h.getLocation().getY())^2;
        		if(manhatten<min_distance)
        		{
        			min_distance=manhatten;
        			indx=i;
        		}
        	}
        	neo_loc=hostages.get(indx).getLocation();
        	hostages.remove(indx);
        	return min_distance + gh2helper(hostages,neo_loc,TB);
        	
    	}
    }
=======
    /**
     * The second A* heuristic assigns a cost value that corresponds
     * to the sum of the MINIMUM required steps to carry EACH unsaved 
     * and alive hostage from hostage location to telephone booth. The
     * minimum number of steps to carry a certain hostage will either
     * be 1) the Manhattan distance from the hostage location to telephone
     * booth OR 2) the Manhattan distance from the closest pad to the booth
     * to the actual booth. We will take the minimum of the two values. If 
     * the former value is the minimum this would suggest that carrying the 
     * hostage without using any pad is cheaper. The latter would suggest 
     * that IF WE CAN FIND A CHEAP SEQUENCE OF STEPS TO TAKE US TO THE CLOSEST
     * PAD TO BOOTH, then it would be cheaper to find a way to the pad and then
     * move from the pad to the telephone booth. This heuristic is admissable
     * for 2 reasons: 1) we only consider movement actions ignoring all others
     * and thus the cost of actions could be greater than the calculated cost
     * 2) We only calculate the distance from the closest pad to the booth.
     * We do not consider the cost of going from hostage location, (possibly)
     * teleporting a number of times and reaching the pad. This cost could be 
     * tremendous making the actual cost cheaper.  
     */
    public int ASHeuristic2(MatrixState s)
    {
    	int cost = 0;
    	Location tBooth =s.getTeleBoothLoc(); //get telephone booth location
    	ArrayList<Hostage> hostages = s.getHostages(); //get hostages
        HashMap<Location, Location> padLocations = s.getPadLocs();
    	int closestPadDist=Integer.MAX_VALUE; //the Manhattan distance between closest pad and telephone booth
    	
    	//calculate the Manhattan distance between closest pad and telephone booth
    	for(Location pad: padLocations.keySet())
    	{
    		int manhDist = (pad.getX()-tBooth.getX())^2 + (pad.getY()-tBooth.getY())^2; // calculate Manhattan distance between pad and booth 
    		closestPadDist = Math.min(closestPadDist, manhDist); 
    	}
    	
    	for(Hostage h: hostages)
    	{
    		if(!h.getLocation().equals(tBooth) && h.getDamage()<100) //check if this hostage is alive and unsaved
    		{
    			int manhDist=(h.getLocation().getX()-tBooth.getX())^2 + (h.getLocation().getY()-tBooth.getY())^2; //calculate Manhattan distance to between hostage and telephone booth
    			cost+= Math.min(manhDist,closestPadDist); 
    		}
    	}
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
