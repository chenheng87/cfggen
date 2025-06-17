package configgen.validator.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ScriptLexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;

    public ScriptLexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, source.length()));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '.':
                addToken(TokenType.DOT);
                break;
            case '=':
                if (match('='))
                    addToken(TokenType.EQUAL);
                break;
            case '!':
                if (match('='))
                    addToken(TokenType.NOT_EQUAL);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '&':
                if (match('&'))
                    addToken(TokenType.AND);
                break;
            case '|':
                if (match('|'))
                    addToken(TokenType.OR);
                break;
            case ' ', '\r', '\t', '\n':
                // Ignore whitespace
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new LexerException("Unexpected character: " + c + " at position " + (current - 1));
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(start, current);
        addToken(TokenType.IDENTIFIER, text);
    }

    private void number() {
        while (isDigit(peek()))
            advance();
        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // Consume the "."
            while (isDigit(peek()))
                advance();
        }
        String text = source.substring(start, current);
        addToken(TokenType.NUMBER, new BigDecimal(text));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, start));
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}