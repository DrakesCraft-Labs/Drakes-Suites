package com.drakescraft.suites.utility.portalgun;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class PortalData {

    public Location location;
    public BlockFace blockFace;
    public Color color;
    public boolean second;

    public boolean intersects(Location loc) {
        if (loc == null || location == null || !location.getWorld().equals(loc.getWorld())) {
            return false;
        }
        Location ploc = location.clone();
        if (blockFace == BlockFace.DOWN) {
            loc = loc.clone().add(0, 2, 0);
            ploc = ploc.add(0.5, 0, 0.5);
            return loc.distance(ploc) < 0.4;
        }
        if (blockFace == BlockFace.UP) {
            loc = loc.clone().add(0, -1, 0);
            ploc = ploc.add(0.5, 0, 0.5);
            return loc.distance(ploc) < 0.4;
        }
        if (blockFace == BlockFace.NORTH) {
            ploc = ploc.add(0.5, 0, 0);
            return loc.distance(ploc) < 0.4;
        }
        if (blockFace == BlockFace.SOUTH) {
            ploc = ploc.clone().add(0.5, 0, 1);
            return loc.distance(ploc) < 0.4;
        }
        if (blockFace == BlockFace.EAST) {
            ploc = ploc.clone().add(1, 0, 0.5);
            return loc.distance(ploc) < 0.4;
        }
        if (blockFace == BlockFace.WEST) {
            ploc = ploc.clone().add(0, 0, 0.5);
            return loc.distance(ploc) < 0.4;
        }
        return false;
    }
}
