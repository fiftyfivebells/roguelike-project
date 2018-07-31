package roguelike.creature;

import roguelike.world.Tile;

public class CreatureAI {
    protected Creature creature;

    public CreatureAI (Creature creature) {
        this.creature = creature;
        this.creature.setAi(this);
    }

    public void onEnter(int x, int y, Tile tile) {}
}