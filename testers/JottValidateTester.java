package testers;

/*
  Jott validate tester. This will test the validation phase of the Jott
  project.

  This tester assumes a working and valid tokenizer and parser.

  We tacked on our validation phase to our parser, so this will still just
  look like we're calling our parser. Not too many changes required. --seb
 */

import provided.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JottValidateTester {
    ArrayList<TestCase> testCases;

    private static class TestCase{
        String testName;
        String fileName;
        boolean error;

        public TestCase(String testName, String fileName, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.error = error;
        }
    }

    private boolean tokensEqualNoFileData(Token t1, Token t2){
        return t1.getTokenType() == t2.getTokenType() &&
                t1.getToken().equals(t2.getToken());
    }

    private void createTestCases(){
        this.testCases = new ArrayList<>();
        testCases.add(new TestCase("invalid param (type mismatch)", "funcCallParamInvalid.jott", true ));
        testCases.add(new TestCase("function not defined", "funcNotDefined.jott", true ));
        testCases.add(new TestCase("integer relop double", "funcReturnInExpr.jott", true ));
        testCases.add(new TestCase("return wrong type", "funcWrongParamType.jott", true ));
        testCases.add(new TestCase("hello world", "helloWorld.jott", false ));
        testCases.add(new TestCase("if statement returns", "ifStmtReturns.jott", false ));
        testCases.add(new TestCase("larger", "largerValid.jott", false ));
        testCases.add(new TestCase("main must return Void", "mainReturnNotInt.jott", true ));
        testCases.add(new TestCase("mismatched return", "mismatchedReturn.jott", true ));
        testCases.add(new TestCase("missing params in call", "missingFuncParams.jott", true ));
        testCases.add(new TestCase("missing main function", "missingMain.jott", true ));
        testCases.add(new TestCase("missing return", "missingReturn.jott", true ));
        testCases.add(new TestCase("missing return (if branches)", "noReturnIf.jott", true ));
        testCases.add(new TestCase("missing return (while)", "noReturnWhile.jott", true ));
        testCases.add(new TestCase("provided example 1", "providedExample1.jott", false ));
        testCases.add(new TestCase("return from void", "returnId.jott", true ));
        testCases.add(new TestCase("valid loop", "validLoop.jott", false ));
        testCases.add(new TestCase("void return", "voidReturn.jott", true ));
        testCases.add(new TestCase("while is a keyword", "whileKeyword.jott", true ));
    }

    private boolean parserTest(TestCase test, String orginalJottCode){
        try {
            ArrayList<Token> tokens = JottTokenizer.tokenize("phase3testcases/" + test.fileName);

            if (tokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a list of tokens, but got null");
                System.err.println("\t\tPlease verify your tokenizer is working properly");
                return false;
            }
            System.out.println(tokenListString(tokens));
            ArrayList<Token> cpyTokens = new ArrayList<>(tokens);
            JottTree root = JottParser.parse(tokens);

            if (!test.error && root == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a JottTree and got null");
                return false;
            } else if (test.error && root == null) {
                return true;
            } else if (test.error) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a null and got JottTree");
                return false;
            }

            System.out.println("Orginal Jott Code:\n");
            System.out.println(orginalJottCode);
            System.out.println();

            String jottCode = root.convertToJott();
            System.out.println("Resulting Jott Code:\n");
            System.out.println(jottCode);

            try {
                FileWriter writer = new FileWriter("phase3testcases/parserTestTemp.jott");
                if (jottCode == null) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Expected a program string; got null");
                    return false;
                }
                writer.write(jottCode);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Token> newTokens = JottTokenizer.tokenize("phase3testcases/parserTestTemp.jott");

            if (newTokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(tokens));
                System.err.println("Got: null");
                return false;
            }

            if (newTokens.size() != cpyTokens.size()) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(cpyTokens));
                System.err.println("Got:    : " + tokenListString(newTokens));
                return false;
            }

            for (int i = 0; i < newTokens.size(); i++) {
                Token n = newTokens.get(i);
                Token t = cpyTokens.get(i);

                if (!tokensEqualNoFileData(n, t)) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Token mismatch: Tokens do not match.");
                    System.err.println("Similar files should have same tokenization.");
                    System.err.println("Expected: " + tokenListString(cpyTokens));
                    System.err.println("Got     : " + tokenListString(newTokens));
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("Unknown Exception occured.");
            e.printStackTrace();
            return false;
        }
    }

    private String tokenListString(ArrayList<Token> tokens){
        StringBuilder sb = new StringBuilder();
        for (Token t: tokens) {
            sb.append(t.getToken());
            sb.append(":");
            sb.append(t.getTokenType().toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    private boolean runTest(TestCase test){
        System.out.println("Running Test: " + test.testName);
        String orginalJottCode;
        try {
            orginalJottCode = new String(
                    Files.readAllBytes(Paths.get("phase3testcases/" + test.fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return parserTest(test, orginalJottCode);

    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottValidateTester tester = new JottValidateTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for(JottValidateTester.TestCase test: tester.testCases){
            numTests++;
            if(tester.runTest(test)){
                passedTests++;
                System.out.println("\tPassed\n");
            }
            else{
                System.out.println("\tFailed\n");
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }
}
