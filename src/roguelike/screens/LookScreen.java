package roguelike.screens;

import roguelike.creature.Creature;
import roguelike.items.Item;
import roguelike.world.Tile;

public class LookScreen extends TargetBasedScreen {

    public LookScreen(Creature player, String caption, int sx, int sy) {
        super(player, caption, sx, sy);
    }

    @Override
    public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
        Creature creature = player.creature(x, y, player.getZ());
        if (creature != null) {
            caption = creature.getGlyph() + " " + creature.getName() + creature.details();
            return;
        }

        Item item = player.item(x, y, player.getZ());
        if (item != null) {
            caption = item.getGlyph() + " " + item.getName() + item.details();
            return;
        }

        Tile tile = player.tile(x, y, player.getZ());
        caption = tile.getGlyph() + " " + tile.details();
    }
}
