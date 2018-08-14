package roguelike.creature.levelUpOptions;

import roguelike.creature.Creature;

import java.util.ArrayList;
import java.util.List;

public class LevelUpController {
    private static LevelUpOption[] options = new LevelUpOption[] {
            new LevelUpOption("Increased hit points") {
                public void invoke(Creature creature) { creature.gainMaxHP(); }
            },
            new LevelUpOption("Increased attack value") {
                public void invoke(Creature creature) { creature.gainAttackValue(); }
            },
            new LevelUpOption("Increased defense value") {
                public void invoke(Creature creature) { creature.gainDefenseValue(); }
            },
            new LevelUpOption("Increased vision radius") {
                public void invoke(Creature creature) { creature.gainVisionRadius(); }
            }
    };

    public LevelUpOption getLevelUpOption(String option) {
        for (LevelUpOption name : options) {
            if (name.getName().equals(option)) {
                return name;
            }
        }
        return null;
    }

    public List<String> getLevelUpOptions() {
        List<String> names = new ArrayList<String>();

        for (LevelUpOption option : options) {
            names.add(option.getName());
        }
        return names;
    }

    public void autoLevelUp(Creature creature) {
        options[(int) (Math.random() * options.length)].invoke(creature);
    }
}
