package roguelike.creature;

import roguelike.world.Line;
import roguelike.world.Point;
import roguelike.world.Tile;

public class CreatureAI {
    protected Creature creature;

    public CreatureAI (Creature creature) {
        this.creature = creature;
        this.creature.setAi(this);
    }

    public boolean canSee(int wx, int wy, int wz) {
        if (creature.getZ() != wz) {
            return false;
        }

        if ((creature.getX()-wx) * (creature.getX()-wx) + (creature.getY()-wy) * (creature.getY()-wy) > creature.getVisionRadius()*creature.getVisionRadius()) {
            return false
        }

        for (Point p : new Line(creature.getX(), creature.getY(), wx, wy)) {
            if (creature.tile(p.getX(), p.getY(), wz).isGround() || p.getX() == wx && p.getY() == wy) {
                continue;
            }
            return false;
        }
        return true;
    }

    public void onEnter(int x, int y, int z, Tile tile) {}

    public void onUpdate() {}

    public void onNotify(String message) {}
}
