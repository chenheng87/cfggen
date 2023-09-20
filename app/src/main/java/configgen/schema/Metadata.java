package configgen.schema;

import java.util.Objects;
import java.util.SequencedMap;



public record Metadata(SequencedMap<String, MetaValue> data) {

    public Metadata {
        Objects.requireNonNull(data);
    }


    public sealed interface MetaValue {
    }

    public enum MetaTag implements MetaValue {
        TAG
    }

    public record MetaInt(int value) implements MetaValue {
    }

    public record MetaFloat(float value) implements MetaValue {
    }

    public record MetaStr(String value) implements MetaValue {
    }

}
