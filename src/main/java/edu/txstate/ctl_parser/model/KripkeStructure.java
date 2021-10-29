package edu.txstate.ctl_parser.model;

import edu.txstate.ctl_parser.util.javacc_parser.CTLFormulaNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KripkeStructure {
    private HashMap<String, State> states;

    public KripkeStructure() {
        states = new HashMap<>();
    }

//    private void addTransition(String s1, String s2) {
//
//    }

    public HashMap<String, State> getStates() {
        return states;
    }

    public void addState(State state) {
        states.put(state.getName(), state);
    }

    public State getState(String stateName) {
        return states.get(stateName);
    }

    public int getNumStates() {
        return states.size();
    }

//    public int getStateIndex(String name) {
//        for (int i = 0; i < states.length; i++) {
//            if (states[i].getName().equals(name)) return i;
//        }
//        return -1;
//    }

    public boolean checkFormula(CTLFormulaNode formula, String stateName) {
        mark(formula);
        return formula.check(getState(stateName));
    }

    private void mark(CTLFormulaNode formula) {
        for (int i = 0; i < formula.jjtGetNumChildren(); i++) {
            mark((CTLFormulaNode) formula.jjtGetChild(i));
        }
        formula.mark(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append("States: \n");
        for (State state : getStates().values())
            stringBuilder.append(state.toString());

        return stringBuilder.toString();
    }

    public void printMarkings() {
//        for (State state : states) {
//            System.out.println(state.getName() + ": " + state.toMarkings());
//        }
    }
}
