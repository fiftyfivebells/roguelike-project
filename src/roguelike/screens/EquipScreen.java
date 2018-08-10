package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.items.Item;

public class EquipScreen extends InventoryBasedScreen {

    public EquipScreen(Creature player) {
        super(player);
    }

    @Override
    protected String getVerb() {
        return "wear or wield";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item.getAttackValue() > 0 || item.getDefenseValue() > 0;
    }

    @Override
    protected Screen useItem(Item item) {
        player.equip(item);
        return null;
    }
}
