package config.task.completecondition;

public final class CollectItem implements config.task.completecondition.Completecondition {
    @Override
    public config.task.Completeconditiontype type() {
        return config.task.Completeconditiontype.COLLECTITEM;
    }

    private int itemid;
    private int count;

    private CollectItem() {
    }

    public CollectItem(int itemid, int count) {
        this.itemid = itemid;
        this.count = count;
    }

    public static CollectItem _create(configgen.genjava.ConfigInput input) {
        CollectItem self = new CollectItem();
        self.itemid = input.readInt();
        self.count = input.readInt();
        return self;
    }

    public int getItemid() {
        return itemid;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(itemid, count);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CollectItem))
            return false;
        CollectItem o = (CollectItem) other;
        return itemid == o.itemid && count == o.count;
    }

    @Override
    public String toString() {
        return "CollectItem(" + itemid + "," + count + ")";
    }

}
