package roguelike.creature.creatures;

import roguelike.creature.Creature;
import roguelike.creature.CreatureAI;
import roguelike.creature.Path;
import roguelike.items.Item;
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

    private boolean canRangedWeaponAttack(Creature player) {
        return creature.getWeapon() != null
                && creature.getWeapon().getRangedAttackValue() > 0
                && creature.canSee(player.getX(), player.getY(), player.getZ());
    }

    private boolean canThrowAttack(Creature player) {
        return creature.canSee(player.getX(), player.getY(), player.getZ())
                && getWeaponToThrow() != null;
    }

    private Item getWeaponToThrow() {
        Item toThrow = null;

        for (Item item : creature.getInventory().getItems()) {
            if (item == null || creature.getWeapon() == item || creature.getArmor() == item) {
                continue;
            }

            if (toThrow == null || item.getThrownAttackValue() > toThrow.getAttackValue()) {
                toThrow = item;
            }
        }
        return toThrow;
    }

    private boolean canPickUp() {
        return creature.item(creature.getX(), creature.getY(), creature.getZ()) != null
                && !creature.getInventory().isFull();
    }

    public void hunt(Creature target) {
        List<Point> points = new Path(creature, target.getX(), target.getY()).getPoints();

        int mx = points.get(0).getX() - creature.getX();
        int my = points.get(0).getY() - creature.getY();

        creature.moveBy(mx, my, 0);
    }
}
