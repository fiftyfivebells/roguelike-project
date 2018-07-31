package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.creature.Creature;
import roguelike.creature.CreatureFactory;
import roguelike.world.World;
import roguelike.world.WorldBuilder;

import java.awt.event.KeyEvent;

public class PlayScreen implements Screen {

    private World world;
    Creature player;
    CreatureFactory cf;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        createWorld();
        cf = new CreatureFactory(world);
        createCreatures(cf);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        player = creatureFactory.newPlayer();

        for (int i = 0; i < 8; i++) {
            creatureFactory.newFungus();
        }
    }

    public void createWorld() {
        world = new WorldBuilder(90, 31)
            .makeCaves()
            .build();
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.getX() - screenWidth / 2, world.getWidth() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.getY() - screenHeight / 2, world.getHeight() - screenWidth));
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int i = 0; i < screenWidth; i++) {
            for (int j = 0; j < screenHeight; j++) {
                int wx = i + left;
                int wy = j + top;

                terminal.write(world.glyph(wx, wy), i, j, world.color(wx, wy));
            }
        }

        for (Creature c : world.getCreatures()) {
            if ((c.getX() > left && c.getX() < left + screenWidth) &&
                    (c.getY() > top && c.getY() < top + screenHeight)) {
                terminal.write(c.getGlyph(), c.getX() - left, c.getY() - top, c.getColor());
            }
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        terminal.write(player.getGlyph(), player.getX() - left, player.getY() - top);
        terminal.writeCenter(" -- press [escape] to lose or [enter] to win.", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch(key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return new LoseScreen();
            case KeyEvent.VK_ENTER: return new WinScreen();
            case KeyEvent.VK_H:
            case KeyEvent.VK_LEFT:  player.moveBy(-1, 0);  break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT: player.moveBy(1, 0);   break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_UP:    player.moveBy(0, -1);   break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_DOWN:  player.moveBy(0, 1);  break;
            case KeyEvent.VK_Y:     player.moveBy(-1, -1);  break;
            case KeyEvent.VK_U:     player.moveBy(1, -1);   break;
            case KeyEvent.VK_B:     player.moveBy(-1, 1); break;
            case KeyEvent.VK_N:     player.moveBy(1, 1);  break;
        }
        world.update();;
        return this;
    }
}
