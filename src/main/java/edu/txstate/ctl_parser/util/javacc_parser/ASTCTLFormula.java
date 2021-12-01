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

public class ASTCTLFormula extends CTLFormulaNode {

    public ASTCTLFormula(int i) {
        super(i);
    }

    public ASTCTLFormula(CTLParser p, int i) {
        super(p, i);
    }

    @Override
    public boolean check(State state) {
        return ((CTLFormulaNode) children[0]).check(state);
    }

    @Override
    public void mark(KripkeStructure model) {
    }

    @Override
    public String getMarking() {
        return ((CTLFormulaNode) jjtGetChild(0)).getMarking();
    }
}
