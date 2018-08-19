package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.creature.Creature;
import roguelike.creature.StuffFactory;
import roguelike.creature.FieldOfView;
import roguelike.items.Item;
import roguelike.world.Tile;
import roguelike.world.World;
import roguelike.world.WorldBuilder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {

    private World world;
    Creature player;
    StuffFactory sf;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private FieldOfView fov;
    private Screen subscreen;

    public PlayScreen() {
        screenWidth = 80;
        screenHeight = 21;
        messages = new ArrayList<String>();
        createWorld();
        fov = new FieldOfView(world);
        sf = new StuffFactory(world, fov);
        createCreatures(sf);
        createItems(sf);
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

            for (int i = 0; i < z + 5; i++) {
                stuffFactory.newZombie(z, player);
            }

            for (int i = 0; i < z + 7; i++) {
                stuffFactory.newGoblin(z, player);
            }
        }
    }

    private void createItems(StuffFactory stuffFactory) {
        for (int z = 0; z < world.getDepth(); z++) {
            for (int i = 0; i < 8; i++) {
                stuffFactory.newRock(z);
                stuffFactory.randomArmor(z);
                stuffFactory.randomWeapon(z);
            }
        }
        stuffFactory.newVictoryItem(world.getDepth() - 1);
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
        return Math.max(0, Math.min(player.getY() - screenHeight / 2, world.getHeight() - screenHeight));
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

    private boolean userIsTryingToExit() {
        return player.getZ() == 0 && world.tile(player.getX(), player.getY(), player.getZ()) == Tile.STAIRS_UP;
    }

    private Screen userExits() {
        for (Item item : player.getInventory().getItems()) {
            if (item != null && item.getName().equals("teddy bear")) {
                return new WinScreen();
            }
        }
        return new LoseScreen();
    }

    private String hunger() {
        if (player.getCurrentFood() < player.getMaxFood() * 0.1) {
            return "Starving";
        } else if (player.getCurrentFood() < player.getMaxFood() * 0.2) {
            return "Hungry";
        } else if (player.getCurrentFood() < player.getMaxFood() * 0.9) {
            return "Stuffed";
        } else if (player.getCurrentFood() < player.getMaxFood() * 0.8) {
            return "Full";
        } else {
            return "";
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();
        displayTiles(terminal, left, top);
        displayMessages(terminal, messages);

        terminal.writeCenter(" -- press [escape] to lose or [enter] to win.", 23);

        String stats = String.format(" %3d/%3d hp %8s", player.getCurrentHP(), player.getMaxHP(), hunger());
        terminal.write(stats, 1, 23);

        if (subscreen != null) {
            subscreen.displayOutput(terminal);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        int level = player.getLevel();

        if (subscreen != null) {
            subscreen = subscreen.respondToUserInput(key);
        } else {

            switch (key.getKeyCode()) {
                case KeyEvent.VK_H:
                case KeyEvent.VK_LEFT:
                    player.moveBy(-1, 0, 0);
                    break;
                case KeyEvent.VK_L:
                case KeyEvent.VK_RIGHT:
                    player.moveBy(1, 0, 0);
                    break;
                case KeyEvent.VK_K:
                case KeyEvent.VK_UP:
                    player.moveBy(0, -1, 0);
                    break;
                case KeyEvent.VK_J:
                case KeyEvent.VK_DOWN:
                    player.moveBy(0, 1, 0);
                    break;
                case KeyEvent.VK_Y:
                    player.moveBy(-1, -1, 0);
                    break;
                case KeyEvent.VK_U:
                    player.moveBy(1, -1, 0);
                    break;
                case KeyEvent.VK_B:
                    player.moveBy(-1, 1, 0);
                    break;
                case KeyEvent.VK_N:
                    player.moveBy(1, 1, 0);
                    break;
                case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
                case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
                case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;
                case KeyEvent.VK_X: subscreen = new ExamineScreen(player); break;
                case KeyEvent.VK_SEMICOLON: subscreen = new LookScreen(player, "Looking",
                        player.getX() - getScrollX(), player.getY() - getScrollY()); break;
                case KeyEvent.VK_T: subscreen = new ThrowScreen(player,
                        player.getX() - getScrollX(),
                        player.getY() - getScrollY()); break;
                case KeyEvent.VK_F:
                    if (player.getWeapon() == null || player.getWeapon().getRangedAttackValue() == 0) {
                        player.notify("You don't have a ranged weapon equipped.");
                    } else {
                        subscreen = new FireWeaponScreen(player,
                                player.getX() - getScrollX(),
                                player.getY() - getScrollY()); break;
                    }
            }

            switch (key.getKeyChar()) {
                case 'g':
                case ',':
                    player.pickup();
                    break;
                case '<':
                    if (userIsTryingToExit()) {
                        return userExits();
                    } else {
                        player.moveBy(0, 0, -1);
                    }
                    break;
                case '>':
                    player.moveBy(0, 0, 1);
                    break;
                case '?':
                    subscreen = new HelpScreen(); break;
            }
        }

        if (player.getLevel() > level) {
            subscreen = new LevelUpScreen(player, player.getLevel() - level);
        }

        if (subscreen == null) {
            world.update();
        }

        if (player.getCurrentHP() < 1) {
            return new LoseScreen();
        }

        return this;
    }
}
