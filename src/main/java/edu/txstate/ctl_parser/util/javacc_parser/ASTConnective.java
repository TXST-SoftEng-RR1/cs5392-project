package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTConnective extends CTLFormulaNode {
	private final int EX = 0;
	private final int EU = 1;
	private final int AF = 2;
	private int type;
	private boolean negate = false;
	private boolean applyOR = false;
	private CTLFormulaNode orWith;
	private final String[] typeNames = {"EX","EU","AF"};
	
	public ASTConnective(int i) {
		super(i);
		type = EX;
	}

	public ASTConnective(CTLParser p, int i) {
		super(p, i);
		type = EX;
	}
	
	@Override
	public String toString()
	{
		return typeNames[type];
	}
	
	public void setType(int path, int temporal)
	{
		if(path == CTLParserConstants.ALL)
		{
			switch(temporal)
			{
			case CTLParserConstants.NEXT:
				type = EX;
				negateSelf();
				negateChild(0);
				break;
			case CTLParserConstants.UNTIL:
				type = EU;
				negateChild(0);
				negateChild(1);
				
				ASTExpr andNode = new ASTExpr(CTLParserTreeConstants.JJTEXPR);
				andNode.setType(CTLParserConstants.AND);
				andNode.jjtAddChild(this.jjtGetChild(0), 0);
				this.jjtAddChild(this.jjtGetChild(1), 0);
				andNode.jjtAddChild(((CTLFormulaNode)this.jjtGetChild(0)).clone(), 1);
				this.jjtAddChild(andNode, 1);
				
				ASTExpr notNode = new ASTExpr(CTLParserTreeConstants.JJTEXPR);
				notNode.setType(CTLParserConstants.NOT);
				ASTConnective addition = new ASTConnective(id);
				addition.type = AF;
				addition.jjtAddChild((Node) ((CTLFormulaNode)((CTLFormulaNode)this.jjtGetChild(0)).jjtGetChild(0)).clone(), 0);
				notNode.jjtAddChild(addition, 0);
				orWith(notNode);
				negateSelf();
				break;
			case CTLParserConstants.FUTURE:
				type = AF;
				break;
			case CTLParserConstants.GLOBALLY:
				type = EU;
				negateSelf();
				negateChild(0);
				this.jjtAddChild(this.jjtGetChild(0), 1);
				this.jjtAddChild(new ASTTrue(0), 0);
			};
			
		}
		else //E...
		{
			switch(temporal)
			{
			case CTLParserConstants.NEXT:
				type = EX;
				break;
			case CTLParserConstants.UNTIL:
				type = EU;
				break;
			case CTLParserConstants.FUTURE:
				type = EU;
				this.jjtAddChild(this.jjtGetChild(0), 1);
				this.jjtAddChild(new ASTTrue(0), 0);
				break;
			case CTLParserConstants.GLOBALLY:
				type = AF;
				negateSelf();
				negateChild(0);
			};
		}
	}

	@Override
	public boolean check(State state) {
		return state.isMarked(getMarking());
	}

	@Override
	public void mark(KripkeStructure model) {
		String marking = getMarking();
		boolean changed;
		switch(type)
		{
			case EX:
				for(State state : model.getStates().values())
				{
					if(preCheckE(state, (CTLFormulaNode)jjtGetChild(0)) && !state.isMarked(marking))
					{
						state.mark(marking);
					}
				}
				break;
			case EU:
				for(State state : model.getStates().values())
				{
					if(((CTLFormulaNode)jjtGetChild(1)).check(state)) state.mark(marking);
				}
				changed = true;
				while(changed)
				{
					changed = false;
					for(State state : model.getStates().values())
					{
						if((preCheckE(state, this) && ((CTLFormulaNode)jjtGetChild(0)).check(state))
								&& !state.isMarked(marking))
						{
							state.mark(marking);
							changed = true;
						}
					}
				}
				break;
			case AF:
				for(State state : model.getStates().values())
				{
					if(preCheckA(state, (CTLFormulaNode)jjtGetChild(0)) ||
							(state.getTransitions().size() == 0 && ((CTLFormulaNode)jjtGetChild(0)).check(state)))
							{
								state.mark(marking);
							}
				}

				changed = true;
				while(changed)
				{
					changed = false;
					for(State state : model.getStates().values())
					{
						if(preCheckA(state, this) && !state.isMarked(marking))
						{
							System.out.println(state.getName()+": "+preCheckA(state, this)+", "+((CTLFormulaNode)jjtGetChild(0)).check(state));
							state.mark(marking);
							changed = true;
						}
					}
				}
				break;
			default:
				System.err.println("Something went wrong");
				break;
		}
	}
	
	private boolean preCheckE(State state, CTLFormulaNode phi)
	{
		//ArrayList<State> transitions = state.getTransitions();
		for (State transition : state.getTransitions()) {
			if (phi.check(transition)) return true;
		}
		return false;
	}
	
	private boolean preCheckA(State state, CTLFormulaNode phi)
	{
		//State[] transitions = state.getTransitions();
		if(state.getTransitionsSize() == 0) return false;
		for (State transition : state.getTransitions()) {
			if (!phi.check(transition) && state != transition) return false;
		}
		return true;
	}
	
	@Override
	public String getMarking() {
		switch(type)
		{
		case EX:
			return "EX"+((CTLFormulaNode)jjtGetChild(0)).getMarking();
		case EU:
			return "E["+((CTLFormulaNode)jjtGetChild(0)).getMarking()+"U"+((CTLFormulaNode)jjtGetChild(1)).getMarking()+"]";
		case AF:
			return "AF"+((CTLFormulaNode)jjtGetChild(0)).getMarking();
		}
		return "";
	}
	
	@Override
	public void added()
	{
		if(negate)
		{
			negate = false;
			((CTLFormulaNode)this.jjtGetParent()).negateChild(0);
		}
		if(applyOR)
		{
			applyOR = false;
			((CTLFormulaNode)this.jjtGetParent()).orChildWith(0, orWith);
		}
	}

	private void negateSelf()
	{
		negate = true;
	}
	private void orWith(CTLFormulaNode n)
	{
		orWith = n;
		applyOR = true;
	}
	
	@Override
	public CTLFormulaNode clone() {
		ASTConnective result = new ASTConnective(id);
		result.type = this.type;
		
		for(int i=0; i<this.jjtGetNumChildren(); i++)
		{
			result.jjtAddChild(((CTLFormulaNode)this.jjtGetChild(i)).clone(), i);
		}
		
		return result;
	}
}
