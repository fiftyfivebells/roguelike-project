package roguelike.creature;

import asciiPanel.AsciiPanel;
import roguelike.creature.creatures.FungusAI;
import roguelike.creature.creatures.PlayerAI;
import roguelike.world.World;

import java.lang.reflect.Field;
import java.util.List;

public class CreatureFactory {
    private World world;
    private FieldOfView fov;

    public CreatureFactory(World world, FieldOfView fov) {
        this.world = world;
        this.fov = fov;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100, 20, 5);
        world.addAtEmptyLocation(player, 0);
        new PlayerAI(player, messages, fov);
        return player;
    }

    public Creature newFungus(int depth) {
        Creature fungus = new Creature(world, 'm', AsciiPanel.brightGreen, 10, 0, 0);
        world.addAtEmptyLocation(fungus, depth);
        new FungusAI(fungus, this);
        return fungus;
    }
}
