package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.items.Item;

public class ThrowScreen extends InventoryBasedScreen {
    private int sx;
    private int sy;

    public ThrowScreen(Creature player,int sx, int sy) {
        super(player);
        this.sx = sx;
        this.sy = sy;
    }

    protected String getVerb() {
        return "throw";
    }

    protected boolean isAcceptable(Item item) {
        return true;
    }

    protected Screen useItem(Item item) {
        return new ThrowAtScreen(player, sx, sy, item);
    }
}
