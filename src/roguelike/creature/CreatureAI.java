package roguelike.creature;

import roguelike.creature.levelUpOptions.LevelUpController;
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
            return false;
        }

        for (Point p : new Line(creature.getX(), creature.getY(), wx, wy)) {
            if (creature.realTile(p.getX(), p.getY(), wz).isGround() || p.getX() == wx && p.getY() == wy) {
                continue;
            }
            return false;
        }
        return true;
    }

    public Tile rememberedTile(int wx, int wy, int wz) {
        return Tile.UNKNOWN;
    }

    public void onEnter(int x, int y, int z, Tile tile) {
        if (tile.isGround()) {
            creature.setX(x);
            creature.setY(y);
            creature.setZ(z);
        } else {
            creature.doAction("bump into a wall");
        }
    }

    public void onGainLevel() {
        new LevelUpController().autoLevelUp(creature);
    }

    public void wander() {
        int mx = (int) (Math.random() * 3) - 1;
        int my = (int) (Math.random() * 3) - 1;

        Creature other = creature.creature(creature.getX() + mx, creature.getY() + my, creature.getZ());

        if (other != null && other.getGlyph() == creature.getGlyph()) {
            return;
        } else {
            creature.moveBy(mx, my, 0);
        }
    }

    public void onUpdate() {}

    public void onNotify(String message) {}
}
