package roguelike.creature.creatures;

import roguelike.creature.Creature;
import roguelike.creature.CreatureAI;
import roguelike.creature.Path;
import roguelike.world.Point;

import java.util.List;

public class GoblinAI extends CreatureAI {
    private Creature player;

    public GoblinAI(Creature creature, Creature player) {
        super(creature);
        this.player = player;
    }

    public void onUpdate() {
        if (canRangedWeaponAttack(player)) {
            creature.rangedWeaponAttack(player);
        } else if (canThrowAttack(player)) {
            creature.throwItem(getWeaponToThrow(), player.getX(), player.getY(), player.getZ());
        } else if (creature.canSee(player.getX(), player.getY(), player.getZ())) {
            hunt(player);
        } else if (canPickUp()) {
            creature.pickup();
        } else {
            wander();
        }
    }

    public void hunt(Creature target) {
        List<Point> points = new Path(creature, target.getX(), target.getY()).getPoints();

        int mx = points.get(0).getX() - creature.getX();
        int my = points.get(0).getY() - creature.getY();

        creature.moveBy(mx, my, 0);
    }
}
