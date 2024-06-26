package provided;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author Adrienne Ressy <amr3032@rit.edu>
 * @author Ethan Hartman <ehh4525@rit.edu>
 * @author Ewen Cazuc <ec1291@rit.edu>
 * @author Lianna Pottgen <lrp2755@rit.edu>
 * @author Sebastian LaVine <sml1040@rit.edu>
 **/
public class JottTokenizer extends PushbackReader {
    // Some tokens only ever consist of one character, and no other token
    // contains that character. This map simply (and quickly) maps between
    // those single characters and their corresponding TokenType values.
    //
    // The `int` cast is required for the key check in start() to work with
    // an `int` from read().
    private static final Map<Integer, TokenType> simpleTokens = Map.of(
        (int) ',', TokenType.COMMA,
        (int) ']', TokenType.R_BRACKET,
        (int) '[', TokenType.L_BRACKET,
        (int) '}', TokenType.R_BRACE,
        (int) '{', TokenType.L_BRACE,
        (int) '/', TokenType.MATH_OP,
        (int) '+', TokenType.MATH_OP,
        (int) '-', TokenType.MATH_OP,
        (int) '*', TokenType.MATH_OP,
        (int) ';', TokenType.SEMICOLON
    );

    private final String filename;
    private int lineNumber;

    /**
     * Constructs a new JottTokenizer.
     * @param filename path to Jott source file to read from
     * @return a new JottTokenizer
     */
    public JottTokenizer(String filename) throws FileNotFoundException {
        // Worried that the BufferedReader and FileReader need to be closed,
        // but we're not saving a reference to them? I was too. But it looks
        // like PushbackReader::close will automatically close the underlying
        // reader, as is the case with BufferedReader::close. So I think we're
        // fine here.
        super(new BufferedReader(new FileReader(filename)));

        this.filename = filename;
        this.lineNumber = 1;
    }

    /**
     * An Exception that represents syntax errors.
     */
    public static class SyntaxException extends Exception {
        /**
         * Constructs a new SyntaxException.
         * @param filename name of the file of the syntax error
         * @param lineNumber line number in filename of the syntax error
         * @param found the character that was read to cause the error
         * @param expected strings representing valid characters for the token
         */
        public SyntaxException(String filename, int lineNumber,
                int found, String... expected) {
            // TODO: known: This will look weird if expected.length == 0
            super(String.format(
                    """
                    Syntax Error:
                    Invalid input %s. Expected %s
                    %s:%d""",
                    (Character.isValidCodePoint(found)
                     && !Character.isISOControl(found)
                     ? "'%c'".formatted(found) : Integer.toString(found)),
                    // Turns the expected chars into something like
                    // ` "+", "-", "/", "*" `
                    String.join(", ",
                        Arrays.stream(expected)
                        .map(s -> "\"" + s + "\"").toList()),
                    filename, lineNumber
                )
            );
        }
    }

    /**
     * Constructs a SyntaxException from the parameters as well as the
     * object's internal state.
     *
     * @param found the invalid character that was read
     * @param expected strings describing characters that would have been valid
     * @return a new SyntaxException
     */
    private SyntaxException syntaxExcFrom(int found, String... expected) {
        return new SyntaxException(this.filename, this.lineNumber,
                found, expected);
    }

    /**
     * Calls PushbackReader::read, but also handles internal data (like
     * line number) depending on the character that is read.
     * @return a character read, or -1 on EOF
     */
    @Override
    public int read() throws IOException {
        int c = super.read();
        if (c == '\n') {
            this.lineNumber++;
        }
        return c;
    }

    /**
     * Calls PushbackReader::unread, but also handles internal data (like
     * line number) depending on the character that is read.
     * @param c character to unread
     */
    @Override
    public void unread(int c) throws IOException {
        if (c == '\n') {
            this.lineNumber--;
        }
        super.unread(c);
    }

    /**
     * Calls unread with a check that c is not -1.
     * PushbackReader::unread will treat -1 as U+FFFF, breaking future reads.
     * @param c character to unread
     */
    public void safeUnread(int c) throws IOException {
        if (c != -1) {
            this.unread(c);
        }
    }

    /**
     * Constructs a Token from the parameters as well as the object's internal
     * state.
     * @param s string representation of the token
     * @param type type of the token
     * @return a new token
     */
    private Token tokenFrom(String s, TokenType type) {
        return new Token(s, this.filename, this.lineNumber, type);
    }

    /**
     * Determines whether a state is a valid number after reading a period.
     * Note that this is not the only state that can return a valid number.
     * See the number branch in the main loop in start() below.
     *
     * @param sb Contains the data that has been read from the token so far
     * @return a number token
     */
    private Token numberFromPeriod(StringBuilder sb)
            throws IOException, SyntaxException {
        int c;
        for (c = this.read(); Character.isDigit(c); c = this.read())
        {
            // We've found more number.
            sb.appendCodePoint(c);
        }
        this.safeUnread(c);
        String tok = sb.toString();
        return this.tokenFrom(tok, TokenType.NUMBER);
    }

