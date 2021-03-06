package roguelike.items;

import java.awt.*;

public class Item {
    private char glyph;
    private Color color;
    private String name;

    private int foodValue;
    private int attackValue;
    private int defenseValue;
    private int thrownAttackValue;
    private int rangedAttackValue;

    public Item(char glyph, Color color, String name) {
        this.glyph = glyph;
        this.color = color;
        this.name = name;
    }

    public char getGlyph() {
        return glyph;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoodValue() {
        return foodValue;
    }

    public void modifyFoodValue(int amount) {
        foodValue += amount;
    }

    public int getAttackValue() { return attackValue; }

    public void modifyAttackValue(int amount) {
        attackValue += amount;
    }

    public int getThrownAttackValue() { return thrownAttackValue; }

    public void modifyThrownAttackValue(int amount) { thrownAttackValue += amount; }

    public int getRangedAttackValue() { return rangedAttackValue; }

    public void modifyRangedAttackValue(int amount) {
        rangedAttackValue += amount;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void modifyDefenseValue(int amount) {
        defenseValue += amount;
    }

    public String details() {
        String details = "";

        if (attackValue != 0) {
            details += "    attack: " + attackValue;
        }

        if (defenseValue != 0) {
            details += "    defense: " + defenseValue;
        }

        if (foodValue != 0) {
            details += "    food: " + foodValue;
        }

        return details;
    }
}
