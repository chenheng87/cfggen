package configgen.validator.ast;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a binary operation (e.g., addition, comparison, logical AND).
 */
public record BinaryExpr(Expression left, Operator op, Expression right) implements Expression {

    public enum Operator {
        EQUAL("=="),
        NOT_EQUAL("!="),
        GREATER(">"),
        GREATER_EQUAL(">="),
        LESS("<"),
        LESS_EQUAL("<="),
        AND("&&"),
        OR("||");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public static Operator fromSymbol(String s) {
            for (Operator op : values()) {
                if (op.symbol.equals(s)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Unknown operator symbol: " + s);
        }
    }

    public BinaryExpr {
        Objects.requireNonNull(left);
        Objects.requireNonNull(op);
        Objects.requireNonNull(right);
    }

    @Override
    public Object evaluate(EvaluationContext context) {
        Object leftVal = left.evaluate(context);
        // Short-circuit for logical operators
        if (op == Operator.AND && !isTrue(leftVal)) {
            return false;
        }
        if (op == Operator.OR && isTrue(leftVal)) {
            return true;
        }

        Object rightVal = right.evaluate(context);

        return switch (op) {
            case AND -> isTrue(leftVal) && isTrue(rightVal);
            case OR -> isTrue(leftVal) || isTrue(rightVal);
            case EQUAL -> isEqual(leftVal, rightVal);
            case NOT_EQUAL -> !isEqual(leftVal, rightVal);
            case GREATER, GREATER_EQUAL, LESS, LESS_EQUAL -> compare(leftVal, rightVal);
        };
    }

    private boolean isTrue(Object obj) {
        if (obj instanceof Boolean b) {
            return b;
        }
        throw new ClassCastException("Cannot cast " + obj.getClass().getName() + " to Boolean for logical operation.");
    }

    private boolean isEqual(Object left, Object right) {
        if (left instanceof Number l && right instanceof Number r) {
            return toBigDecimal(l).compareTo(toBigDecimal(r)) == 0;
        }
        return Objects.equals(left, right);
    }

    private boolean compare(Object left, Object right) {
        if (left instanceof Number l && right instanceof Number r) {
            int cmp = toBigDecimal(l).compareTo(toBigDecimal(r));
            return switch (op) {
                case GREATER -> cmp > 0;
                case GREATER_EQUAL -> cmp >= 0;
                case LESS -> cmp < 0;
                case LESS_EQUAL -> cmp <= 0;
                default -> throw new IllegalStateException("Unexpected comparison operator: " + op);
            };
        }
        throw new ClassCastException("Cannot compare non-numeric types: " +
                left.getClass().getName() + ", " + right.getClass().getName());
    }

    private BigDecimal toBigDecimal(Number n) {
        if (n instanceof BigDecimal bd) {
            return bd;
        }
        return new BigDecimal(n.toString());
    }
}