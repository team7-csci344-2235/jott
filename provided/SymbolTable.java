package provided;

import provided.nodes.TypeNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for symbol table which allows for function tracking.
 * <p>
 *      I felt lazy and didn't want to make exceptions. Be sure to check stuff if necessary!
 * </p>
 *
 * @author Ethan Hartman <ehh4525@rit.edu>
 */
public class SymbolTable {
    protected record FunctionStuff(ArrayList<TypeNode.VariableType> params, TypeNode.VariableType returnType) { }

    /**
     * Map where the name is the key and the value is the Function's 'stuff' (params, returnType).
     */
    protected final HashMap<String, FunctionStuff> functionStuffMap;

    protected SymbolTable(SymbolTable parent) {
        this.functionStuffMap = parent.functionStuffMap;
    }

    public SymbolTable() {
        this.functionStuffMap = new HashMap<>();
        ArrayList<TypeNode.VariableType> valforConcat = new ArrayList<>();
        valforConcat.add(TypeNode.VariableType.STRING);
        valforConcat.add(TypeNode.VariableType.STRING);
        tryAddFunction("concat", valforConcat, TypeNode.VariableType.STRING);

        ArrayList<TypeNode.VariableType> valsForLength = new ArrayList<>();
        valsForLength.add(TypeNode.VariableType.STRING);
        tryAddFunction("length", valsForLength, TypeNode.VariableType.INTEGER);
    }

    /**
     * Create a new variable table
     * @return the new variable table
     */
    public VariableTable createVariableTable() {
        return new VariableTable(this);
    }

    /**
     * Try to add a function to the table
     * @param name the name of the function
     * @param params the parameters of the function
     * @return true if the function was added, false if the function already exists
     */
    public boolean tryAddFunction(String name, ArrayList<TypeNode.VariableType> params, TypeNode.VariableType returnType) {
        if (functionStuffMap.containsKey(name))
            return false;
        functionStuffMap.put(name, new FunctionStuff(params, returnType));
        return true;
    }

    /**
     * Check if a function exists in the table
     * @param name the name of the function
     * @return true if the function exists, false otherwise
     */
    public boolean hasFunction(String name) {
        return functionStuffMap.containsKey(name);
    }

    /**
     * Get the parameters of a function. Assumes the function exists.
     * @param name the name of the function
     * @return the parameters of the function
     */
    public ArrayList<TypeNode.VariableType> getFunctionParams(String name) {
        return functionStuffMap.get(name).params;
    }

    /**
     * Get function return type. Assumes the function exists.
     * @param name the name of the function
     * @return the return type of the function
     */
    public TypeNode.VariableType getFunctionReturnType(String name) {
        return functionStuffMap.get(name).returnType;
    }
}
