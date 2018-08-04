package roguelike.world;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
    FLOOR('.', AsciiPanel.white),
    WALL('#', AsciiPanel.white),
    BOUNDS('x', AsciiPanel.brightBlack),
    STAIRS_DOWN('>', AsciiPanel.brightGreen),
    STAIRS_UP('<', AsciiPanel.brightGreen),
    UNKNOWN(' ', AsciiPanel.white);

    private char glyph;
    public char getGlyph() { return glyph; }

    private Color color;
    public Color getColor() { return color; }

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }

    public boolean isDiggable() {
        return this == Tile.WALL;
    }

    public boolean isGround() {
        return this != Tile.WALL && this != Tile.BOUNDS;
    }
}
