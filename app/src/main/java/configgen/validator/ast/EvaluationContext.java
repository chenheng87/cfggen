package configgen.validator.ast;

import configgen.schema.FieldSchema;
import configgen.schema.Structural;
import configgen.value.CfgValue;

import java.util.Objects;

/**
 * Holds the context for evaluating an expression, primarily the data of a
 * single row (VStruct).
 * It provides a method to resolve field values by name.
 */
public class EvaluationContext {
    private final CfgValue.VStruct row;
    private final Structural schema;

    public EvaluationContext(CfgValue.VStruct row) {
        this.row = Objects.requireNonNull(row);
        this.schema = row.schema();
    }

    /**
     * Retrieves the value of a field by its name from the current row.
     *
     * @param fieldName The name of the field.
     * @return The field's value (as a CfgValue.Value).
     * @throws IllegalArgumentException if the field name does not exist in the
     *                                  schema.
     */
    public CfgValue.Value getFieldValue(String fieldName) {
        for (int i = 0; i < schema.fields().size(); i++) {
            FieldSchema field = schema.fields().get(i);
            if (field.name().equals(fieldName)) {
                return row.values().get(i);
            }
        }
        throw new IllegalArgumentException("Field '" + fieldName + "' not found in table '" + schema.name() + "'.");
    }
}