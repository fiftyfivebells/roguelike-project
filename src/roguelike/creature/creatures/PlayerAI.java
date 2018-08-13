package roguelike.creature.creatures;

import roguelike.creature.Creature;
import roguelike.creature.CreatureAI;
import roguelike.creature.FieldOfView;
import roguelike.world.Tile;

import java.lang.reflect.Field;
import java.util.List;

public class PlayerAI extends CreatureAI {

    private List<String> messages;
    private FieldOfView fov;

    public PlayerAI(Creature creature, List<String> messages, FieldOfView fov) {
        super(creature);
        this.messages = messages;
        this.fov = fov;
    }

    @Override
    public boolean canSee(int wx, int wy, int wz) {
        return fov.isVisible(wx, wy, wz);
    }

    public void onEnter(int x, int y, int z, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            creature.setZ(z);
        } else if (tile.isDiggable()) {
            creature.dig(x, y, z);
        }
    }

    @Override
    public void onGainLevel() {}

    public void onNotify(String message) {

        messages.add(message);
    }
}
