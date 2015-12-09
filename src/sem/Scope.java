package sem;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable = new HashMap<>();
	
	public Scope(Scope outer) {
		this.outer = outer;
	}
	
	public Scope() { this(null); }
	
	public Symbol lookup(String name) {
		Symbol s = symbolTable.get(name);
		if (s == null && outer != null)
			return outer.lookup(name);
		return s;
	}
	
	public Symbol lookupCurrent(String name) {
		return symbolTable.get(name);
	}
	
	public void put(Symbol sym) {
		symbolTable.put(sym.name, sym);
	}
}
