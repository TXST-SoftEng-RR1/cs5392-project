package edu.txstate.ctl_parser.model;

import java.util.ArrayList;

public class State {
    // final member variables as we do not want to re-bind them
    // to reference another object
    private final String name;
    private final ArrayList<String> atoms;
    private final ArrayList<State> transitions;
    private final ArrayList<String> marks;

    public State() {
        name = "n/a";
        atoms = new ArrayList<>();
        transitions = new ArrayList<>();
        marks = new ArrayList<>();
    }

    public State(String n) {
        name = n;
        atoms = new ArrayList<>();
        transitions = new ArrayList<>();
        marks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String[] getAtoms() {
        String[] result = new String[atoms.size()];
        for (int i = 0; i < atoms.size(); i++) {
            result[i] = atoms.get(i);
        }
        return result;
    }

    public String getAtomString() {
        StringBuilder result = new StringBuilder(15);

        for (String atom : atoms) {
            result.append(",").append(atom);
        }

        return result.toString();
    }

    public State[] getTransitions() {
        State[] result = new State[transitions.size()];
        for (int i = 0; i < transitions.size(); i++) {
            result[i] = transitions.get(i);
        }
        return result;
    }

    public void addTransition(State s) {
        transitions.add(s);
    }

    public void addAtom(String atom) {
        atoms.add(atom);
    }

    public boolean hasAtom(String atom) {
        return atoms.contains(atom);
    }

    public void mark(String mark) {
        if (!isMarked(mark))
            marks.add(mark);
    }

    public boolean isMarked(String mark) {
        return marks.contains(mark);
    }

    public String toMarkings() {
        return marks.toString();
    }
}
