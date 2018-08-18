package roguelike.creature;

import asciiPanel.AsciiPanel;
import roguelike.creature.creatures.BatAI;
import roguelike.creature.creatures.FungusAI;
import roguelike.creature.creatures.PlayerAI;
import roguelike.creature.creatures.ZombieAI;
import roguelike.items.Item;
import roguelike.world.World;

import java.lang.reflect.Field;
import java.util.List;

public class StuffFactory {
    private World world;
    private FieldOfView fov;

    public StuffFactory(World world, FieldOfView fov) {
        this.world = world;
        this.fov = fov;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(world, '@', "player", AsciiPanel.brightWhite, 100, 20, 5);
        world.addAtEmptyLocation(player, 0);
        new PlayerAI(player, messages, fov);
        return player;
    }

    public Creature newFungus(int depth) {
        Creature fungus = new Creature(world, 'm', "fungus", AsciiPanel.brightGreen, 10, 0, 0);
        world.addAtEmptyLocation(fungus, depth);
        new FungusAI(fungus, this);
        return fungus;
    }

    public Creature newBat(int depth) {
        Creature bat = new Creature(world, 'b', "bat", AsciiPanel.yellow, 15, 5, 0);
        world.addAtEmptyLocation(bat, depth);
        new BatAI(bat);
        return bat;
    }

    public Creature newZombie(int depth, Creature player) {
        Creature zombie = new Creature(world, 'z', "zombie", AsciiPanel.white, 50, 10, 10);
        world.addAtEmptyLocation(zombie, depth);
        new ZombieAI(zombie, player);
        return zombie;
    }

    public Item newRock(int depth) {
        Item rock = new Item(',', AsciiPanel.yellow, "rock");
        world.addAtEmptyLocation(rock, depth);
        return rock;
    }

    public Item newVictoryItem(int depth) {
        Item item = new Item('*', AsciiPanel.brightRed, "teddy bear");
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newDagger(int depth) {
        Item item = new Item('|', AsciiPanel.white, "dagger");
        item.modifyAttackValue(5);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newSword(int depth) {
        Item item = new Item('|', AsciiPanel.brightCyan, "sword");
        item.modifyAttackValue(10);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newStaff(int depth) {
        Item item = new Item('/', AsciiPanel.white, "staff");
        item.modifyAttackValue(5);
        item.modifyDefenseValue(3);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newBow(int depth) {
        Item item = new Item(')', AsciiPanel.brightBlue, "bow");
        item.modifyRangedAttackValue(5);
        item.modifyAttackValue(1);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newLightArmor(int depth) {
        Item item = new Item('[', AsciiPanel.green, "tunic");
        item.modifyDefenseValue(2);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newMediumArmor(int depth) {
        Item item = new Item('[', AsciiPanel.white, "chainmail");
        item.modifyDefenseValue(4);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newHeavyArmor(int depth) {
        Item item = new Item('[', AsciiPanel.brightWhite, "plate mail");
        item.modifyDefenseValue(6);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item randomWeapon (int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0: return newDagger(depth);
            case 1: return newSword(depth);
            default: return newStaff(depth);
        }
    }

    public Item randomArmor (int depth) {
        switch ((int) (Math.random() * 3)) {
            case 0: return newLightArmor(depth);
            case 1: return newMediumArmor(depth);
            default: return newHeavyArmor(depth);
        }
    }

}
