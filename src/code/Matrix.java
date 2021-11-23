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
        	if(h.getLocation().equals(neo_loc) && h.getDamage()<100 && !h.isCarried() && s.getNeo().getCurrentCapacity()>0 && neo_loc.equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.CARRY))
        	{
        		operators.add(MatrixOperator.CARRY);
        		
        	}
        	
        	if(h.isCarried() && neo_loc.equals(s.getTeleBoothLoc())&& !operators.contains(MatrixOperator.DROP))
        	{
        		operators.add(MatrixOperator.DROP);
        		
        	}
        	
        	if(h.getDamage()>=100 && !h.isCarried() && h.getLocation().adjacent(neo_loc) && h.getLocation().equals(s.getTeleBoothLoc()) && !operators.contains(MatrixOperator.KILL))
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
//        for(Location l : fly_loc.)
//        {
//        	if (neo_loc.equals(l))
//        	{
//        		operators.add(MatrixOperator.FLY);
//        		break;
//        	}
//        }
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
		switch(a) {
		  case UP:
			new_loc=new Location(res.getNeo().getLocation().getX(),res.getNeo().getLocation().getY()+1);
		    res.getNeo().setLocation(new_loc);
		    for(Hostage h : res.getHostages())
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
		      for(Hostage h : res.getHostages())
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
		      for(Hostage h : res.getHostages())
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
		      for(Hostage h : res.getHostages())
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
		      for(Hostage h : res.getHostages())
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
				  for(Hostage h : res.getHostages())
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

		    		 for(Hostage h:res.getHostages())
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
		    for(Hostage h:res.getHostages())
		    {
		    	if(!h.isAlive() && h.getLocation().adjacent(res.getNeo().getLocation()) && !h.getLocation().equals(res.getTeleBoothLoc()))
		    	{
		    		res.removeHostage(h);
		    	}
		    }
		    break;
		  case FLY:
			  // the definition of pads needs to be discussed
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
