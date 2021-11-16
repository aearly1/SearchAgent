package code;


import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class representing a state of the world specific to the matrix problem. Contains enough information to
 * know the effects of our actions, and determine goal configurations.
 */

public class MatrixState implements Serializable {
    /**
     * The dimensions of our grid. Needed for a complete transition model. REMOVE
     * IF PROVEN UNNECESSARY.
     */
    private Location _gridDims;

    /**
     * A state should track the locations of all objects in the world, as
     * long as they're relevant to the agent. In order, we track:
     * neoLoc: The location of Neo (player/agent).
     * teleBoothLoc: Location of the telephone booth.
     * hostageLocs: An array containing the locations of all hostages, turned or not.
     * killed turned hostages are deleted from the array.
     * agentLocs: Locations of all ALIVE agents. Dead agents are deleted from the array.
     * padLocs: Locations of all pads;
     * pillLocs: Locations of all UNTAKEN pills. Taken pills are deleted from the array
     * <p>
     * Additionally, we track the health of all hostages and neo, if a hostage is being carried, and the number of
     * hostages neo can currently carry.
     * <p>
     * We delete killed turned hostages from the hostages array, since we don't need them to know
     * the effects of our actions further down the tree.
     */

    private Neo _neo;
    private ArrayList<Hostage> _hostages;
    private ArrayList<Location> _agentLocs;
    private ArrayList<Location> _padLocs;
    private ArrayList<Location> _pillLocs;
    private Location _teleBoothLoc;

    /**
     * onstructor. Be careful when passing objects to make sure they are passed
     * by value and not reference
     */

    public MatrixState(Location gridDims, Neo neo, ArrayList<Hostage> hostages, ArrayList<Location> agentLocs,
                       ArrayList<Location> padLocs, ArrayList<Location> pillLocs, Location teleBoothLoc) {
        _gridDims = gridDims;
        _neo = neo;
        _hostages = hostages;
        _agentLocs = agentLocs;
        _padLocs = padLocs;
        _pillLocs = pillLocs;
        _teleBoothLoc = teleBoothLoc;
    }

    /**
     * Default constructor. everything is either 0 or empty.
     */

    public MatrixState() {
        _gridDims = new Location(0, 0);
        _neo = new Neo(new Location(0, 0), 0, 0);
        _hostages = new ArrayList<Hostage>();
        _agentLocs = new ArrayList<Location>();
        _padLocs = new ArrayList<Location>();
        _pillLocs = new ArrayList<Location>();
        _teleBoothLoc = new Location(0, 0);
    }

    // ==========================Getters-and-Setters==========================

    public Location getGridDims() {
        return _gridDims;
    }

    public void setGridDims(Location gridDims) {
        _gridDims = gridDims;
    }

    public Neo getNeo() {
        return _neo;
    }

    public void setNeo(Neo neo) {
        _neo = neo;
    }

    public ArrayList<Hostage> getHostages() {
        return _hostages;
    }

    public void setHostages(ArrayList<Hostage> hostages) {
        _hostages = hostages;
    }

    public ArrayList<Location> getAgentLocs() {
        return _agentLocs;
    }

    public void setAgentLocs(ArrayList<Location> agentLocs) {
        _agentLocs = agentLocs;
    }

    public ArrayList<Location> getPadLocs() {
        return _padLocs;
    }

    public void setPadLocs(ArrayList<Location> padLocs) {
        _padLocs = padLocs;
    }

    public ArrayList<Location> getPillLocs() {
        return _pillLocs;
    }

    public void setPillLocs(ArrayList<Location> pillLocs) {
        _pillLocs = pillLocs;
    }

    public Location getTeleBoothLoc() {
        return _teleBoothLoc;
    }

    public void setTeleBoothLoc(Location teleBoothLoc) {
        _teleBoothLoc = teleBoothLoc;
    }

    // ============================Additional-Methods===========================

    /**
     * This creates a deep clone of the current state. Use it when making new nodes.
     * 1- copy the parent state
     * 2- modify the copied state
     * 3- assign the state to the child node's state
     *
     * @return returns a deep clone of the state
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public MatrixState copy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(this);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(inputStream);
        MatrixState copied = (MatrixState) in.readObject();
        return copied;
    }

    //====================

    public void removeHostage(Hostage h) {
        _hostages.remove(h); // removes by reference
    }

    public void removeHostage(int indx) {
        _hostages.remove(indx);
    }

    public void addHostage(Hostage h) {
        _hostages.add(h);
    }

    //====================

    public void removeAgent(Location a) {
        _agentLocs.remove(a); // removes by reference
    }

    public void removeAgent(int indx) {
        _agentLocs.remove(indx);
    }

    public void addAgent(Location a) {
        _agentLocs.add(a);
    }

    //====================


    public void removePill(Location p) {
        _pillLocs.remove(p); // removes by reference
    }

    public void removePill(int indx) {
        _pillLocs.remove(indx);
    }

    public void addPill(Location p) {
        _pillLocs.add(p);
    }

    //====================

    public void removePad(Location p) {
        _padLocs.remove(p); // removes by reference
    }

    public void removePad(int indx) {
        _padLocs.remove(indx);
    }

    public void addPad(Location p) {
        _padLocs.add(p);
    }

    // ================================Hashing=================================

    /**
     * Array would hash to the same code only if their order is the same.
     * Don't mess up the order!
     */
    @Override
    public int hashCode() {
        return Objects.hash(_gridDims, _neo, _hostages, _agentLocs, _padLocs, _pillLocs, _teleBoothLoc);
    }
}
