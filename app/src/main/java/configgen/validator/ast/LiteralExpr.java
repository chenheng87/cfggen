package configgen.validator.ast;

import java.util.Objects;

/**
 * Represents a literal value (e.g., a number or a string) in an expression.
 */
public record LiteralExpr(Object value) implements Expression {

    public LiteralExpr {
        Objects.requireNonNull(value);
    }

    @Override
    public Object evaluate(EvaluationContext context) {
        return value;
    }
}