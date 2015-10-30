package sem;

import java.util.Map;

public abstract class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable;
	
	public Scope(Scope outer) { 
		this.outer = outer; 
	}
	
	public Scope() { this(null); }
	
	public Symbol lookup(String name) {
		// To be completed...
		return null;
	}
	
	public Symbol lookupCurrent(String name) {
		// To be completed...
		return null;
	}
	
	public void Symbol(Symbol sym) {
		symbolTable.put(sym.name, sym);
	}
}
