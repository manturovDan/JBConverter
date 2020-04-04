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
            simplNode(((Operator) vertex).getLeft());
            simplNode(((Operator) vertex).getRight());

            if (((Operator) vertex).getLeft() instanceof Number && ((Operator) vertex).getRight() instanceof Number) {
                return simplBinNum((Operator) vertex);
            }

            if (vertex instanceof Multiple) {
                if (((Multiple) vertex).getRight() instanceof Number && ((Multiple) vertex).getLeft() instanceof Multiple) {
                    if (((Multiple) ((Multiple) vertex).getLeft()).getLeft() instanceof Number) {
                        return simpleMul((Number) ((Multiple) ((Multiple) vertex).getLeft()).getLeft(), (Number) ((Multiple) vertex).getRight(),
                                ((Multiple) ((Multiple) vertex).getLeft()).getRight(), (Multiple) vertex);
                    } else if (((Multiple) ((Multiple) vertex).getLeft()).getRight() instanceof Number) {
                        return simpleMul((Number) ((Multiple) ((Multiple) vertex).getLeft()).getRight(), (Number) ((Multiple) vertex).getRight(),
                                ((Multiple) ((Multiple) vertex).getLeft()).getLeft(), (Multiple) vertex);
                    }
                } else if (((Multiple) vertex).getLeft() instanceof Number && ((Multiple) vertex).getRight() instanceof Multiple) {
                    if (((Multiple) ((Multiple) vertex).getRight()).getLeft() instanceof Number) {
                        return simpleMul((Number) ((Multiple) ((Multiple) vertex).getRight()).getLeft(), (Number) ((Multiple) vertex).getLeft(),
                                ((Multiple) ((Multiple) vertex).getRight()).getRight(), (Multiple) vertex);
                    } else if (((Multiple) ((Multiple) vertex).getRight()).getRight() instanceof Number) {
                        return simpleMul((Number) ((Multiple) ((Multiple) vertex).getRight()).getRight(), (Number) ((Multiple) vertex).getLeft(),
                                ((Multiple) ((Multiple) vertex).getRight()).getLeft(), (Multiple) vertex);
                    }
                }

            }
        }

        return vertex;

    }

    public Node simplBinNum(Operator vertex) {
        long res;
        if (vertex instanceof Multiple) {
            res = (long)((Number) (vertex.getLeft())).getVal() * (long)((Number) (vertex.getRight())).getVal();
        } else if (vertex instanceof Plus) {
            res = (long)((Number) (vertex.getLeft())).getVal() + (long)((Number) (vertex.getRight())).getVal();
        } else if (vertex instanceof Minus) {
            res = (long)((Number) (vertex.getLeft())).getVal() - (long)((Number) (vertex.getRight())).getVal();
        } else {
            return vertex;
        }

        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE)
            return vertex;

        ((Number) vertex.getLeft()).setVal((int) res);
        vertex.getLeft().setParent(vertex.getParent());

        if (((Operator) vertex.getParent()).getLeft() == vertex) {
            ((Operator) vertex.getParent()).setLeft( vertex.getLeft());
        } else if (((Operator) vertex.getParent()).getRight() == vertex) {
            ((Operator) vertex.getParent()).setRight(vertex.getLeft());
        }

        return vertex;
    }

    public Node simpleMul(Number n1, Number n2, Node x, Multiple vertex) {
        long res = (long)n1.getVal() * (long)n2.getVal();

        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE)
            return vertex;

        n1.setVal((int)res);
        n1.setParent(vertex);
        vertex.setLeft(n1);
        x.setParent(vertex);
        vertex.setRight(x);

        return vertex;
    }
}
