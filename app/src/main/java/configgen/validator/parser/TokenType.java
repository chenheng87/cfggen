package configgen.validator.parser;

public enum TokenType {
    // Single-character tokens
    DOT,

    // Operators
    EQUAL, NOT_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // Logical
    AND,
    OR,

    // Keywords (if any in the future)

    // Other
    EOF
}