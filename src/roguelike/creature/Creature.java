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

    private int HP;
    private int maxHP;
    private int attackValue;
    private int defenseValue;

    public Creature(World world, char glyph, Color color, int hp, int atk, int def) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = hp;
        this.attackValue = atk;
        this.defenseValue = def;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getHP() { return HP; }

    public void setHP(int HP) { this.HP = HP; }

    public int getMaxHP() { return maxHP; }

    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public int getAttackValue() { return attackValue; }

    public void setAttackValue(int attackValue) { this.attackValue = attackValue; }

    public int getDefenseValue() { return defenseValue; }

    public void setDefenseValue(int defenseValue) { this.defenseValue = defenseValue; }

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

    public void attack(Creature other) {
        int amount = Math.max(0, getAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount) + 1;

        other,modifyHP(-amount);
    }

    public void modifyHP(int amount) {
        HP += amount;

        if (HP < 1) {
            world.remove(this);
        }
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
        ai.onUpdate();
    }
}
