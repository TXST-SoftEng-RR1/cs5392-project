package edu.txstate.ctl_parser.model;

import edu.txstate.ctl_parser.util.javacc_parser.CTLFormulaNode;

public class KripkeStructure {
    private State[] states;

    public KripkeStructure() {

    }

    private void addTransition(String s1, String s2) {
        int S1, S2;
        S1 = this.getStateIndex(s1);
        S2 = this.getStateIndex(s2);
        if (S1 != -1 && S2 != -1) {
            this.getState(S1).addTransition(this.getState(S2));
        }
    }

    public String[] getStates() {
        String[] result = new String[states.length];
        for (int i = 0; i < states.length; i++) {
            result[i] = states[i].getName();
        }
        return result;
    }

    public State getState(int i) {
        return states[i];
    }

    public int getNumStates() {
        return states.length;
    }

    public int getStateIndex(String name) {
        for (int i = 0; i < states.length; i++) {
            if (states[i].getName().equals(name)) return i;
        }
        return -1;
    }

    public boolean checkFormula(CTLFormulaNode formula, String state) {
        mark(formula);
        return formula.check(getState(getStateIndex(state)));
    }

    private void mark(CTLFormulaNode formula) {
        for (int i = 0; i < formula.jjtGetNumChildren(); i++) {
            mark((CTLFormulaNode) formula.jjtGetChild(i));
        }
        formula.mark(this);
    }

    public void printMarkings() {
        for (State state : states) {
            System.out.println(state.getName() + ": " + state.toMarkings());
        }
    }
}
