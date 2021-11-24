package code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Helpers {

    // ============================Additional-Methods===========================

    /**
     * @param s string of locations delimited by ,
     * @return an arraylist of locations
     */

    private static ArrayList<Location> stringToLocArray(String s) {
        String[] arrStr = s.split(",");
        ArrayList<Location> locs = new ArrayList<>();

        for (int i = 0; i < arrStr.length - 1; i += 2) {
            Location loc = new Location(Integer.parseInt(arrStr[i]), Integer.parseInt(arrStr[i + 1]));
            locs.add(loc);
        }

        return locs;
    }

    /**
     * Takes grid string and deduces the initial state
     *
     * @param grid initial state represented as a string
     */

    public static MatrixState parseGrid(String grid) {
        String[] segments = grid.split(";");

        // map dimensions
        String[] dimsStr = segments[0].split(",");
        Location dims = new Location(Integer.parseInt(dimsStr[0]), Integer.parseInt(dimsStr[1]));

        //maximum capacity
        int c = Integer.parseInt(segments[1]);

        //neo location
        String[] neolocStr = segments[2].split(",");
        Location neoLoc = new Location(Integer.parseInt(neolocStr[0]), Integer.parseInt(neolocStr[1]));

        //telephone booth location
        String[] tbStr = segments[3].split(",");
        Location tbLoc = new Location(Integer.parseInt(tbStr[0]), Integer.parseInt(tbStr[1]));

        //agent locations
        ArrayList<Location> agents = stringToLocArray(segments[4]);

        //pill locations
        ArrayList<Location> pills = stringToLocArray(segments[5]);

        //pad locations
        ArrayList<Location> padArr = stringToLocArray(segments[6]);
        HashMap<Location, Location> pads = new HashMap<>();

        for (int i = 0; i < padArr.size() - 1; i += 2) {
            pads.put(padArr.get(i), padArr.get(i + 1));
        }

        //hostages
        String[] hostageStr = segments[7].split(",");
        ArrayList<Hostage> hostages = new ArrayList<>();

        for (int i = 0; i < hostageStr.length - 2; i += 3) {
            Location hostageLoc = new Location(Integer.parseInt(hostageStr[i]), Integer.parseInt(hostageStr[i + 1]));
            Hostage hostage = new Hostage(hostageLoc, Integer.parseInt(hostageStr[i + 2]), false);
            hostages.add(hostage);
        }

        Neo neo = new Neo(neoLoc, 0, c);

        return new MatrixState(dims, neo, hostages, agents, pads, pills, tbLoc);
    }

    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public static boolean hostage98(Location neo, ArrayList<Hostage> hostages,MatrixOperator operator)
    {
    	Location temp;
    	switch(operator) {
    	  case UP:
    		  temp = new Location(neo.getX(),neo.getY()+1);
    	    break;
    	  case DOWN:
    		  temp = new Location(neo.getX(),neo.getY()-1);
    	    break;
    	  case LEFT:
    		  temp = new Location(neo.getX()-1,neo.getY());
      	    break;
    	  case RIGHT:
    		  temp = new Location(neo.getX()+1,neo.getY());
      	    break;
    	  default:
    		  temp=null;
    	}
    	for (Hostage h : hostages)
    	{
    		if(h.getLocation().equals(temp) && h.getDamage()==98)
    		{
    			return true;
    		}
    	}
    	return false;
    }
}
