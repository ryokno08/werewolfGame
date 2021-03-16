package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Colors {
    private final static List<Color> colors = new ArrayList<>();

    public static List<Color> values() {
        colors.clear();

        colors.add(Color.BLUE);
        colors.add(Color.OLIVE);
        colors.add(Color.ORANGE);
        colors.add(Color.PURPLE);
        colors.add(Color.WHITE);
        colors.add(Color.AQUA);
        colors.add(Color.BLACK);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.LIME);

        return colors;
    }
}
