package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.items.Item;

import java.awt.event.KeyEvent;

public class DropScreen extends InventoryBasedScreen {

    public DropScreen(Creature player) {
        super(player);
    }

    @Override
    protected String getVerb() {
        return "drop";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen useItem(Item item) {
        player.drop(item);
        return null;
    }
}
