package roguelike.creature;

import asciiPanel.AsciiPanel;
import roguelike.creature.creatures.FungusAI;
import roguelike.world.World;

import java.util.List;

public class CreatureFactory {
    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100, 20, 5);
        world.addAtEmptyLocation(player);
        new PlayerAI(player, messages);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(world, 'm', AsciiPanel.brightGreen, 10, 0, 0);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }
}
