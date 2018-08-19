package roguelike.creature;

import roguelike.items.Inventory;
import roguelike.items.Item;
import roguelike.world.Line;
import roguelike.world.Point;
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
    private int level;
    private int xp;
    private int regenHPCooldown;
    private int regenHPper1000;

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
        this.level = 1;
        this.regenHPper1000 = 10;
        this.regenHPCooldown = 1000;
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

    public int getXp() { return xp;}

    public void modifyXp(int amount) {
        xp += amount;

        notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

        while (xp > (int) (Math.pow(level, 1.5) * 20)) {
            level++;
            doAction("advance to level %d", level);
            ai.onGainLevel();
            modifyHP(level * 2);
        }
    }

    public void modifyRegenHPper1000(int amount) {
        regenHPper1000 += amount;
    }

    private void regenerateHealth() {
        regenHPCooldown -= regenHPper1000;
        if (regenHPCooldown < 0) {
            modifyHP(1);
            modifyFood(-1);
            regenHPCooldown += 1000;
        }
    }

    public int getLevel() { return level; }

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

    public String details() {
        return String.format("  level: %d   attack: %d  defense: %d     hp: %d", level, attackValue, defenseValue, currentHP);
    }

    public int getAttackValue() {
        return attackValue
            + (weapon == null ? 0 : weapon.getAttackValue())
            + (armor == null ? 0 : armor.getAttackValue());
    }

    public void setAttackValue(int attackValue) { this.attackValue = attackValue; }

    public int getDefenseValue() {
        return defenseValue
            + (armor == null ? 0 : armor.getDefenseValue())
            + (weapon == null ? 0 : weapon.getDefenseValue());
    }

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
        unequip(item);
    }

    public void unequip(Item item) {
        if (item == null) { return; }

        if (item == armor) {
            doAction("remove a " + item.getName());
            armor = null;
        } else if (item == weapon) {
            doAction("put away a " + item.getName());
            weapon = null;
        }
    }

    public void equip(Item item) {
        if (!inventory.contains(item)) {
            if (inventory.isFull()) {
                notify("Can't equip %s because you're holding too much stuff.", item.getName());
                return;
            } else {
                world.remove(item);
                inventory.add(item);
            }
        }

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

    public void meleeAttack(Creature other) {
        commonAttack(other, getAttackValue(), "attack the %s for %d damage.",
                other.getName());
    }

    public void gainXP(Creature creature) {
        int amount = creature.getMaxHP()
                + creature.getAttackValue()
                + creature.getDefenseValue()
                - (level * 2);

        if (amount > 0) {
            modifyXp(amount);
        }
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

    public void gainMaxHP() {
        maxHP += 10;
        currentHP += 10;
        doAction("look healthier");
    }

    public void gainAttackValue() {
        attackValue += 2;
        doAction("look stronger");
    }

    public void gainDefenseValue() {
        defenseValue += 2;
        doAction("look tougher");
    }

    public void gainVisionRadius() {
        visionRadius += 1;
        doAction("look more aware");
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
        if (item.getFoodValue() == 0) {
            notify("Yuck!");
        }

        modifyFood(item.getFoodValue());
        inventory.remove(item);
        unequip(item);
    }

    private void leaveCorpse() {
        Item corpse = new Item('%', color, name + " corpse");
        corpse.modifyFoodValue(3 * maxHP);
        world.addAtEmptySpace(corpse, x, y, z);
        for (Item item : inventory.getItems()) {
            if (item != null) {
                drop(item);
            }
        }
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
            meleeAttack(other);
        }
    }

    public Creature creature(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz)) {
            return world.placeCreature(wx, wy, wz);
        } else {
            return null;
        }
    }

    public Tile realTile(int wx, int wy, int wz) { return world.tile(wx, wy, wz); }

    public Tile tile(int wx, int wy, int wz) {

        if (canSee(wx, wy, wz)) {
            return world.tile(wx, wy, wz);
        } else {
            return ai.rememberedTile(wx, wy, wz);
        }

    }

    public Item item(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz)) {
            return world.item(wx, wy, wz);
        } else {
            return null;
        }
    }

    public void throwItem(Item item, int wx, int wy, int wz) {
        Point end = new Point(x, y, 0);

        for (Point p : new Line(x, y, wx, wy)) {
            if (!realTile(p.getX(), p.getY(), z).isGround()) { break; }

            end = p;
        }

        wx = end.getX();
        wy = end.getY();

        Creature c = creature(wx, wy, wz);

        if (c!= null) {
            throwAttack(item, c);
        } else {
            doAction("throw a %s", item.getName());
        }
    }

    public void throwAttack(Item item, Creature creature) {
        commonAttack(creature, attackValue / 2 + item.getThrownAttackValue(), "throw a %s at the %s for %d damage",
                item.getName(), creature.getName());
    }

    public void rangedWeaponAttack(Creature other) {
        commonAttack(other, attackValue / 2 + weapon.getRangedAttackValue(), "fire a %s at the %s for %d damage",
                weapon.getName(), other.getName());
    }

    private void commonAttack(Creature other, int attack, String action, Object ... params) {
        modifyFood(-2);

        int amount = Math.max(0, attack - other.getDefenseValue());
        amount = (int) (Math.random() * amount) + 1;

        Object[] params2 = new Object[params.length + 1];
        for (int i = 0; i < params.length; i++) {
            params2[i] = params[i];
        }
        params2[params2.length - 1] = amount;

        doAction(action, params2);

        other.modifyHP(-amount);

        if (other.getCurrentHP() < 1) {
            gainXP(other);
        }
    }

    private void getRidOf(Item item) {
        inventory.remove(item);
        unequip(item);
    }

    private void putAt(Item item, int wx, int wy, int wz) {
        inventory.remove(item);
        unequip(item);
        world.addAtEmptySpace(item, wx, wy, wz);
    }

    public boolean canSee(int wx, int wy, int wz) {
        return ai.canSee(wx, wy, wz);
    }

    public boolean canEnter(int x, int y, int z) {

        return world.tile(x, y, z).isGround() && world.placeCreature(x, y, z) == null;
    }

    public void update() {
        modifyFood(-1);
        regenerateHealth();
        ai.onUpdate();
    }

    public void notify(String message, Object ... params) {

        ai.onNotify(String.format(message, params));
    }
}
