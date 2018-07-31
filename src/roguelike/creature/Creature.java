package roguelike.creature;

import roguelike.world.World;

import java.awt.Color;

public class Creature {
    private World world;
    private CreatureAI ai;

    private int x;
    private int y;

    private char glyph;
    private Color color;

    public Creature(World world, char glyph, Color color) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public void moveBy(int mx, int my) {
        Creature other = world.placeCreature(x + mx, y + my);

        if (other == null) {
            ai.onEnter(x+mx, y+my, world.tile(x+mx, y+my));
        } else {
            attack(other);
        }
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround() && world.placeCreature(x, y) == null;
    }

    public void attack(Creature c) {
        world.remove(c);
    }

    public void update() {
        ai.update();
    }
}
