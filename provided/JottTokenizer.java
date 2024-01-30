package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Ethan Hartman <ehh4525@rit.edu>
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class JottTokenizer {
    private final static String INVALID_CHARACTER = "Invalid character '%s' in file '%s' on line %s.\n";

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){
        ArrayList<Token> tokens = null;
        try (Scanner scanner = new Scanner(new File(filename))) { // Automatic resource management file scanner
            int lineNum = 1;
            tokens = new ArrayList<>();
            while (scanner.hasNextLine())
                start(tokens, new LazyIterator(scanner.nextLine(), filename, lineNum++));
        } catch (FileNotFoundException e) {
            System.out.println("Could not tokenize file '" + filename + "' since it was not found.");
        }

        return tokens;
    }

    private static void start(ArrayList<Token> tokens, LazyIterator iterator) {
        for (char c : iterator) {
            Token token = null;
            switch (c) {
                case ' ':
                    continue; // Whitespace, ignore the rest of the iteration.
                case ',', '[', ']', '{', '}':
                    // start to standalone could be done in this line tbh...
                    break;
                case ';':
                    break;
                default:
                    if (Character.isLetter(c)) {
                        token = startToLetter(iterator, c);
                    } else if (Character.isDigit(c)) {
                        // Do digit processing here
                    }
            }

            if (token != null)
                tokens.add(token);
            else
                System.out.printf(INVALID_CHARACTER, c, iterator.getFilePath(), iterator.getLineNum()); // Probably throw an error in the future
        }
    }

    private static Token startToLetter(LazyIterator iterator, char prevLetter) {
        StringBuilder builder = new StringBuilder().append(prevLetter);
        for (char c : iterator) {
            if (Character.isLetter(c) || Character.isDigit(c))
                builder.append(c);
            else {
                iterator.back(); // Go back because we have encountered a character for something else
                break;
            }
        }

        return new Token(builder.toString(), iterator.getFilePath(), iterator.getLineNum(), TokenType.ID_KEYWORD);
    }

    public static void main(String[] args) {
        for (Token t : tokenize("test_stuff.txt"))
            System.out.println(t.getToken());
    }
}
