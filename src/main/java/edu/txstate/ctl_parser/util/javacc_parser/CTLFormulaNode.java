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

public abstract class CTLFormulaNode extends SimpleNode {

    public CTLFormulaNode(int i) {
        super(i);
    }

    public CTLFormulaNode(CTLParser p, int i) {
        super(p, i);
    }

    abstract public boolean check(State state);

    abstract public void mark(KripkeStructure model);

    abstract public String getMarking();

    public int getChildIndex(CTLFormulaNode child) {
        for (int i = 0; i < children.length; i++) {
            if (children[i] == child) return i;
        }

        return -1;
    }

    public void encapChild(CTLFormulaNode cap, int child1, int child2) {
        cap.jjtSetParent(this);
        this.jjtGetChild(child1).jjtSetParent(cap);
        cap.jjtAddChild(this.jjtGetChild(child1), 0);
        this.jjtAddChild(cap, child1);

        if (child2 != -1) {
            cap.jjtAddChild(this.jjtGetChild(child2), 1);
            this.jjtGetChild(child1).jjtSetParent(cap);
        }
    }

    public void negateChild(int child) {
        ASTExpr node = new ASTExpr(CTLParserTreeConstants.JJTEXPR);
        node.setType(CTLParserConstants.NOT);
        encapChild(node, child, -1);
    }

    public void orChildWith(int child, CTLFormulaNode n) {
        ASTExpr node = new ASTExpr(CTLParserTreeConstants.JJTEXPR);
        node.setType(CTLParserConstants.OR);
        node.jjtAddChild(n, 1);
        encapChild(node, child, -1);
    }

    @Override
    public void jjtAddChild(Node n, int i) {
        super.jjtAddChild(n, i);
        ((CTLFormulaNode) n).added();
    }

    public void added() {
    }

    public CTLFormulaNode clone() {
        return null;
    }
}
