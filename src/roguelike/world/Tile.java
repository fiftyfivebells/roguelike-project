package roguelike.world;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
    FLOOR('.', AsciiPanel.white, "A dirt and rock cave floor."),
    WALL('#', AsciiPanel.white, "A dirt and rock cave wall."),
    BOUNDS('x', AsciiPanel.brightBlack, "Beyond the edge of the world."),
    STAIRS_DOWN('>', AsciiPanel.brightGreen, "A stone staircase that goes down."),
    STAIRS_UP('<', AsciiPanel.brightGreen, "A stone staircase that goes up."),
    UNKNOWN(' ', AsciiPanel.white, "(unknown)");

    private char glyph;
    public char getGlyph() { return glyph; }

    private Color color;
    public Color getColor() { return color; }

    private String details;
    public String details() { return details; }

    Tile(char glyph, Color color, String details) {
        this.glyph = glyph;
        this.color = color;
        this.details = details;
    }

    public boolean isDiggable() {
        return this == Tile.WALL;
    }

    public boolean isGround() {
        return this != Tile.WALL && this != Tile.BOUNDS;
    }

    }
}
