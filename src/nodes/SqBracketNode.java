package src.nodes;

import java.util.Map;

/**
 * Class for a square bracket node.
 * Descendant of SimpleNode because it has a different Java, C, and Python conversion.
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class SqBracketNode extends SimpleNode{
    // Map of conversions because Jott is weird and uses square brackets instead of round...
    private static final Map<String, String> CONVERSIONS = Map.of(
            "[", "(",
            "]", ")"
    );

    public SqBracketNode(String value) {
        super(value);
    }

    @Override
    public String convertToJava(String className) {
        return CONVERSIONS.get(value);
    }

    @Override
    public String convertToC() {
        return CONVERSIONS.get(value);
    }

    @Override
    public String convertToPython() {
        return CONVERSIONS.get(value);
    }
}
