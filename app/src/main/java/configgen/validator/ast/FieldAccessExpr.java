package configgen.validator.ast;

import configgen.value.CfgValue;
import java.util.List;
import java.util.Objects;

/**
 * Represents accessing a field and optionally a property of that field (e.g.,
 * "fieldName.length").
 */
public record FieldAccessExpr(String fieldName, String property) implements Expression {

    public FieldAccessExpr {
        Objects.requireNonNull(fieldName);
    }

    public FieldAccessExpr(String fieldName) {
        this(fieldName, null);
    }

    @Override
    public Object evaluate(EvaluationContext context) {
        CfgValue.Value fieldValue = context.getFieldValue(fieldName);

        Object unwrappedValue = unwrap(fieldValue);

        if (property == null) {
            return unwrappedValue;
        }

        return switch (property) {
            case "len", "length", "size" -> getLength(unwrappedValue);
            default -> throw new IllegalStateException("Unsupported property: " + property);
        };
    }

    private static Object unwrap(CfgValue.Value value) {
        return switch (value) {
            case CfgValue.VBool v -> v.value();
            case CfgValue.VInt v -> v.value();
            case CfgValue.VLong v -> v.value();
            case CfgValue.VFloat v -> v.value();
            case CfgValue.VString v -> v.value();
            case CfgValue.VText v -> v.value();
            case CfgValue.VList v -> v.valueList(); // Keep as list for .size
            case CfgValue.VMap v -> v.valueMap(); // Keep as map for .size
            // VStruct and VInterface are not directly unwrapped to a primitive.
            // Further property access would need to be defined if required.
            default -> value;
        };
    }

    private static int getLength(Object value) {
        if (value instanceof String s) {
            return s.length();
        }
        if (value instanceof List<?> l) {
            return l.size();
        }
        // Could add support for Map size if needed
        throw new IllegalStateException("Cannot get length of type: " + value.getClass().getName());
    }
}