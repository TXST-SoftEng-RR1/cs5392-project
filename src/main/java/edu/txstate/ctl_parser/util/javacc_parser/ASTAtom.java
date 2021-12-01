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

package edu.txstate.ctl_parser.util.javacc_parser;

import edu.txstate.ctl_parser.model.KripkeStructure;
import edu.txstate.ctl_parser.model.State;

public class ASTAtom extends CTLFormulaNode {
    private String name;

    public ASTAtom(int i) {
        super(i);
    }

    public ASTAtom(CTLParser p, int i) {
        super(p, i);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public boolean check(State state) {
        return state.hasAtom(name);
    }

    @Override
    public void mark(KripkeStructure model) {
    }

    @Override
    public String getMarking() {
        return name;
    }

    @Override
    public CTLFormulaNode clone() {
        ASTAtom result = new ASTAtom(id);
        result.setName(name);
        return result;
    }
}
