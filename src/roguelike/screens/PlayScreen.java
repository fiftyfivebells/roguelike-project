package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.creature.Creature;
import roguelike.creature.StuffFactory;
import roguelike.creature.FieldOfView;
import roguelike.world.World;
import roguelike.world.WorldBuilder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {

    private World world;
    Creature player;
    StuffFactory cf;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private FieldOfView fov;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<String>();
        createWorld();
        fov = new FieldOfView(world);
        cf = new StuffFactory(world, fov);
        createCreatures(cf);
    }

    private void createCreatures(StuffFactory stuffFactory) {
        player = stuffFactory.newPlayer(messages);

        for (int z = 0; z < world.getDepth(); z++) {
            for (int i = 0; i < 8; i++) {
                stuffFactory.newFungus(z);
            }

            for (int j = 0; j < 20; j++) {
                stuffFactory.newBat(z);
            }
        }
    }

    private void createItems(StuffFactory stuffFactory) {
        for (int z = 0; z < world.getDepth(); z++) {
            for (int i = 0; i < world.getDepth() * world.getHeight() / 20; i++) {
                stuffFactory.newRock(z);
            }
        }
    }

    public void createWorld() {
        world = new WorldBuilder(90, 31, 5)
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
        fov.update(player.getX(), player.getY(), player.getZ(), player.getVisionRadius());

        for (int i = 0; i < screenWidth; i++) {
            for (int j = 0; j < screenHeight; j++) {
                int wx = i + left;
                int wy = j + top;

                if (player.canSee(wx, wy, player.getZ())) {
                    terminal.write(world.glyph(wx, wy, player.getZ()), i, j, world.color(wx, wy, player.getZ()));
                } else {
                    terminal.write(fov.tile(wx, wy, player.getZ()).getGlyph(), i, j, Color.darkGray);
                }
            }
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = screenHeight - messages.size();

        for (int i = 0; i < messages.size(); i++) {
            terminal.writeCenter(messages.get(i), top + i + 1);
        }
        messages.clear();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        displayMessages(terminal, messages);

        terminal.writeCenter(" -- press [escape] to lose or [enter] to win.", 23);

        String stats = String.format(" %3d/%3d hp", player.getCurrentHP(), player.getMaxHP());
        terminal.write(stats, 1, 23);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch(key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return new LoseScreen();
            case KeyEvent.VK_ENTER: return new WinScreen();
            case KeyEvent.VK_H:
            case KeyEvent.VK_LEFT:  player.moveBy(-1, 0, 0);  break;
            case KeyEvent.VK_L:
            case KeyEvent.VK_RIGHT: player.moveBy(1, 0, 0);   break;
            case KeyEvent.VK_K:
            case KeyEvent.VK_UP:    player.moveBy(0, -1, 0);   break;
            case KeyEvent.VK_J:
            case KeyEvent.VK_DOWN:  player.moveBy(0, 1, 0);  break;
            case KeyEvent.VK_Y:     player.moveBy(-1, -1, 0);  break;
            case KeyEvent.VK_U:     player.moveBy(1, -1, 0);   break;
            case KeyEvent.VK_B:     player.moveBy(-1, 1, 0); break;
            case KeyEvent.VK_N:     player.moveBy(1, 1, 0);  break;
        }

        switch(key.getKeyChar()) {
            case '<': player.moveBy(0, 0, -1); break;
            case '>': player.moveBy(0, 0, 1); break;
        }
        world.update();

        if (player.getCurrentHP() < 1) {
            return new LoseScreen();
        }
        return this;
    }
}
