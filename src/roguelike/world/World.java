package roguelike.world;

import roguelike.creature.Creature;
import roguelike.items.Item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class World {
    private Tile[][][] tiles;
    private List<Creature> creatures;
    private Item[][][] items;
    private int width;
    private int height;
    private int depth;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() { return depth; }

    public List<Creature> getCreatures() { return creatures; }

    public World(Tile[][][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.depth = tiles[0][0].length;
        this.creatures = new ArrayList<Creature>();
        this.items = new Item[width][height][depth];
    }

    public Tile tile(int x, int y, int z) {
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y][z];
        }
    }

    public Item item(int width, int height, int depth) {
        return items[width][height][depth];
    }

    public Creature placeCreature(int x, int y, int z) {
        for (Creature c : creatures) {
            if (c.getX() == x && c.getY() == y && c.getZ() == z) {
                return c;
            }
        }
        return null;
    }

    public void addAtEmptyLocation(Creature creature, int z) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }
        while (!tile(x, y, z).isGround() || placeCreature(x, y, z) != null);

        creature.setX(x);
        creature.setY(y);
        creature.setZ(z);
        creatures.add(creature);
    }

    public void addAtEmptyLocation(Item item, int z) {
        int x;
        int y;

        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }
        while (!tile(x, y, depth).isGround() || item(x, y, depth) != null);

        items[x][y][depth] = item;
    }

    public void remove(Creature c) {
        creatures.remove(c);
    }

    public void update() {
        List<Creature> toUpdate = new ArrayList(creatures);
        for (Creature c : toUpdate) {
            c.update();
        }
    }

    public char glyph(int x, int y, int z) {
        Creature creature = placeCreature(x, y, z);
        if (creature != null) {
            return creature.getGlyph();
        }

        if (item(x, y, z) != null) {
            return item(x, y, z).getColor();
        }

    }

    public Color color(int x, int y, int z) {
        Creature creature = placeCreature(x, y, z);
        if (creature != null) {
            return creature.getColor();
        }

        if (item(x, y, z) != null) {
            return item(x, y, z).getColor();
        }
    }

    public void dig(int x, int y, int z) {
        if (tile(x, y, z).isDiggable()) {
            tiles[x][y][z] = Tile.FLOOR;
        }
    }
}


