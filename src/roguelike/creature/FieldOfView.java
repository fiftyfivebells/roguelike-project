package roguelike.creature;

import roguelike.world.Tile;
import roguelike.world.World;

public class FieldOfView {
    private World world;
    private int depth;

    private boolean[][] visible;
    private Tile[][][] tiles;

    public FieldOfView(World world) {
        this.world = world;
        this.visible = new boolean[world.getWidth()][world.getHeight()];
        this.tiles = new Tile[world.getWidth()][world.getHeight()][world.getDepth()];

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                for (int z = 0; z < world.getDepth(); z++) {
                    tiles[x][y][z] = Tile.UNKNOWN;
                }
            }
        }
    }

    public Tile tile(int x, int y, int z) {
        return tiles[x][y][z];
    }

    public boolean isVisible(int x, int y, int z) {
        return z == depth && x >= 0 && y >= 0 && x < visible.length && y < visible[0].length && visible[x][y];
    }
}
