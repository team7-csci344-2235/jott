package provided;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Jott {
    private static final HashSet<String> CONVERSION_LANGUAGES = new HashSet<>(List.of(new String[]{"jott", "java", "c", "python"}));
    public static void main(String[] args) throws JottTree.NodeValidateException {
        if (args.length != 3) {
            System.err.println("Invalid argument length. Usage: Jott <input_filename> <output_filename> <language>");
            return;
        }

        File inputFile = new File(args[0]);
        if (!inputFile.exists() || inputFile.isDirectory()) {
            System.err.printf("Cannot read file: '%s'%n", args[0]);
            return;
        }

        File outputFile = new File(args[1]);
        if (outputFile.isDirectory()) {
            System.err.print("Output must be a file, not a directory.%n");
            return;
        }

        String conversionLanguage = args[2];
        if (!CONVERSION_LANGUAGES.contains(conversionLanguage.toLowerCase())) {
            System.err.printf("Cannot convert to '%s'. This language is not supported.%n", conversionLanguage);
            System.err.print("Supported languages: " + String.join(", ", Arrays.stream(CONVERSION_LANGUAGES.toArray()).map(s -> "\"" + s + "\"").toList()) + "\n");
            return;
        }

        // Gather input file tokens
        ArrayList<Token> tokens = JottTokenizer.tokenize(inputFile.getAbsolutePath());

        // Build parse tree then validate
        JottTree parseTree = JottParser.parse(tokens, true);
        if (parseTree == null) return;

        // Write to output
        try (FileWriter out = new FileWriter(outputFile.getAbsoluteFile())) {
           out.write(switch (conversionLanguage.toLowerCase()) {
               case "java" -> parseTree.convertToJava(outputFile.getName());
               //NOTE HERE!!!!
               case "python" -> parseTree.convertToPython(0);
                             case "c" -> parseTree.convertToC();
               case "jott" -> parseTree.convertToJott();
               default -> throw new IllegalStateException("Unexpected value: " + conversionLanguage.toLowerCase());
           });
        } catch (IOException e) {
            System.err.printf("The file '%s' could not be written to.", outputFile.getName());
        }
    }
}
