package config.ai.triggertick;

public final class ConstValue implements config.ai.triggertick.TriggerTick {
    @Override
    public config.ai.Triggerticktype type() {
        return config.ai.Triggerticktype.CONSTVALUE;
    }

    private int value;

    private ConstValue() {
    }

    public ConstValue(int value) {
        this.value = value;
    }

    public static ConstValue _create(configgen.genjava.ConfigInput input) {
        ConstValue self = new ConstValue();
        self.value = input.readInt();
        return self;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ConstValue))
            return false;
        ConstValue o = (ConstValue) other;
        return value == o.value;
    }

    @Override
    public String toString() {
        return "ConstValue(" + value + ")";
    }

}
