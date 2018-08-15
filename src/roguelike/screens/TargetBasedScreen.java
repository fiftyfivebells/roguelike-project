package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.creature.Creature;
import roguelike.world.Line;
import roguelike.world.Point;

import java.awt.event.KeyEvent;

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

    @Override
    public void displayOutput(AsciiPanel terminal) {
        for (Point p : new Line(sx, sy, sx+x, sy+y)) {
            if (p.getX() < 0 || p.getX() >= 80 || p.getY() < 0 || p.getY() >= 24) {
                continue;
            }

            terminal.write('*', p.getX(), p.getY(), AsciiPanel.brightMagenta);
        }

        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write(caption, 0, 23);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
}
