/*
 * Copyright (c) 2021 borislavsabotinov.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.txstate.ctl_parser.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class State {
    // final member variables as we do not want to re-bind them
    // to reference another object
    private final String name;
    private final Set<Atom> atoms;
    private final ArrayList<State> transitions;
    private final ArrayList<String> marks;
    private final Logger logger = Logger.getLogger(State.class.getName());

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
        return transitions;
    }

    public int getTransitionsSize() {
        return transitions.size();
    }

    public void addTransition(State s) {
        if (!transitions.contains(s))
            transitions.add(s);
        else
            logger.warning("Warning: Attempting to add an existing transition; ignoring! ");
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
