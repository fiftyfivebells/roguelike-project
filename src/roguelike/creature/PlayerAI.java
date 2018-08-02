package roguelike.creature;

import roguelike.world.Tile;

import java.util.List;

public class PlayerAI extends CreatureAI {

    private List<String> messages;

    public PlayerAI(Creature creature, List<String> messages) {
        super(creature);
        this.messages = messages;
    }

    @Override
    public void onEnter(int x, int y, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
        } else if (tile.isDiggable()) {
            creature.dig(x, y);
        }
    }

    public void onNotify(String message) {
        messages.add(message);
    }
}
