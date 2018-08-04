package roguelike.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point {
    private int x;
    private int y;
    private int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public List<Point> neighbors() {
        List<Point> points = new ArrayList<Point>();

        for (int ox = -1; ox < 2; ox++) {
            for (int oy = -1; oy < 2; oy++) {
                if (ox == 0 && oy == 0) { continue; }

                points.add(new Point(x+ox, y+oy, z));
            }
        }
        Collections.shuffle(points);
        return points;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point other = (Point) o;

        if (x != other.getX()) {
            return false;
        }

        if (y != other.getY()) {
            return false;
        }

        if (z != other.getZ()) {
            return false;
        }

        return true;
    }
}
