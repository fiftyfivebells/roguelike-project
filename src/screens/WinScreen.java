package screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class WinScreen implements Screen {
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You're a winner", 1, 1);
        terminal.writeCenter("-- Press [enter] to restart --", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
