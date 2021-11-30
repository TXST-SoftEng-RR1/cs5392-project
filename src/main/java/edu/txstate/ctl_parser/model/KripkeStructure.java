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

import edu.txstate.ctl_parser.util.javacc_parser.ASTCTLFormula;
import edu.txstate.ctl_parser.util.javacc_parser.CTLFormulaNode;
import edu.txstate.ctl_parser.util.javacc_parser.CTLParser;
import edu.txstate.ctl_parser.util.javacc_parser.ParseException;

import java.io.InputStream;
import java.util.HashMap;

public class KripkeStructure {
    private HashMap<String, State> states;

    public KripkeStructure() {
        states = new HashMap<>();
    }

    public HashMap<String, State> getStates() {
        return states;
    }

    public State getState(int idx) {
        State[] statesArr = states.values().toArray(new State[0]);
        return statesArr[idx];
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

    private boolean checkFormula(ASTCTLFormula formula, String stateName) {
        mark(formula);
        printMarkings();
        return formula.check(getState(stateName));
    }

    private void mark(CTLFormulaNode formula) {
        for (int i = 0; i < formula.jjtGetNumChildren(); i++) {
            mark((CTLFormulaNode) formula.jjtGetChild(i));
        }
        formula.mark(this);
    }

    public boolean validateFormula(InputStream formula, String state) {
        ASTCTLFormula astCtlFormula;
        CTLParser parser = null;
        if (CTLParser.isJj_initialized_once())
            CTLParser.ReInit(formula);
        else
            parser = new CTLParser(formula);

        try {
            astCtlFormula = parser.Formula();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return this.checkFormula(astCtlFormula, state);
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
        for (State state : states.values()) {
            System.out.println(state.getName() + ": " + state.toMarkings());
        }
    }
}
