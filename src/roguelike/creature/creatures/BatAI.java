package roguelike.creature.creatures;

import roguelike.creature.Creature;
import roguelike.creature.CreatureAI;

public class BatAI extends CreatureAI {

    public BatAI(Creature creature) {
        super(creature);
    }

    public void onUpdate() {
        wander();
        wander();
    }
}
