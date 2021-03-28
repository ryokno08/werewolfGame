package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.*;

public class Colors {
    private final static Map<String, Color> colors = new HashMap<>();

    public static Map<String, Color> values() {
        colors.clear();

        colors.put("SILVER", Color.SILVER);
        colors.put("GRAY", Color.GRAY);
        colors.put("BLUE", Color.BLUE);
        colors.put("ORANGE", Color.ORANGE);
        colors.put("PURPLE", Color.PURPLE);
        colors.put("WHITE", Color.WHITE);
        colors.put("BLACK", Color.BLACK);
        colors.put("GREEN", Color.GREEN);
        colors.put("YELLOW", Color.YELLOW);
        colors.put("LIME", Color.LIME);

        return colors;
    }
}
