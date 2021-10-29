package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTExpr extends CTLFormulaNode {

	public ASTExpr(int i) {
		super(i);
	}

	public ASTExpr(CTLParser p, int i) {
		super(p, i);
	}
	
	@Override
	public String toString()
	{
		switch((Integer)value)
		{
			case CTLParserConstants.NOT:
				return "Not";
			case CTLParserConstants.AND:
				return "And";
			case CTLParserConstants.OR:
				return "Or";
			case CTLParserConstants.THEN:
				return "Then";
		}
		return CTLParserTreeConstants.jjtNodeName[id];
	}
	
	public void setType(int type)
	{
		
		if(type == CTLParserConstants.THEN)
		{
			SimpleNode n = new ASTExpr(id);
			n.jjtSetValue(CTLParserConstants.NOT);
			n.jjtAddChild(children[0], 0);
			children[0].jjtSetParent(n);
			n.jjtSetParent(this);
			this.jjtAddChild(n, 0);
			jjtSetValue(CTLParserConstants.OR);
		}
		else
		{
			jjtSetValue(type);
		}
	}

	@Override
	public boolean check(State state) {
		switch((Integer)value)
		{
		case CTLParserConstants.NOT:
			return !((CTLFormulaNode)jjtGetChild(0)).check(state);
		case CTLParserConstants.AND:
			return ((CTLFormulaNode)jjtGetChild(0)).check(state) && ((CTLFormulaNode)jjtGetChild(1)).check(state);
		case CTLParserConstants.OR:
			return ((CTLFormulaNode)jjtGetChild(0)).check(state) || ((CTLFormulaNode)jjtGetChild(1)).check(state);
		}
		return false;
	}
	
	@Override
	public void mark(KripkeStructure model) {
	}
	
	@Override
	public String getMarking()
	{
		switch((Integer)value)
		{
		case CTLParserConstants.NOT:
			return "(!"+((CTLFormulaNode)jjtGetChild(0)).getMarking()+")";
		case CTLParserConstants.AND:
			return "("+((CTLFormulaNode)jjtGetChild(0)).getMarking()+"&&"+((CTLFormulaNode)jjtGetChild(1)).getMarking()+")";
		case CTLParserConstants.OR:
			return "("+((CTLFormulaNode)jjtGetChild(0)).getMarking()+"||"+((CTLFormulaNode)jjtGetChild(1)).getMarking()+")";
		}
		return "";
	}

	@Override
	public CTLFormulaNode clone() {
		ASTExpr result = new ASTExpr(id);
		result.setType((Integer) this.jjtGetValue());
		for(int i=0; i<this.jjtGetNumChildren(); i++)
		{
			result.jjtAddChild(((CTLFormulaNode)this.jjtGetChild(i)).clone(), i);
		}
		
		return result;
	}
}
