package roguelike.creature.creatures;

import roguelike.creature.Creature;
import roguelike.creature.CreatureAI;
import roguelike.creature.CreatureFactory;

public class FungusAI extends CreatureAI {

    private CreatureFactory factory;
    private int spreadCount;

    public FungusAI(Creature creature, CreatureFactory cf) {
        super(creature);
        this.factory = cf;
    }

    public void onUpdate() {
        if (spreadCount < 5 && Math.random() < 0.02) {
            spread();
        }
    }

    public void spread() {
        int x = creature.getX() + (int) (Math.random() * 11) - 5;
        int y = creature.getY() + (int) (Math.random() * 11) - 5;

        if (!creature.canEnter(x, y, creature.getZ())) {
            return;
        }

        Creature child = factory.newFungus(creature.getZ());
        child.setX(x);
        child.setY(y);
        child.setZ(creature.getZ());
        spreadCount++;
    }
}
