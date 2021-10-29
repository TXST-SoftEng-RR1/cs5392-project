package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTTrue extends CTLFormulaNode {

	public ASTTrue(int i) {
		super(i);
	}

	public ASTTrue(CTLParser p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString()
	{
		return "T";
	}
	
	@Override
	public boolean check(State state) {
		return true;
	}

	@Override
	public void mark(KripkeStructure model) {
	}

	@Override
	public String getMarking() {
		return "T";
	}

	@Override
	public CTLFormulaNode clone() {
		return new ASTTrue(id);
	}
}
