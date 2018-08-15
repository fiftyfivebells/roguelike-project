package roguelike.screens;

import roguelike.creature.Creature;

public class TargetBasedScreen implements Screen {
    protected Creature player;
    protected String caption;
    private int sx;
    private int sy;
    private int x;
    private int y;

    public TargetBasedScreen(Creature player, String caption, int sx, int sy) {
        this.player = player;
        this.caption = caption;
        this.sx = sx;
        this.sy = sy;
    }
}
