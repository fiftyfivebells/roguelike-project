package screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class PlayScreen implements Screen {
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Look how much fun you're having!", 1, 1);
        terminal.writeCenter(" -- press [escape] to lose or [enter] to win.", 22);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch(key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: return new LoseScreen();
            case KeyEvent.VK_ENTER: return new WinScreen();
        }
        return this;
    }
}
