package roguelike.world;

public class WorldBuilder {
    private int width;
    private int height;
    private int depth;
    private Tile[][][] tiles;

    public WorldBuilder(int width, int height, int depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.tiles = new Tile[width][height][depth];
    }

    public World build() {
        return new World(tiles);
    }

    public WorldBuilder createRegions() {
        int[][][] regions = new int[width][height][depth];

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (tiles[x][y][z] != Tile.WALL && regions[x][y][z] == 0) {
                        int size = fillRegion(nextRegion++, x, y, z);

                        if (size < 25) {
                            removeRegion(nextRegion - 1, z);
                        }
                    }
                }
            }
        }
        return this;
    }

    private WorldBuilder randomizeTiles() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
            }
        }
        return this;
    }

    private WorldBuilder smoothTiles(int repeat) {
        Tile[][] tiles2 = new Tile[width][height];

        for (int round = 0; round < repeat; round++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int floors = 0;
                    int rocks = 0;

                    for (int ox = -1; ox < 2; ox++) {
                        for (int oy = -1; oy < 2; oy++) {
                            if (i + ox < 0 || i + ox >= width || j + oy < 0 || j + oy >= height) {
                                continue;
                            }
                            if (tiles[i + ox][j + oy] == Tile.FLOOR) {
                                floors++;
                            } else {
                                rocks++;
                            }
                        }
                    }
                    tiles2[i][j]= floors >= rocks ? Tile.FLOOR : Tile.WALL;
                }
            }
            tiles = tiles2;
        }
        return this;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smoothTiles(8);
    }
}
