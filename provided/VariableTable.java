package provided;

import provided.nodes.TypeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for variable table which allows for variable type and initialization tracking
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class VariableTable extends SymbolTable {
    protected record VariableStuff(TypeNode.VariableType type, Boolean initialized) { }
    protected final Map<String, VariableStuff> variableStuffMap;

    public VariableTable(SymbolTable parent) {
        super(parent);
        this.variableStuffMap = new HashMap<>();
    }

    /**
     * Try to declare a variable to the table
     * @param name the name of the variable
     * @param type the type of the variable
     * @return true if the variable was successfully declared, false if the variable already exists in the variable or function table
     */
    public boolean tryDeclareVariable(String name, TypeNode.VariableType type) {
        if (variableStuffMap.containsKey(name) || functionStuffMap.containsKey(name))
            return false;
        variableStuffMap.put(name, new VariableStuff(type, false));
        return true;
    }

    /**
     * Set that a variable has been assigned.
     * We need to store this information to make sure that variables are
     * initialized before use.
     * @param name the name of the variable
     */
    public void assignVariable(String name) {
        VariableStuff variableStuff = variableStuffMap.get(name);
        variableStuffMap.put(name, new VariableStuff(variableStuff.type, true));
    }

    /**
     * Check if a variable exists
     * @param name the name of the variable
     * @return true if the variable exists, false otherwise
     */
    public boolean hasVariable(String name) {
        return variableStuffMap.containsKey(name);
    }

    /**
     * Check if a variable is initialized
     * <p>
     *     Note: Check if the variable exists before trying to check if it is initialized, so you can throw the appropriate error
     * </p>
     * @param name the name of the variable
     * @return true if the variable is initialized, false otherwise
     */
    public boolean isVariableInitialized(String name) {
        return variableStuffMap.get(name).initialized;
    }

    /**
     * Get the type of variable
     * <p>
     *     Note: Check if the variable exists before trying to get its type, so you can throw the appropriate error
     * </p>
     * @param name the name of the variable
     * @return the type of the variable or null if the variable does not exist
     */
    public TypeNode.VariableType getVariableType(String name) {
        return variableStuffMap.get(name).type;
    }
}
