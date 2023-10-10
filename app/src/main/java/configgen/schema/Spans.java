package configgen.schema;

import java.util.OptionalInt;

import static configgen.schema.FieldFormat.AutoOrPack.PACK;
import static configgen.schema.FieldType.*;
import static configgen.schema.Metadata.*;

public class Spans {

    public static void preCalculateAllSpan(CfgSchema schema) {
        for (Nameable item : schema.items()) {
            switch (item) {
                case InterfaceSchema interfaceSchema -> {
                    for (StructSchema impl : interfaceSchema.impls()) {
                        for (FieldSchema field : impl.fields()) {
                            span(field);
                        }
                        span(impl);
                    }
                    span(interfaceSchema);
                }
                case Structural structural -> {
                    for (FieldSchema field : structural.fields()) {
                        span(field);
                    }
                    span(structural);
                }
            }
        }
    }

    static final String SPAN = "__span";

    public static int span(Nameable nameable) {
        Metadata meta = nameable.meta();
        if (meta.get(SPAN) instanceof MetaInt vi) {
            return vi.value();
        }

        int s = calcSpan(nameable);
        meta.putInt(SPAN, s);
        return s;
    }

    public static int calcSpan(Nameable nameable) {
        Metadata meta = nameable.meta();
        MetaValue value = meta.data().get(SPAN);
        if (value instanceof MetaInt vi) {
            return vi.value();
        }

        if (nameable.fmt() == PACK || nameable.fmt() instanceof FieldFormat.Sep) {
            return 1;
        }

        switch (nameable) {
            case InterfaceSchema interfaceSchema -> {
                OptionalInt max = interfaceSchema.impls().stream().mapToInt(Spans::span).max();
                if (max.isPresent()) {
                    return max.getAsInt() + 1;
                } else {
                    return 1;
                }
            }
            case Structural structural -> {
                return structural.fields().stream().mapToInt(Spans::span).sum();
            }
        }
    }

    public static int span(FieldSchema field) {
        Metadata meta = field.meta();
        if (meta.get(SPAN) instanceof MetaInt vi) {
            return vi.value();
        }

        int s = calcSpan(field);
        meta.putInt(SPAN, s);
        return s;
    }

    public static int calcSpan(FieldSchema field) {
        FieldFormat fmt = field.fmt();

        switch (fmt) {
            case PACK:
            case FieldFormat.Sep _:
                return 1;
            default:
                break;
        }

        switch (field.type()) {
            case Primitive _ -> {
                return 1;
            }

            case StructRef structRef -> {
                return span(structRef.obj());
            }

            case FList flist -> {
                switch (fmt) {
                    case FieldFormat.Block block -> {
                        return span(flist.item()) * block.fix();

                    }
                    case FieldFormat.Fix fix -> {
                        return span(flist.item()) * fix.count();
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + fmt);
                }

            }
            case FMap fmap -> {
                switch (fmt) {
                    case FieldFormat.Block block -> {
                        return (span(fmap.key()) + span(fmap.value())) *
                                block.fix();

                    }
                    case FieldFormat.Fix fix -> {
                        return (span(fmap.key()) + span(fmap.value())) *
                                fix.count();
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + fmt);
                }
            }
        }
    }

    public static int span(SimpleType type) {
        switch (type) {
            case Primitive _ -> {
                return 1;
            }
            case StructRef structRef -> {
                return span(structRef.obj());
            }
        }
    }

}