    /**
     * The first state representing the tokenization DFA, and branches that
     * follow.
     * @return the next token in the file
     */
    private Token start() throws IOException, SyntaxException {
        // Note that many of these paths return early.
        // EOF is not the only way to leave this loop.
        for (int c = this.read(); c != -1 /* EOF */; c = this.read())
        {
            if (Character.isWhitespace(c))
            {
                // All whitespace is ignored.
                continue;
            }
            else if (c == '#')
            {
                // Line comment.
                for (;;) {
                    int nc = this.read();
                    if (nc == -1)
                    {
                        return null; // EOF -- no token found.
                    }
                    else if (nc == '\n')
                    {
                        break; // Newline -- end of comment.
                    }
                }
            }
            else if (simpleTokens.containsKey(c))
            {
                // A handful of simple, one-character long tokens.
                // ',', ']', '[', '}', '{', '/', '+', '-', '*', and ';'.
                // See map definition at top of file.
                String tok = Character.toString(c);
                return this.tokenFrom(tok, simpleTokens.get(c));
            }
            else if (Character.isLetter(c))
            {
                // id/keyword. We must lex the longest possible token.
                StringBuilder sb = new StringBuilder();
                sb.appendCodePoint(c);
                for (;;) {
                    int nc = this.read();
                    if (Character.isLetterOrDigit(nc))
                    {
                        // We've found more of the token! Let's keep going.
                        sb.appendCodePoint(nc);
                    }
                    else
                    {
                        this.safeUnread(nc);
                        // This token's not getting any longer.
                        String tok = sb.toString();
                        return this.tokenFrom(tok, TokenType.ID_KEYWORD);
                    }
                }
            }
            else if (c == ':')
            {
                // colon or fcHeader. Let's find out.
                int nc = this.read();
                if (nc == ':')
                {
                    // "::" => fcHeader.
                    return this.tokenFrom("::", TokenType.FC_HEADER);
                }
                else
                {
                    this.safeUnread(nc);
                    return this.tokenFrom(":", TokenType.COLON);
                }
            }
            else if (c == '.')
            {
                // number (with a leading decimal point).
                // To be a valid token, the next character must be a digit.
                int nc = this.read();
                if (Character.isDigit(nc))
                {
                    // Continue reading digits.
                    StringBuilder sb = new StringBuilder();
                    sb.appendCodePoint('.');
                    sb.appendCodePoint(nc);
                    return this.numberFromPeriod(sb);
                }
                else
                {
                    // Not a number.
                    throw this.syntaxExcFrom(nc, "[0-9]");
                }
            }
            else if (Character.isDigit(c))
            {
                // number.
                StringBuilder sb = new StringBuilder();
                sb.appendCodePoint(c);
                for (;;) {
                    int nc = this.read();
                    if (nc == '.')
                    {
                        // We can read more numbers, but not another period.
                        sb.appendCodePoint('.');
                        return this.numberFromPeriod(sb);
                    }
                    else if (Character.isDigit(nc))
                    {
                        sb.appendCodePoint(nc);
                    }
                    else
                    {
                        this.safeUnread(nc);
                        String tok = sb.toString();
                        return this.tokenFrom(tok, TokenType.NUMBER);
                    }
                }
            }
            else if (c == '\"')
            {
                // string.
                StringBuilder sb = new StringBuilder();
                sb.appendCodePoint(c);
                for (;;) {
                    int nc = this.read();
                    if (Character.isLetterOrDigit(nc) || nc == ' ')
                    {
                        sb.appendCodePoint(nc);
                    }
                    else if (nc == '\"')
                    {
                        // End of the string.
                        sb.appendCodePoint('\"');
                        String tok = sb.toString();
                        return this.tokenFrom(tok, TokenType.STRING);
                    }
                    else
                    {
                        throw this.syntaxExcFrom(nc, "[A-za-z]", "[0-9]",
                                " ", "\"");
                    }
                }
            }
            else if (c == '=')
            {
                // assign or relOp (equality).
                int nc = this.read();
                if (nc == '=')
                {
                    // "==" => relOp (equality).
                    return this.tokenFrom("==", TokenType.REL_OP);
                }
                else
                {
                    // Just one "=" => assign.
                    this.safeUnread(nc);
                    return this.tokenFrom("=", TokenType.ASSIGN);
                }
            }
            else if (c == '<')
            {
                // relOp: less-than or less-than-or-equal-to.
                int nc = this.read();
                if (nc == '=')
                {
                    // "<=" => less-than-or-equal-to.
                    return this.tokenFrom("<=", TokenType.REL_OP);
                }
                else
                {
                    // "<" => less-than.
                    this.safeUnread(nc);
                    return this.tokenFrom("<", TokenType.REL_OP);
                }
            }
            else if (c == '>')
            {
                // relOp: greater-than or greater-than-or-equal-to.
                int nc = this.read();
                if (nc == '=')
                {
                    // ">=" => greater-than-or-equal-to.
                    return this.tokenFrom(">=", TokenType.REL_OP);
                }
                else
                {
                    // ">" => greater-than.
                    this.safeUnread(nc);
                    return this.tokenFrom(">", TokenType.REL_OP);
                }
            }
            else if (c == '!')
            {
                // relOp (not-equal).
                int nc = this.read();
                if (nc == '=')
                {
                    return this.tokenFrom("!=", TokenType.REL_OP);
                }
                else
                {
                    throw this.syntaxExcFrom(nc, "=");
                }
            }
            else
            {
                // An invalid character was found.
                throw this.syntaxExcFrom(c, "#...", ",", "]", "[", "}", "{",
                        "=", "<", ">", "/", "+", "-", "*", ";", ".", "[0-9]",
                        "[a-zA-z]", ":", "!", "\""
                );
            }
        }

        // EOF was found before a token could be found.
        return null;
    }

    /**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
        try (JottTokenizer tokenizer = new JottTokenizer(filename))
        {
            ArrayList<Token> tokens = new ArrayList<>();
            Token t;

            while ((t = tokenizer.start()) != null) {
                tokens.add(t);
            }

            return tokens;
        }
        catch (FileNotFoundException ex) {
            System.err.printf("Error opening file: %s\n", ex.getMessage());
            return null;
        } catch (IOException | SyntaxException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
