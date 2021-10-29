package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTCTLFormula extends CTLFormulaNode {

	public ASTCTLFormula(int i) {
		super(i);
	}

	public ASTCTLFormula(CTLParser p, int i) {
		super(p, i);
	}
	
	@Override
	public boolean check(State state) {
		return ((CTLFormulaNode)children[0]).check(state);
	}

	@Override
	public void mark(KripkeStructure model) {
	}

	@Override
	public String getMarking() {
		return ((CTLFormulaNode)jjtGetChild(0)).getMarking();
	}
}
