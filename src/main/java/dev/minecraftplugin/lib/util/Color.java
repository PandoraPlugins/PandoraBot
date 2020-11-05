package dev.minecraftplugin.lib.util;

public class Color {
    public static java.awt.Color color(String s) {
        return java.awt.Color.decode(String.valueOf(Integer.parseInt(s.replace("#", ""), 16)));
    }
}
