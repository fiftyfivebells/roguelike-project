package roguelike.creature;

import roguelike.items.Inventory;
import roguelike.items.Item;
import roguelike.world.Tile;
import roguelike.world.World;

import java.awt.Color;

public class Creature {
    private World world;
    private CreatureAI ai;

    private int x;
    private int y;
    private int z;

    private char glyph;
    private Color color;

    private String name;
    private int currentHP;
    private int maxHP;
    private int maxFood;
    private int currentFood;
    private int attackValue;
    private int defenseValue;
    private int visionRadius;

    private Inventory inventory;
    private Item weapon;
    private Item armor;


    public Creature(World world, char glyph, String name, Color color, int hp, int atk, int def) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = hp;
        this.currentHP = hp;
        this.maxFood = 1000;
        this.currentFood = maxFood / 3 * 2;
        this.attackValue = atk;
        this.defenseValue = def;
        this.visionRadius = 9;
        this.name = name;
        this.inventory = new Inventory(20);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() { return z; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public void setZ(int z) { this.z = z; }

    public Item getWeapon() { return weapon; }

    public Item getArmor() { return armor; }

    public int getVisionRadius() { return visionRadius; }

    public void setVisionRadius(int visionRadius) { this.visionRadius = visionRadius; }

    public int getCurrentHP() { return currentHP; }

    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }

    public int getMaxHP() { return maxHP; }

    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public int getMaxFood() { return maxFood; }

    public int getCurrentFood() { return currentFood; }

    public int getAttackValue() { return attackValue; }

    public void setAttackValue(int attackValue) { this.attackValue = attackValue; }

    public int getDefenseValue() { return defenseValue; }

    public void setDefenseValue(int defenseValue) { this.defenseValue = defenseValue; }

    public Inventory getInventory() { return inventory; }

    public String getName() { return name; }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public void setAi(CreatureAI ai) {
        this.ai = ai;
    }

    public void pickup() {
        Item item = world.item(x, y, z);

        if (inventory.isFull() || item == null) {
            doAction("grab at the ground");
        } else {
            doAction("pick up a %s", item.getName());
            world.remove(x, y, z);
            inventory.add(item);
        }
    }

    public void drop(Item item) {
        doAction("drop a " + item.getName());
        inventory.remove(item);
        world.addAtEmptySpace(item, x, y, z);
    }

    private void unequip(Item item) {
        if (item == null) { return; }

        if (item == armor) {
            doAction("remove a " + item.getName());
            armor = null;
        } else if (item == weapon) {
            doAction("put away a " + item.getName());
            weapon = null;
        }
    }

    private void equip(Item item) {
        if (item.getAttackValue() == 0 && item.getDefenseValue() == 0) { return; }

        if (item.getAttackValue() >= item.getDefenseValue()) {
            unequip(weapon);
            doAction("wield a " + item.getName());
            weapon = item;
        } else {
            unequip(armor);
            doAction("put on a " + item.getName());
            armor = item;
        }
    }

    public void dig(int wx, int wy, int wz) {
        modifyFood(-10);
        world.dig(wx, wy, wz);
        doAction("dig");
    }

    public void attack(Creature other) {
        int amount = Math.max(0, getAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount) + 1;

        other.modifyHP(-amount);

        doAction("attack the %s for %d damage", other.getName(), amount);
        modifyFood(-10);
    }

    public void doAction(String message, Object ... params) {
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) { continue; }

                Creature other = world.placeCreature(x + ox, y + oy, z);

                if (other == null) { continue; }

                if (other == this) {
                    other.notify("You " + message + ".", params);
                } else if (other.canSee(x, y, z)) {
                    other.notify(String.format("The %s %s.", getName(), makeSecondPerson(message)), params);
                }
            }
        }
    }

    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public void modifyHP(int amount) {
        currentHP += amount;

        if (currentHP < 1) {
            doAction("die");
            leaveCorpse();
            world.remove(this);
        } else if (currentHP > maxHP) {
            currentHP = maxHP;
        }
    }

    public void modifyFood(int amount) {
        currentFood += amount;
        if (currentFood > maxFood) {
            currentFood = maxFood;
        } else if (currentFood < 1 && isPlayer()) {
            modifyHP(-1000);
        }
    }

    public boolean isPlayer() {
        return glyph == '@';
    }

    public void eat(Item item) {
        modifyFood(item.getFoodValue());
        inventory.remove(item);
    }

    private void leaveCorpse() {
        Item corpse = new Item('%', color, name + " corpse");
        corpse.modifyFoodValue(3 * maxHP);
        world.addAtEmptySpace(corpse, x, y, z);
    }

    public void moveBy(int mx, int my, int mz) {
        Tile tile = world.tile(x+mx, y+my, z+mz);

        if (mx == 0 && my == 0 && mz == 0) { return; }

        if (mz == -1) {
            if (tile == Tile.STAIRS_DOWN) {
                doAction("walk up stairs to level %d", z+mz+1);
            } else {
                doAction("try to go up but are stopped by the cave ceiling");
                return;
            }
        } else if (mz == 1) {
            if (tile == Tile.STAIRS_UP) {
                doAction("walk down stairs to level %d", z+mz+1);
            } else {
                doAction("try to go down by are stopped by the cave floor");
                return;
            }
        }

        Creature other = world.placeCreature(x+mx, y+my, z+mz);

        if (other == null) {
            ai.onEnter(x+mx, y+my, z+mz, tile);
        } else {
            attack(other);
        }
    }

    public Creature creature(int wx, int wy, int wz) {
        return world.placeCreature(wx, wy, wz);
    }

    public Tile tile(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz);
    }

    public boolean canSee(int wx, int wy, int wz) {
        return ai.canSee(wx, wy, wz);
    }

    public boolean canEnter(int x, int y, int z) {

        return world.tile(x, y, z).isGround() && world.placeCreature(x, y, z) == null;
    }

    public void update() {
        modifyFood(-1);
        ai.onUpdate();
    }

    public void notify(String message, Object ... params) {

        ai.onNotify(String.format(message, params));
    }
}
