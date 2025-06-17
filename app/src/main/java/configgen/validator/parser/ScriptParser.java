package configgen.validator.parser;

import configgen.validator.ast.*;

import java.util.List;

import static configgen.validator.parser.TokenType.*;

/**
 * A simple recursive descent parser that constructs an AST from a list of
 * tokens.
 * Grammar:
 * expression → logical_or
 * logical_or → logical_and ( "||" logical_and )*
 * logical_and → equality ( "&&" equality )*
 * equality → comparison ( ( "!=" | "==" ) comparison )*
 * comparison → primary ( ( ">" | ">=" | "<" | "<=" ) primary )*
 * primary → NUMBER | IDENTIFIER ( "." IDENTIFIER )?
 */
public class ScriptParser {
    private final List<Token> tokens;
    private int current = 0;

    public ScriptParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expression parse() {
        try {
            return expression();
        } catch (Exception e) {
            throw new ParserException("Failed to parse expression", e);
        }
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr() {
        Expression expr = logicalAnd();
        while (match(OR)) {
            Token operator = previous();
            Expression right = logicalAnd();
            expr = new BinaryExpr(expr, BinaryExpr.Operator.fromSymbol(operator.lexeme()), right);
        }
        return expr;
    }

    private Expression logicalAnd() {
        Expression expr = equality();
        while (match(AND)) {
            Token operator = previous();
            Expression right = equality();
            expr = new BinaryExpr(expr, BinaryExpr.Operator.fromSymbol(operator.lexeme()), right);
        }
        return expr;
    }

    private Expression equality() {
        Expression expr = comparison();
        while (match(EQUAL, NOT_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expr = new BinaryExpr(expr, BinaryExpr.Operator.fromSymbol(operator.lexeme()), right);
        }
        return expr;
    }

    private Expression comparison() {
        Expression expr = primary();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expression right = primary();
            expr = new BinaryExpr(expr, BinaryExpr.Operator.fromSymbol(operator.lexeme()), right);
        }
        return expr;
    }

    private Expression primary() {
        if (match(NUMBER)) {
            return new LiteralExpr(previous().literal());
        }

        if (match(IDENTIFIER)) {
            Token identifier = previous();
            if (match(DOT)) {
                Token property = consume(IDENTIFIER, "Expect property name after '.'.");
                return new FieldAccessExpr(identifier.lexeme(), property.lexeme());
            }
            return new FieldAccessExpr(identifier.lexeme());
        }

        throw new ParserException("Expect expression, got " + peek().type() + " at position " + peek().position());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw new ParserException(message + " Got " + peek().type() + " at position " + peek().position());
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type() == type;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}