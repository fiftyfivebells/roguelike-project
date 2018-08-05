package roguelike.items;

public class Inventory {

    private Item[] items;

    public Inventory(int max) {
        items = new Item[max];
    }

    public Item[] getItems() {
        return items;
    }

    public Item get(int i) {
        return items[i];
    }
}
