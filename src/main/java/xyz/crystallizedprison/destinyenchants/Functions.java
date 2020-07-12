package xyz.crystallizedprison.destinyenchants;

import java.util.SplittableRandom;

public class Functions {

    public static Boolean getChance(double chance) {
        double random = Math.random() * 100;

        return random <= chance;
    }



}
