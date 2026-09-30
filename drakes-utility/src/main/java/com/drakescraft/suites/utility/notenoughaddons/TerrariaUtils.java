package com.drakescraft.suites.utility.notenoughaddons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public final class TerrariaUtils {

    private TerrariaUtils() {
    }

    public static Vector fastNormalize(Vector v) {
        float length = fastLength(v);
        v.multiply(length);
        return v;
    }

    public static float fastLength(Vector v) {
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        return fastSqrt(x * x + y * y + z * z);
    }

    public static float fastSqrt(double doubleNum) {
        int i;
        float x2, y;
        float threehalfs = 1.5F;
        float num = (float) doubleNum;

        x2 = num * 0.5F;
        y = num;
        i = Float.floatToIntBits(y);
        i = 0x5f3759df - (i >> 1);
        y = Float.intBitsToFloat(i);
        y = y * (threehalfs - (x2 * y * y));

        return y;
    }

    public static Vector knockback(Location l1, Location l2, double k) {
        Vector ray = new Vector(l1.getX() - l2.getX(), l1.getY() - l2.getY(), l1.getZ() - l2.getZ());
        return fastNormalize(ray).multiply(k / 6.0);
    }

    public static void castDamage(EntityDamageByEntityEvent e, Player p, ItemStack item, double critChance, double dmg, double knockback, int useTime) {
        Material mat = item.getType();
        if (p.hasCooldown(mat)) {
            e.setDamage(0);
            e.setCancelled(true);
            return;
        }
        int tickDelay = Math.max(1, Math.floorDiv(useTime, 60) * 20);
        boolean isCrit = Math.random() < critChance;
        Location eLoc = e.getEntity().getLocation();
        Location pLoc = p.getLocation();

        dmg = (ThreadLocalRandom.current().nextDouble(0.3) + 0.85) * dmg;
        e.setDamage(dmg * (isCrit ? 2.0 : 1.0));
        e.getEntity().setVelocity(knockback(eLoc, pLoc, knockback).multiply(isCrit ? 1.4 : 1.0));
        p.setCooldown(mat, tickDelay);
    }

    public static String useTimeConv(int t) {
        if (t <= 8) return "&fInsanely fast";
        if (t <= 20) return "&fVery fast";
        if (t <= 25) return "&fFast";
        if (t <= 30) return "&fAverage";
        if (t <= 35) return "&fSlow";
        if (t <= 45) return "&fVery slow";
        if (t <= 55) return "&fExtremely slow";
        return "&fSnail";
    }

    public static String kbConv(double k) {
        if (k == 0) return "&fNo knockback";
        if (k <= 1.5) return "&fExtremely weak";
        if (k <= 3) return "&fVery weak";
        if (k <= 4) return "&fWeak";
        if (k <= 6) return "&fAverage knockback";
        if (k <= 7) return "&fStrong";
        if (k <= 9) return "&fVery strong";
        if (k <= 11) return "&fExtremely strong";
        return "&fInsane knockback";
    }

    public static String getDMG(double d) {
        return "&f" + (int) d + " melee damage";
    }

    public static String getCC(double c) {
        return "&f" + (int) (c * 100) + "% critical strike chance";
    }
}
