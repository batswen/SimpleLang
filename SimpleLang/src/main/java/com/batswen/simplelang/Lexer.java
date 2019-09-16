package com.batswen.simplelang;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Lexer {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private final String s;

    Map<String, TokenType> keywords = new HashMap<>();

    static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    Lexer(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("if", TokenType.Keyword_if);
        this.keywords.put("else", TokenType.Keyword_else);
        this.keywords.put("print", TokenType.Keyword_print);
        this.keywords.put("putc", TokenType.Keyword_putc);
        this.keywords.put("while", TokenType.Keyword_while);
    }
    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.End_of_input) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int)this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }
    Token char_lit(int line, int pos) {
        char c = getNextChar(); // skip opening quote
        int n = (int)c;
        if (c == '\'') {
            error(line, pos, "empty character constant");
        } else if (c == '\\') {
            c = getNextChar();
            switch (c) {
                case 'n':
                    n = 10;
                    break;
                case '\\':
                    n = '\\';
                    break;
                default:
                    error(line, pos, String.format("unknown escape sequence \\%c", c));
                    break;
            }
        }
        if (getNextChar() != '\'') {
            error(line, pos, "multi-character constant");
        }
        getNextChar();
        return new Token(TokenType.Integer, "" + n, line, pos);
    }
    Token string_lit(char start, int line, int pos) {
        String result = "";
        while (getNextChar() != start) {
            if (this.chr == '\u0000') {
                error(line, pos, "EOF while scanning string literal");
            }
            if (this.chr == '\n') {
                error(line, pos, "EOL while scanning string literal");
            }
            result += this.chr;
        }
        getNextChar();
        return new Token(TokenType.String, result, line, pos);
    }
    Token div_or_comment(int line, int pos) {
        if (getNextChar() != '*') {
            return new Token(TokenType.Op_divide, "", line, pos);
        }
        getNextChar();
        while (true) { 
            switch (this.chr) {
                case '\u0000':
                    error(line, pos, "EOF in comment");
                    break;
                case '*':
                    if (getNextChar() == '/') {
                        getNextChar();
                        return getToken();
                    }   break;
                default:
                    getNextChar();
                    break;
            }
        }
    }
    Token identifier_or_integer(int line, int pos) {
        boolean is_number = true;
        String text = "";

        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_') {
            text += this.chr;
            if (!Character.isDigit(this.chr)) {
                is_number = false;
            }
            getNextChar();
        }

        if (text.equals("")) {
            error(line, pos, String.format("identifer_or_integer unrecopgnized character: (%d) %c", (int)this.chr, this.chr));
        }

        if (Character.isDigit(text.charAt(0))) {
            if (!is_number) {
                    error(line, pos, String.format("invaslid number: %s", text));
            }
            return new Token(TokenType.Integer, text, line, pos);
        }

        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), "", line, pos);
        }
        return new Token(TokenType.Identifier, text, line, pos);
    }
    Token getToken() {
        int gtline, gtpos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        gtline = this.line;
        gtpos = this.pos;

        switch (this.chr) {
            case '\u0000': return new Token(TokenType.End_of_input, "", this.line, this.pos);
            case '/': return div_or_comment(gtline, gtpos);
            case '\'': return char_lit(gtline, gtpos);
            case '<': return follow('=', TokenType.Op_lessequal, TokenType.Op_less, gtline, gtpos);
            case '>': return follow('=', TokenType.Op_greaterequal, TokenType.Op_greater, gtline, gtpos);
            case '=': return follow('=', TokenType.Op_equal, TokenType.Op_assign, gtline, gtpos);
            case '!': return follow('=', TokenType.Op_notequal, TokenType.Op_not, gtline, gtpos);
            case '&': return follow('&', TokenType.Op_and, TokenType.End_of_input, gtline, gtpos);
            case '|': return follow('|', TokenType.Op_or, TokenType.End_of_input, gtline, gtpos);
            case '"': return string_lit(this.chr, gtline, gtpos);
            case '{': getNextChar(); return new Token(TokenType.LeftBrace, "", gtline, gtpos);
            case '}': getNextChar(); return new Token(TokenType.RightBrace, "", gtline, gtpos);
            case '(': getNextChar(); return new Token(TokenType.LeftParen, "", gtline, gtpos);
            case ')': getNextChar(); return new Token(TokenType.RightParen, "", gtline, gtpos);
            case '+': getNextChar(); return new Token(TokenType.Op_add, "", gtline, gtpos);
            case '-': getNextChar(); return new Token(TokenType.Op_subtract, "", gtline, gtpos);
            case '*': getNextChar(); return new Token(TokenType.Op_multiply, "", gtline, gtpos);
            case '%': getNextChar(); return new Token(TokenType.Op_mod, "", gtline, gtpos);
            case ';': getNextChar(); return new Token(TokenType.Semicolon, "", gtline, gtpos);
            case ',': getNextChar(); return new Token(TokenType.Comma, "", gtline, gtpos);

            default: return identifier_or_integer(gtline, gtpos);
        }
    }

    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    List<Token> getTokens() {
        Token t;
        List<Token> result = new ArrayList<>();
        while ((t = getToken()).tokentype != TokenType.End_of_input) {
            result.add(t);
        }
        result.add(t);
        return result;
    }
}
