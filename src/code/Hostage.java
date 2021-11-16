package code;

import java.util.Objects;

/**
 * Helper class which contains all the attributes needed for a hostage.
 */

public class Hostage {
    /**
     * We need to track hostage location on the grid, hostage health, and
     * if they're currently being carried by neo.
     *
     * Location and damage together determine if a hostage is at the telephone
     * booth dead or alive (to avoid updating damage and to count them as saved hostages).
     * carried and damage together determine if a hostage should turn or die. If damage is
     * 100 and location is not the booth, that's an agent and must be killed.
     */

    private Location _location;
    private int _damage;
    private boolean _carried;

    public Hostage(Location location, int damage, boolean carried){
        _location = location;
        _damage = damage;
        _carried = carried;
    }

    // ==========================Getters-and-Setters==========================

    public Location getLocation() {
        return _location;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    public int getDamage() {
        return _damage;
    }

    public void setDamage(int damage) {
        _damage = damage;
    }

    public boolean isCarried() {
        return _carried;
    }

    public void setCarried(boolean carried) {
        _carried = carried;
    }

    // ================================Hashing=================================

    @Override
    public int hashCode() {
        return Objects.hash(_location, _damage, _carried);
    }
}
