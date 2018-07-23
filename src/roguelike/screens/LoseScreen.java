package roguelike.screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class LoseScreen implements Screen {
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("I think you lost", 1, 1);
        terminal.writeCenter("-- press [enter] to play again --", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
