package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.world.Line;
import roguelike.world.Point;

public class FireWeaponScreen extends TargetBasedScreen {

    public FireWeaponScreen(Creature player,int sx, int sy) {
        super(player, "Fire " + player.getWeapon().getName() + " at?", sx, sy);
    }

    public boolean isAcceptable(int x, int y) {
        if (!player.canSee(x, y, player.getZ())) {
            return false;
        }

        for (Point p : new Line(player.getX(), player.getY(), x, y)) {
            if (!player.realTile(p.getX(), p.getY(), p.getZ()).isGround()) {
                return false;
            }
        }
        return true;
    }

    public void selectWorldCoordinate(int x, int y, int screenX, int screenY) {
        Creature other = player.creature(x, y, player.getZ());
        if (other == null) {
            player.notify("There's no one there to fire at.");
        } else {
            player.rangedWeaponAttack(other);
        }
    }
}
