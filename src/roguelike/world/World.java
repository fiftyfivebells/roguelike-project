package roguelike.world;

import roguelike.creature.Creature;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class World {
    private Tile[][] tiles;
    private List<Creature> creatures = new ArrayList<Creature>();
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Creature> getCreatures() { return creatures; }

    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public Creature placeCreature(int x, int y) {
        for (Creature c : creatures) {
            if (c.getX() == x && c.getY() == y) {
                return c;
            }
        }
        return null;
    }

    public void addAtEmptyLocation(Creature creature) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }
        while (!tile(x, y).isGround() && placeCreature(x, y) != null);

        creature.setX(x);
        creature.setY(y);
        creatures.add(creature);
    }

    public char glyph(int x, int y) {
        return tile(x, y).getGlyph();
    }

    public Color color(int x, int y) {
        return tile(x, y).getColor();
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }
}


