package roguelike.creature.levelUpOptions;

import roguelike.creature.Creature;

public abstract class LevelUpOption {
    private String name;

    public LevelUpOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void invoke(Creature creature);
}
