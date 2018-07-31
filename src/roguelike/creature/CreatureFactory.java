package roguelike.creature;

import asciiPanel.AsciiPanel;
import roguelike.creature.creatures.FungusAI;
import roguelike.world.World;

public class CreatureFactory {
    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer() {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite);
        world.addAtEmptyLocation(player);
        new PlayerAI(player);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(world, 'm', AsciiPanel.brightGreen);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus);
        return fungus;
    }
}
