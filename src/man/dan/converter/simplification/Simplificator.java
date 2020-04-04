package man.dan.converter.simplification;

import man.dan.converter.lexer.Num;
import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.*;
import man.dan.converter.representation.Number;

import java.util.LinkedList;

public class Simplificator {
    protected LinkedList<Call> chain;

    public Simplificator(LinkedList<Call> ch) {
        chain = ch;
    }

    public void simpl() throws TypeError {
        for(Call cLink : chain) {
            simplifyTree(cLink);
        }
    }

    protected void simplifyTree(Call treeCont) throws TypeError {
        treeCont.setVertex(simplNode(treeCont.getVertex()));
    }

    protected Node simplNode(Node vertex) {
        if (vertex == null || vertex instanceof Number)
            return vertex;

        if (vertex instanceof Operator) {
            if (((Operator) vertex).getLeft() instanceof Number && ((Operator) vertex).getRight() instanceof Number) {
                long res;
                if (vertex instanceof Multiple) {
                    res = (long)((Number) (((Multiple) vertex).getLeft())).getVal() * (long)((Number) (((Multiple) vertex).getRight())).getVal();
                } else if (vertex instanceof Plus) {
                    res = (long)((Number) (((Plus) vertex).getLeft())).getVal() + (long)((Number) (((Plus) vertex).getRight())).getVal();
                } else if (vertex instanceof Minus) {
                    res = (long)((Number) (((Minus) vertex).getLeft())).getVal() - (long)((Number) (((Minus) vertex).getRight())).getVal();
                } else {
                    return vertex;
                }

                if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE)
                    return vertex;

                ((Number) ((Operator) vertex).getLeft()).setVal((int) res);
                ((Operator) vertex).getLeft().setParent(vertex.getParent());

                if (((Operator) vertex.getParent()).getLeft() == vertex) {
                    ((Operator) vertex.getParent()).setLeft(((Operator) vertex).getLeft());
                } else if (((Operator) vertex.getParent()).getRight() == vertex) {
                    ((Operator) vertex.getParent()).setRight(((Operator) vertex).getLeft());
                }

                return vertex;
            }

            simplNode(((Operator) vertex).getLeft());
            simplNode(((Operator) vertex).getRight());
        }

        return vertex;

    }
}
