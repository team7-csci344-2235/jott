package provided;

/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 * @author Sebastian LaVine <sml1040@rit.edu>
 * @author Lianna Pottgen <lrp2755@rit.edu>
 **/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

// TODO: documentation (JavaDoc even) throughout.
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

    // TODO: this is just verbatim one of the example syntax errors that
    // the professor listed. We might want more general syntax errors too.
    // Can deal with that later, maybe.
    public static class SyntaxException extends Exception {
        public SyntaxException(String filename, int lineNumber,
                int found, String... expected) {
            // TODO: known: This will look weird if expected.length == 0
            super(String.format(
                    """
                    Syntax Error:
                    Invalid token %s. Expected %s
                    %s:%d""",
                    (Character.isValidCodePoint(found)
                     ? "\"%c\"".formatted(found) : Integer.toString(found)),
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
     * Calls PushbackReader::read, but also handles internal data (like
     * line number) depending on the character that is read.
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
	 */
	@Override
	public void unread(int c) throws IOException {
	    if (c == '\n') {
	        this.lineNumber--;
	    }
	    super.unread(c);
	}

    private Token tokenFrom(String s, TokenType type) {
        return new Token(s, this.filename, this.lineNumber, type);
    }

	private Token start() throws IOException, SyntaxException {
	    // Note that many of these paths return early.
	    // EOF is not the only way to leave this loop.
	    for (int c = this.read(); c != -1 /* EOF */; c = this.read())
	    {
            //whitespace
	        if (Character.isWhitespace(c))
	        {
	            continue;
	        }
            //comment
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
	            StringBuffer sb = new StringBuffer();
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
                        if (nc != -1) {
                            // If we haven't reached the end of the file,
                            // then we'll be seeing this character again.
                            // It's important that we actually *don't* pass
                            // -1 to unread, by the way -- see commit 502e294.
                            this.unread(nc);
                        }
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
                    // Just one ":" => colon.
                    if (nc != -1) {
                        this.unread(nc);
                    }
                    return this.tokenFrom(":", TokenType.COLON);
                }
            }
            // TODO: In between these: the branches for the rest of the
	        // tokens we need to lex.
            
            else if(c == '='){
                int nc = this.read();
                if(nc == '='){
                    //we have ==, which means we have an relOp
                    return this.tokenFrom("==", TokenType.REL_OP);
                }
                else{
                    //we have =, which means we have assign
                    if(nc != -1){
                        this.unread(nc);
                    }
                    return this.tokenFrom("=", TokenType.ASSIGN);
                }
            }

            else if(c == '<'){
                int nc = this.read();
                if(nc == '='){
                    //we have <=, which means we have an relOp
                    return this.tokenFrom("<=", TokenType.REL_OP);
                }
                else{
                    //we have < we have an error
                    if(nc != -1){
                        this.unread(nc);
                    }
                    return this.tokenFrom("<", TokenType.REL_OP);
                }
            }

            else if(c == '>'){
                int nc = this.read();
                if(nc == '='){
                    //we have <=, which means we have an relOp
                    return this.tokenFrom(">=", TokenType.REL_OP);
                }
                else{
                    //we have < we have an error
                    if(nc != -1){
                        this.unread(nc);
                    }
                    return this.tokenFrom(">", TokenType.REL_OP);
                }
            }

            else if(c == '!'){
                int nc = this.read();
                if(nc == '='){
                    return this.tokenFrom("!=", TokenType.REL_OP);
                }
                else{
                    //we have an error??????
                    if(nc != -1){
                        this.unread(nc);
                    }
                    throw new SyntaxException(this.filename, this.lineNumber, c, "=");
                }
            }

	        else
	        {
                // An invalid character was found.
                throw new SyntaxException(this.filename, this.lineNumber, c,
                        "#...", ",", "]", "[", "}", "{", "=", "<", ">", "/", "+",
                        "-", "*", ";", ".", "[0-9]", "[a-zA-z]", ":", "!", "\""
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

    // TODO: Once further along on this, we should remove this and rely on the
    // test suite from the professor. Maybe.
    public static void main(String[] args) {
        var tokens = tokenize("test_stuff.txt");
        if (tokens != null) {
            System.out.println(String.join(" ",
                tokens.stream().map(Token::getToken).toList())
            );
        }
    }
}
