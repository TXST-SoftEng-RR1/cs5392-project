package edu.txstate.ctl_parser.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class State {
    // final member variables as we do not want to re-bind them
    // to reference another object
    private final String name;
    private final Set<Atom> atoms;
    private final ArrayList<State> transitions;
    private final ArrayList<String> marks;

    public State(String n) {
        name = n;
        atoms = new HashSet<>();
        transitions = new ArrayList<>();
        marks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Set<Atom> getAtoms() {
        return atoms;
    }

    public String getAtomString() {
        StringBuilder result = new StringBuilder(15);
        for (Atom atom : atoms) {
            result.append(",").append(atom.toString());
        }

        return result.toString();
    }

    public ArrayList<State> getTransitions() {
//        State[] result = new State[transitions.size()];
//        for (int i = 0; i < transitions.size(); i++) {
//            result[i] = transitions.get(i);
//        }
//        return result;
        return transitions;
    }

    public int getTransitionsSize() {
        return transitions.size();
    }

    public void addTransition(State s) {
        transitions.add(s);
    }

    public void addAtom(char atomName) {
        //atoms.add(atom);
        atoms.add(new Atom(atomName));
    }

    public boolean hasAtom(String atom) {
        for (Atom tmpAtom : atoms) {
            if (tmpAtom.getName() == atom.charAt(0))
                return true;
        }
        return false;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(25);
        stringBuilder.append("  State: ").append(this.getName());
        stringBuilder.append("\n");

        stringBuilder.append("     Atoms: ");
        for (Atom atom : atoms)
            stringBuilder.append(atom.toString());
        stringBuilder.append("\n");

        stringBuilder.append("     Transitions: ");
        for (State transition : transitions)
            stringBuilder.append(transition.getName()).append(" ");
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
