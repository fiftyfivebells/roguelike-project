package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.items.Item;

public class ExamineScreen extends InventoryBasedScreen {

    public ExamineScreen(Creature player) {
        super(player);
    }

    @Override
    protected String getVerb() {
        return "examine";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen useItem(Item item) {
        String article = "aeiou".contains(item.getName().subSequence(0, 1)) ? "an " : "a ";
        player.notify("It's " + article + item.getName() +  "." + item.details());
        return null;
    }
}
