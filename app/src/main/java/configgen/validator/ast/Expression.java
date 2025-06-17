package configgen.validator.ast;

/**
 * An expression node in the validation script's Abstract Syntax Tree (AST).
 * Each node must be evaluable to a result.
 */
@FunctionalInterface
public interface Expression {
    /**
     * Evaluates the expression against a given row of data.
     *
     * @param context The evaluation context, providing data and services.
     * @return The result of the evaluation, typically a Boolean for validation
     *         rules.
     */
    Object evaluate(EvaluationContext context);
}