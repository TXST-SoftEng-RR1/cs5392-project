package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTAtom extends CTLFormulaNode {
	private String name;
	
	public ASTAtom(int i) {
		super(i);
	}

	public ASTAtom(CTLParser p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}

	@Override
	public boolean check(State state) {
		return state.hasAtom(name);
	}

	@Override
	public void mark(KripkeStructure model) {
	}

	@Override
	public String getMarking() {
		return name;
	}
	
	@Override
	public CTLFormulaNode clone() {
		ASTAtom result = new ASTAtom(id);
		result.setName(name);
		return result;
	}
}
