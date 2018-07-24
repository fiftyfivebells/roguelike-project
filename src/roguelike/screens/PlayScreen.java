package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.world.World;
import roguelike.world.WorldBuilder;

import java.awt.event.KeyEvent;

public class PlayScreen implements Screen {

    private World world;
    private int centerX;
    private int centerY;
    private int screenWidth;
    private int screenHeight;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        createWorld();
    }

    public void createWorld() {
        world = new WorldBuilder(90, 31)
            .makeCaves()
            .build();
    }

    public int getScrollX() {
        return Math.max(0, Math.min(centerX - screenWidth / 2, world.getWidth() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(centerY - screenHeight / 2, world.getHeight() - screenWidth));
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        for (int i = 0; i < screenWidth; i++) {
            for (int j = 0; j < screenHeight; j++) {
                int wx = i + left;
                int wy = j + top;

                terminal.write(world.glyph(wx, wy), i, j, world.color(wx, wy));
            }
        }
    }

    private void scrollBy(int mx, int my) {
        centerX = Math.max(0, Math.min(centerX + mx, world.getWidth() - 1));
        centerY = Math.max(0, Math.min(centerY + my, world.getHeight() - 1));
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        terminal.write('X', centerX - left, centerY - top);
        terminal.writeCenter(" -- press [escape] to lose or [enter] to win.", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch(key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return new LoseScreen();
            case KeyEvent.VK_ENTER: return new WinScreen();
            case KeyEvent.VK_H:
            case KeyEvent.VK_LEFT:  scrollBy(-1, 0);  break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT: scrollBy(1, 0);   break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_UP:    scrollBy(0, -1);   break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_DOWN:  scrollBy(0, 1);  break;
            case KeyEvent.VK_Y:     scrollBy(-1, -1);  break;
            case KeyEvent.VK_U:     scrollBy(1, -1);   break;
            case KeyEvent.VK_B:     scrollBy(-1, 1); break;
            case KeyEvent.VK_N:     scrollBy(1, 1);  break;
        }
        return this;
    }
}
