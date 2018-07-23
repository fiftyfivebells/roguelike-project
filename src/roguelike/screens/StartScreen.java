package roguelike.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class StartScreen implements Screen {
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("roguelike project", 1, 1);
        terminal.writeCenter("Press [enter] to start the game.", 22);
    }

    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
