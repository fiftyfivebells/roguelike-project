package roguelike.creature;

import roguelike.world.Tile;

public class PlayerAI extends CreatureAI {
    public PlayerAI(Creature creature) {
        super(creature);
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
}
